/**
 * Copyright (C) 2012 White Source Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whitesource.agent.api.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.whitesource.agent.api.APIConstants;
import org.whitesource.agent.api.dispatch.*;
import org.whitesource.agent.api.model.*;
import org.whitesource.api.client.WssServiceClient;
import org.whitesource.api.client.WssServiceClientImpl;
import org.whitesource.api.client.WssServiceException;

import java.io.IOException;
import java.lang.reflect.Type;
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
    public void testPropertiesRequestSentOk() {
        final PropertiesRequest propertiesRequest = requestFactory.newPropertiesRequest(null);

        HttpRequestHandler handler = new HttpRequestHandler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                List<NameValuePair> nvps = URLEncodedUtils.parse(entity);
                for (NameValuePair nvp : nvps) {
                    if (nvp.getName().equals(APIConstants.PARAM_REQUEST_TYPE)) {
                        assertEquals(nvp.getValue(), propertiesRequest.type().toString());
                    } else if (nvp.getName().equals(APIConstants.PARAM_AGENT)) {
                        assertEquals(nvp.getValue(), propertiesRequest.agent());
                    } else if (nvp.getName().equals(APIConstants.PARAM_AGENT_VERSION)) {
                        assertEquals(nvp.getValue(), propertiesRequest.agentVersion());
                    } else if (nvp.getName().equals(APIConstants.PARAM_TOKEN)) {
                        assertEquals(nvp.getValue(), propertiesRequest.orgToken());
                    }
                }
            }
        };
        server.register("/agent", handler);

        try {
            client.getProperties(propertiesRequest);
        } catch (WssServiceException e) {
            // suppress exception
        }
    }

    @Test
    public void testUpdateRequestSentOk() {
        final Collection<AgentProjectInfo> projects = new ArrayList<AgentProjectInfo>();
        final AgentProjectInfo projectInfo = new AgentProjectInfo();
        projectInfo.setProjectToken("projectToken");
        projectInfo.setCoordinates(new Coordinates("groupId", "artifactId", "version"));
        projectInfo.setParentCoordinates(new Coordinates("groupId", "parent-artifactId", "version"));
        final DependencyInfo dependencyInfo = new DependencyInfo("dep-groupId", "dep-artifactId", "dep-version");
        projectInfo.getDependencies().add(dependencyInfo);
        projects.add(projectInfo);
        final UpdateInventoryRequest updateInventoryRequest = requestFactory.newUpdateInventoryRequest("orgToken", projects);

        HttpRequestHandler handler = new HttpRequestHandler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                List<NameValuePair> nvps = URLEncodedUtils.parse(entity);
                for (NameValuePair nvp : nvps) {
                    if (nvp.getName().equals(APIConstants.PARAM_REQUEST_TYPE)) {
                        assertEquals(nvp.getValue(), updateInventoryRequest.type().toString());
                    } else if (nvp.getName().equals(APIConstants.PARAM_AGENT)) {
                        assertEquals(nvp.getValue(), updateInventoryRequest.agent());
                    } else if (nvp.getName().equals(APIConstants.PARAM_AGENT_VERSION)) {
                        assertEquals(nvp.getValue(), updateInventoryRequest.agentVersion());
                    } else if (nvp.getName().equals(APIConstants.PARAM_TOKEN)) {
                        assertEquals(nvp.getValue(), updateInventoryRequest.orgToken());
                    } else if (nvp.getName().equals(APIConstants.PARAM_TIME_STAMP)) {
                        assertEquals(nvp.getValue(), Long.toString(updateInventoryRequest.timeStamp()));
                    } else if (nvp.getName().equals(APIConstants.PARAM_DIFF)) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<Collection<AgentProjectInfo>>() {
                        }.getType();
                        final Collection<AgentProjectInfo> tmpProjects = gson.fromJson(nvp.getValue(), type);
                        assertEquals(tmpProjects.size(), 1);
                        final AgentProjectInfo info = tmpProjects.iterator().next();
                        assertEquals(info.getProjectToken(), projectInfo.getProjectToken());
                        assertEquals(info.getCoordinates(), projectInfo.getCoordinates());
                        assertEquals(info.getParentCoordinates(), projectInfo.getParentCoordinates());
                        assertEquals(info.getDependencies().size(), 1);
                        assertEquals(info.getDependencies().iterator().next(), dependencyInfo);
                    }
                }
            }
        };
        server.register("/agent", handler);
        try {
            client.updateInventory(updateInventoryRequest);
        } catch (WssServiceException e) {
            // suppress exception
        }
    }

    @Test
    public void testCheckPoliciesRequestSentOk() {
        final Collection<AgentProjectInfo> projects = new ArrayList<AgentProjectInfo>();
        final AgentProjectInfo projectInfo = new AgentProjectInfo();
        projectInfo.setProjectToken("projectToken");
        projectInfo.setCoordinates(new Coordinates("groupId", "artifactId", "version"));
        projectInfo.setParentCoordinates(new Coordinates("groupId", "parent-artifactId", "version"));
        final DependencyInfo dependencyInfo = new DependencyInfo("dep-groupId", "dep-artifactId", "dep-version");
        projectInfo.getDependencies().add(dependencyInfo);
        projects.add(projectInfo);
        final CheckPoliciesRequest checkPoliciesRequest = requestFactory.newCheckPoliciesRequest("orgToken", projects);

        HttpRequestHandler handler = new HttpRequestHandler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                List<NameValuePair> nvps = URLEncodedUtils.parse(entity);
                for (NameValuePair nvp : nvps) {
                    if (nvp.getName().equals(APIConstants.PARAM_REQUEST_TYPE)) {
                        assertEquals(nvp.getValue(), checkPoliciesRequest.type().toString());
                    } else if (nvp.getName().equals(APIConstants.PARAM_AGENT)) {
                        assertEquals(nvp.getValue(), checkPoliciesRequest.agent());
                    } else if (nvp.getName().equals(APIConstants.PARAM_AGENT_VERSION)) {
                        assertEquals(nvp.getValue(), checkPoliciesRequest.agentVersion());
                    } else if (nvp.getName().equals(APIConstants.PARAM_TOKEN)) {
                        assertEquals(nvp.getValue(), checkPoliciesRequest.orgToken());
                    } else if (nvp.getName().equals(APIConstants.PARAM_TIME_STAMP)) {
                        assertEquals(nvp.getValue(), Long.toString(checkPoliciesRequest.timeStamp()));
                    } else if (nvp.getName().equals(APIConstants.PARAM_DIFF)) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<Collection<AgentProjectInfo>>() {
                        }.getType();
                        final Collection<AgentProjectInfo> tmpProjects = gson.fromJson(nvp.getValue(), type);
                        assertEquals(tmpProjects.size(), 1);
                        final AgentProjectInfo info = tmpProjects.iterator().next();
                        assertEquals(info.getProjectToken(), projectInfo.getProjectToken());
                        assertEquals(info.getCoordinates(), projectInfo.getCoordinates());
                        assertEquals(info.getParentCoordinates(), projectInfo.getParentCoordinates());
                        assertEquals(info.getDependencies().size(), 1);
                        assertEquals(info.getDependencies().iterator().next(), dependencyInfo);
                    }
                }
            }
        };
        server.register("/agent", handler);
        try {
            client.checkPolicies(checkPoliciesRequest);
        } catch (WssServiceException e) {
            // suppress exception
        }
    }

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
    public void testCheckPolicies() {
        CheckPoliciesResult response = new CheckPoliciesResult("WhiteSource");

        Map<String, PolicyCheckResourceNode> existingProjects = new HashMap<String, PolicyCheckResourceNode>();
        Map<String, PolicyCheckResourceNode> newProjects = new HashMap<String, PolicyCheckResourceNode>();
        Map<String, Collection<ResourceInfo>> projectNewResources = new HashMap<String, Collection<ResourceInfo>>();

        response.setExistingProjects(existingProjects);
        response.setNewProjects(newProjects);
        response.setProjectNewResources(projectNewResources);

        PolicyCheckResourceNode root = new PolicyCheckResourceNode();
        existingProjects.put("exitingProject - 1.0", root);

        ResourceInfo resource1 = new ResourceInfo("artifactId-1");
        ResourceInfo resource2 = new ResourceInfo("artifactId-2");
        ResourceInfo resource3 = new ResourceInfo("artifactId-3");
        ResourceInfo resource4 = new ResourceInfo("artifactId-4");
        ResourceInfo resource5 = new ResourceInfo("artifactId-5");
        ResourceInfo resource6 = new ResourceInfo("artifactId-6");
        ResourceInfo resource7 = new ResourceInfo("artifactId-7");
        ResourceInfo resource8 = new ResourceInfo("artifactId-8");
        ResourceInfo resource11 = new ResourceInfo("artifactId-11");
        ResourceInfo resource12 = new ResourceInfo("artifactId-12");
        PolicyCheckResourceNode node = new PolicyCheckResourceNode(resource1, new RequestPolicyInfo("policy-1"));
        root.getChildren().add(node);
        node = new PolicyCheckResourceNode(resource2, null);
        root.getChildren().add(node);
        node = new PolicyCheckResourceNode(resource3, null);
        root.getChildren().add(node);
        node.getChildren().add(new PolicyCheckResourceNode(resource4, null));
        node.getChildren().add(new PolicyCheckResourceNode(resource5, null));
        PolicyCheckResourceNode grandson = new PolicyCheckResourceNode(resource6, new RequestPolicyInfo("policy-2"));
        node.getChildren().add(grandson);
        grandson.getChildren().add(new PolicyCheckResourceNode(resource7, null));
        grandson.getChildren().add(new PolicyCheckResourceNode(resource8, null));


        root = new PolicyCheckResourceNode();
        newProjects.put("newProject - 0.0.1", root);
        node = new PolicyCheckResourceNode(resource11, null);
        root.getChildren().add(node);
        node = new PolicyCheckResourceNode(resource12, null);
        root.getChildren().add(node);

        server.register("/agent", createHandler(response));

        Collection<AgentProjectInfo> projects = new ArrayList<AgentProjectInfo>();

        AgentProjectInfo info = new AgentProjectInfo();
        info.setCoordinates(new Coordinates("org.whitesource", "exitingProject", "1.0"));
        info.setDependencies(Arrays.asList(
                new DependencyInfo("groupId-1", "artifactId-1", "version-1"),
                new DependencyInfo("groupId-2", "artifactId-2", "version-2"),
                new DependencyInfo("groupId-3", "artifactId-3", "version-3")));
        projects.add(info);

        info = new AgentProjectInfo();
        info.setCoordinates(new Coordinates("org.whitesource", "newProject", "0.0.1-SNAPSHOT"));
        info.setDependencies(Arrays.asList(
                new DependencyInfo("groupId-11", "artifactId-11", "version-11"),
                new DependencyInfo("groupId-12", "artifactId-12", "version-12")));
        projects.add(info);

        CheckPoliciesRequest request = requestFactory.newCheckPoliciesRequest("orgToken", projects);

        CheckPoliciesResult result = null;
        try {
            result = client.checkPolicies(request);
        } catch (WssServiceException e) {
            log.error("Error updating inventory", e);
            fail("Unable to update inventory");
        }

        assertNotNull(result);
        assertEquals("WhiteSource", result.getOrganization());
        assertThat(result.getNewProjects().keySet(), hasItems("newProject - 0.0.1"));
        assertThat(result.getExistingProjects().keySet(), hasItems("exitingProject - 1.0"));
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
        thrown.expectMessage("Forbidden");

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
                Gson gson = new Gson();
                String data = gson.toJson(result);
                ResultEnvelope envelope = new ResultEnvelope(status, message, data);
                String json = gson.toJson(envelope);
                HttpEntity entity = new StringEntity(json, "utf8");
                response.setEntity(entity);

                response.setStatusCode(HttpStatus.SC_OK);
            }
        };
    }

}