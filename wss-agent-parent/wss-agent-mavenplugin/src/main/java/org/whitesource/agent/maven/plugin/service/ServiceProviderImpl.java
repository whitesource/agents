package org.whitesource.agent.maven.plugin.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.IOUtil;
import org.whitesource.agent.api.APIConstants;
import org.whitesource.agent.api.JsonParsingException;
import org.whitesource.agent.api.JsonUtils;
import org.whitesource.agent.api.dispatch.PropertiesRequest;
import org.whitesource.agent.api.dispatch.PropertiesResult;
import org.whitesource.agent.api.dispatch.ReportRequest;
import org.whitesource.agent.api.dispatch.ReportResult;
import org.whitesource.agent.api.dispatch.RequestType;
import org.whitesource.agent.api.dispatch.ResultEnvelope;
import org.whitesource.agent.api.dispatch.ServiceRequest;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.dispatch.UpdateInventoryResult;
import org.whitesource.agent.maven.plugin.Constants;


/**
 * Implementation class for {@link ServiceProvider}
 * 
 * @author tom.shapira
 */
public final class ServiceProviderImpl implements ServiceProvider {

	/* --- Members --- */

	private Log log;

	private static final ServiceProvider INSTANCE = new ServiceProviderImpl();

	/* --- Singleton --- */
	
	private ServiceProviderImpl() {
		
	}

	public static ServiceProvider getInstance() {
		return INSTANCE;
	}

	/* --- Concrete implementation methods --- */

	public PropertiesResult getProperties(PropertiesRequest request) throws MojoExecutionException {
		return service(request);
	}

	public UpdateInventoryResult updateInventory(UpdateInventoryRequest request) throws MojoExecutionException {
		return service(request);
	}
	
	public ReportResult getReport(ReportRequest request) throws MojoExecutionException {
		return service(request);
	}

	/* --- Private methods --- */
	
	/**
	 * The method service the given request.
	 * 
	 * @param request Request to serve.
	 * 
	 * @return Result from white source service.
	 * 
	 * @throws MojoExecutionException In case of errors while serving the request.
	 */
	@SuppressWarnings("unchecked")
	private <R> R service(ServiceRequest<R> request) throws MojoExecutionException {
		R result = null;
		
		try {
			// create http request
			HttpRequestBase httpRequest = createHttpRequest(request);

			// actual call to the service
			logDebug("sending request for " + request.getType());
			HttpResponse response = sendRequest(httpRequest);

			// verify response
			logDebug("response = " + response);
			verifyResponse(response);
			
			// handle response
			String data = getResultData(response);
			switch (request.getType()) {
			case PROPERTIES: 
				result = (R) PropertiesResult.fromJSON(data); 
				break;
			case UPDATE: 
				result = (R) UpdateInventoryResult.fromJSON(data); 
				break;
			case REPORT: 
				result = (R) ReportResult.fromJSON(data); 
				break;
			default: 
				throw new IllegalStateException("Unsupporeted request type.");
			}
			
		}  catch (JsonParsingException e) {
			throw new MojoExecutionException(Constants.ERROR_JSON_PARSING, e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		
		return result;
	}
	
	/**
	 * The method create the http post request to be sent to the remote service.
	 * 
	 * @param request Request to service.
	 * 
	 * @return Newly created http post request. 
	 * 
	 * @throws UnsupportedEncodingException In case of error encoding request parameters.
	 * @throws JsonParsingException In case of error marshaling data to JSON format.
	 */
	private <R> HttpRequestBase createHttpRequest(ServiceRequest<R> request) 
			throws UnsupportedEncodingException, JsonParsingException {
		HttpPost httpRequest = new HttpPost(Constants.SERVICE_ENDPOINT_URL);
		httpRequest.setHeader("Accept", Constants.APPLICATION_JSON);
		
		RequestType type = request.getType();
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(APIConstants.PARAM_AGENT_VERSION, Constants.AGENT_VERSION));
		nvps.add(new BasicNameValuePair(APIConstants.PARAM_REQUEST_TYPE, type.toString()));
		nvps.add(new BasicNameValuePair(APIConstants.PARAM_TOKEN, request.getOrgToken()));
		
		switch (type) {
		case UPDATE:
			nvps.add(new BasicNameValuePair(APIConstants.PARAM_TIME_STAMP, String.valueOf(request.getTimeStamp())));
			nvps.add(new BasicNameValuePair(APIConstants.PARAM_DIFF, JsonUtils.toJson(((UpdateInventoryRequest) request).getProjects())));
			break;
		case REPORT:
			nvps.add(new BasicNameValuePair(APIConstants.PARAM_TIME_STAMP, String.valueOf(request.getTimeStamp())));
			nvps.add(new BasicNameValuePair(APIConstants.PARAM_DEPENDENCIES, JsonUtils.toJson(((ReportRequest) request).getDependencies())));
			break;
		default:
			break;
		}
		
		httpRequest.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		
		return httpRequest;
	}

	/**
	 * Send HTTP request and return response.
	 * 
	 * @return Response from server
	 */
	private HttpResponse sendRequest(HttpRequestBase httpRequest) throws MojoExecutionException {
		HttpResponse response = null;

		try {
			HttpClient client = new DefaultHttpClient();
			response = client.execute(httpRequest);
		} catch (IOException e) {
			throw new MojoExecutionException(Constants.ERROR_CONNECTION, e);
		}

		return response;
	}

	/**
	 * Read the data from the {@link RequestEnvelope}.
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @throws MojoExecutionException
	 * @throws JsonParsingException
	 * @throws IOException
	 */
	private String getResultData(HttpResponse response) throws MojoExecutionException, JsonParsingException, IOException {
		// read and parse result envelope 
		String json = readResponse(response);
		ResultEnvelope envelope = ResultEnvelope.fromJSON(json);
		
		// extract info from envelope
		String message = envelope.getMessage();
		String data = envelope.getData();

		// fault ?
		if (ResultEnvelope.STATUS_SUCCESS != envelope.getStatus()) {
			throw new MojoExecutionException(message + ": " + data);
		}
		
		return data;
	}

	/**
	 * Reads the response.
	 * 
	 * @param httpResponse
	 * @return
	 * @throws IOException 
	 */
	private String readResponse(HttpResponse httpResponse) throws IOException {
		return IOUtil.toString(httpResponse.getEntity().getContent());
	}
	
	/**
	 * The method verify the given http response.
	 * 
	 * @param response Http response to verify.
	 * 
	 * @throws IllegalArgumentException In case the response is invalid.
	 */
	private void verifyResponse(HttpResponse response) {
		boolean verified = (response != null && response.getStatusLine() != null);
		String logMessage = "";
		
		if (verified) {
			StatusLine statusLine = response.getStatusLine();
			verified = (statusLine.getStatusCode() == HttpStatus.SC_OK);
			logMessage = statusLine.getReasonPhrase();
		} else {
			logMessage = "HttpResponse or statusLine is null";
		}
		
		if (!verified) {
			logDebug(logMessage);
			throw new IllegalArgumentException(Constants.ERROR_HTTP);
		}
	}

	/**
	 * Writes a debug message to the log.
	 * 
	 * @param message Debug message
	 */
	private void logDebug(String message) {
		if (log != null) {
			log.debug(message);
		}
	}

	/* --- Getters / Setters --- */

	public void setLog(Log log) {
		this.log = log;
	}
}