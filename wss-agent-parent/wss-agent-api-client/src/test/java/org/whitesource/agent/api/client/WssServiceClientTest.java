package org.whitesource.agent.api.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.whitesource.agent.api.JsonUtils;
import org.whitesource.agent.api.dispatch.*;
import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.Coordinates;
import org.whitesource.agent.api.model.DependencyInfo;
import org.whitesource.api.client.WssServiceClient;
import org.whitesource.api.client.WssServiceClientImpl;
import org.whitesource.api.client.WssServiceException;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class WssServiceClientTest {

	/* --- Static members --- */
	
	private static final Log log = LogFactory.getLog(WssServiceClientTest.class);
	
	/* --- Members --- */
	
	private WssServiceClient client;
	
	private RequestFactory requestFactory;
	
	private LocalTestServer server;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/* --- Life cycle methods --- */
	
	@Before
	public void setUp() throws Exception {
	    server = new LocalTestServer(null, null);
	    server.start();
	    
	    String serviceUrl = "http:/" + server.getServiceAddress().toString() + "/agent";
		client = new WssServiceClientImpl(serviceUrl);
		log.info("Local test server is available at " + serviceUrl);
		
		requestFactory = new RequestFactory("Client Test", "1.0");
	}
	
	@After
	public void tearDown() throws Exception {
		client.shutdown();
		server.stop();
	}

	/* --- Test methods --- */
	
	@Test
	public void testProperties() {
		PropertiesResult response = new PropertiesResult(new Properties());
		server.register("/agent", createHandler(response));
		
		PropertiesRequest request = requestFactory.newPropertiesRequest(null);
		PropertiesResult result = null; 
		try {
			result = client.getProperties(request);
		} catch (WssServiceException e) {
			log.error("Error getting properties", e);
			fail("Unable to get properties");
		}
		
		assertNotNull(result);
		assertNotNull(result.getProperties());
	}
	
	@Test
	public void testReport() {
		Map<String, Integer> licenseDistribution = new HashMap<String, Integer>();
		licenseDistribution.put("Apache 2.0", 10);
		licenseDistribution.put("GPL 3.0", 1);
		ReportResult response = new ReportResult(licenseDistribution, 8);
		server.register("/agent", createHandler(response));
		
		Collection<DependencyInfo> dependencies = Arrays.asList(new DependencyInfo("groupId", "artifactId", "version"));
		ReportRequest request = requestFactory.newReportRequest(dependencies);
		
		ReportResult result = null;
		try {
			result = client.getReport(request);
		} catch (WssServiceException e) {
			log.error("Error getting report", e);
			fail("Unable to get report");
		}
		
		assertNotNull(result);
		assertEquals(8, result.getNumOfNewerVersions());
		assertNotNull(result.getLicenseDistribution());
		assertEquals(10, result.getLicenseDistribution().get("Apache 2.0").intValue());
		assertEquals(1, result.getLicenseDistribution().get("GPL 3.0").intValue());
	}
	
	@Test
	public void testUpdateInventory() {
		UpdateInventoryResult response = new UpdateInventoryResult("WhiteSource");
		response.setCreatedProjects(Arrays.asList("newProject - 0.0.1"));
		response.setUpdatedProjects(Arrays.asList("exitingProject - 1.0"));
		server.register("/agent", createHandler(response));
		
		Collection<AgentProjectInfo> projects = new ArrayList<AgentProjectInfo>();
		
		AgentProjectInfo info = new AgentProjectInfo();
		info.setCoordinates(new Coordinates("org.whitesource", "exitingProject", "1.0"));
		info.setDependencies(Arrays.asList(new DependencyInfo("groupId", "artifactId", "version")));
		projects.add(info);
		
		info = new AgentProjectInfo();
		info.setCoordinates(new Coordinates("org.whitesource", "newProject", "0.0.1-SNAPSHOT"));
		info.setDependencies(Arrays.asList(new DependencyInfo("groupId", "artifactId", "version")));
		projects.add(info);
		
		UpdateInventoryRequest request = requestFactory.newUpdateInventoryRequest("orgToken", projects);
		
		UpdateInventoryResult result = null;
		try {
			result = client.updateInventory(request);
		} catch (WssServiceException e) {
			log.error("Error updating inventory", e);
			fail("Unable to update inventory");
		}
		
		assertNotNull(result);
		assertEquals("WhiteSource", result.getOrganization());
		assertThat(result.getCreatedProjects(), hasItems("newProject - 0.0.1"));
		assertThat(result.getUpdatedProjects(), hasItems("exitingProject - 1.0"));
	}
	
	@Test
	public void testServerError() throws WssServiceException {
		thrown.expect(WssServiceException.class);
		thrown.expectMessage(ResultEnvelope.MESSAGE_ILLEGAL_ARGUMENTS);
		
		PropertiesResult response = new PropertiesResult(new Properties());
		server.register("/agent", createHandler(response, ResultEnvelope.STATUS_BAD_REQUEST, ResultEnvelope.MESSAGE_ILLEGAL_ARGUMENTS));
		client.getProperties(requestFactory.newPropertiesRequest(null));
	}
	
	@Test
	public void testHttpError() throws WssServiceException {
		thrown.expect(WssServiceException.class);
		thrown.expectMessage("HTTP response code: " + HttpStatus.SC_FORBIDDEN);
		
		server.register("/agent", new HttpRequestHandler() {
			
			@Override
			public void handle(HttpRequest request, HttpResponse response,
					HttpContext context) throws HttpException, IOException {
				response.setStatusCode(HttpStatus.SC_FORBIDDEN);				
			}
		});
		
		client.getProperties(requestFactory.newPropertiesRequest(null));
	}
	
	@Test
	public void testEmptyResponse() throws WssServiceException {
		thrown.expect(WssServiceException.class);
		
		server.register("/agent", new HttpRequestHandler() {
			
			@Override
			public void handle(HttpRequest request, HttpResponse response,
					HttpContext context) throws HttpException, IOException {
				response.setEntity(null);
				response.setStatusCode(HttpStatus.SC_OK);				
			}
		});
		
		client.getProperties(requestFactory.newPropertiesRequest(null));
	}
	
	/* --- Private methods --- */
	
	private HttpRequestHandler createHandler(final Object result) {
		return createHandler(result, ResultEnvelope.STATUS_SUCCESS, ResultEnvelope.MESSAGE_OK);
	}
	
	private HttpRequestHandler createHandler(final Object result, final int status, final String message) {
		return new HttpRequestHandler() {
			
			@Override
			public void handle(HttpRequest request, HttpResponse response,
					HttpContext context) throws HttpException, IOException {

				String data = JsonUtils.toJson(result);
				ResultEnvelope envelope = new ResultEnvelope(status, message, data);
				String json = JsonUtils.toJson(envelope);
				HttpEntity entity = new StringEntity(json, "utf8");
				response.setEntity(entity);
				
				response.setStatusCode(HttpStatus.SC_OK);
			}
		};
	}
	
}