package com.wss.agent.maven.plugin.update.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
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

import com.wss.agent.api.PropertiesRequest;
import com.wss.agent.api.PropertiesResult;
import com.wss.agent.api.RequestType;
import com.wss.agent.api.ResultEnvelope;
import com.wss.agent.api.ServiceRequest;
import com.wss.agent.api.UpdateInventoryRequest;
import com.wss.agent.api.UpdateInventoryResult;
import com.wss.agent.constants.AgentConstants;
import com.wss.agent.exception.JsonParsingException;
import com.wss.agent.maven.plugin.update.Constants;
import com.wss.agent.utils.JsonUtils;

/**
 * Implementation class for {@link UpdateService}
 * 
 * @author tom.shapira
 */
public class UpdateServiceImpl implements UpdateService {

	/* --- Members --- */

	private Log log;

	private static UpdateService INSTANCE = new UpdateServiceImpl();

	/* --- Singleton --- */
	
	private UpdateServiceImpl() {
		
	}

	public static UpdateService getInstance() {
		return INSTANCE;
	}

	/* --- Concrete implementation methods --- */

	public PropertiesResult getProperties(PropertiesRequest request) throws MojoExecutionException {
		return service(request);
	}

	public UpdateInventoryResult updateInventory(UpdateInventoryRequest request) throws MojoExecutionException {
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

			// handle response
			logDebug("response = " + response);
			if (response == null || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				logDebug(response.getStatusLine().getReasonPhrase());
				throw new MojoExecutionException(Constants.ERROR_HTTP);
			}
			else {
				String data = getResultData(response);
				
				switch (request.getType()) {
				case PROPERTIES: result = (R) PropertiesResult.fromJSON(data); break;
				case UPDATE: result = (R) UpdateInventoryResult.fromJSON(data); break;
				default: throw new IllegalStateException("Unsupporeted request type.");
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}  catch (JsonParsingException e) {
			throw new MojoExecutionException(Constants.ERROR_JSON_PARSING, e);
		} catch (IllegalStateException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (IOException e) {
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
		
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_AGENT_VERSION, Constants.AGENT_VERSION));
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_REQUEST_TYPE, request.getType().toString()));
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_TOKEN, request.getOrgToken()));
		
		if (RequestType.UPDATE.equals(request.getType())) {
			nvps.add(new BasicNameValuePair(AgentConstants.PARAM_TIME_STAMP, String.valueOf(request.getTimeStamp())));
			nvps.add(new BasicNameValuePair(AgentConstants.PARAM_DIFF, JsonUtils.toJson(((UpdateInventoryRequest) request).getProjects())));
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
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private String getResultData(HttpResponse response) throws MojoExecutionException, JsonParsingException, IllegalStateException, IOException {
		String data = null;

		String json = readResponse(response);

		ResultEnvelope envelope = ResultEnvelope.fromJSON(json);
		int status = envelope.getStatus();
		String message = envelope.getMessage();
		data = envelope.getData();

		if (status == ResultEnvelope.STATUS_BAD_REQUEST) {
			throw new MojoExecutionException(message + ": " + data);
		} else if (status == ResultEnvelope.STATUS_SERVER_ERROR) {
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
	 * @throws IllegalStateException 
	 */
	private String readResponse(HttpResponse httpResponse) throws IllegalStateException, IOException {
		return IOUtil.toString(httpResponse.getEntity().getContent());
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
