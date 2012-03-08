package com.wss.agent.maven.plugin.update.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpResponse;
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

import com.wss.agent.constants.AgentConstants;
import com.wss.agent.exception.JsonParsingException;
import com.wss.agent.maven.plugin.update.Constants;
import com.wss.agent.model.AgentProjectInfo;
import com.wss.agent.request.PropertiesRequest;
import com.wss.agent.request.PropertiesResult;
import com.wss.agent.request.RequestType;
import com.wss.agent.request.UpdateInventoryRequest;
import com.wss.agent.request.UpdateInventoryResult;
import com.wss.agent.utils.JsonUtils;

/**
 * Implementation class for {@link UpdateService}
 * 
 * @author tom.shapira
 *
 */
public class UpdateServiceImpl implements UpdateService {

	/* --- Static methods --- */
	
	private static final String FROM_SERVER = "From server: ";
	
	/* --- Members --- */
	
	private Log log;
	
	/* --- Concrete implementation methods --- */
	
	public PropertiesResult getProperties(PropertiesRequest request) throws MojoExecutionException {
		PropertiesResult result = null;
		try {
			result = servicePoperties(request);
		} catch (UnsupportedEncodingException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		return result;
	}

	public UpdateInventoryResult updateInventory(UpdateInventoryRequest request) throws MojoExecutionException {
		UpdateInventoryResult result = null;
		try {
			result = serviceUpdate(request);
		} catch (UnsupportedEncodingException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		return result;
	}
	
	/* --- Private methods --- */
	
	private PropertiesResult servicePoperties(PropertiesRequest request) throws UnsupportedEncodingException, MojoExecutionException {
		PropertiesResult result = null;
		
		// create http request
		HttpPost httpPost = new HttpPost(Constants.SERVICE_ENDPOINT_URL);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_REQUEST_TYPE, RequestType.PROPERTIES.toString()));
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_TOKEN, request.getToken()));
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_AGENT_VERSION, Constants.AGENT_VERSION));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		
		// actual call to the service
		HttpResponse response = sendRequest(httpPost);
		
		// handle response
		if (response != null) {
			try {
				String json = readResponse(response);
				result = PropertiesResult.fromJSON(json);
				
				logDebug(FROM_SERVER + json);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Send HTTP request and read the response.
	 * 
	 * @return 
	 * 
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	private UpdateInventoryResult serviceUpdate(UpdateInventoryRequest request) 
			throws MojoExecutionException, UnsupportedEncodingException {
		UpdateInventoryResult result = null;
		
		// create http request
		HttpPost httpPost = new HttpPost(Constants.SERVICE_ENDPOINT_URL);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_REQUEST_TYPE, RequestType.UPDATE.toString()));
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_TOKEN, request.getToken()));
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_TIME_STAMP, String.valueOf(request.getTimeStamp())));
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_AGENT_VERSION, Constants.AGENT_VERSION));
		nvps.add(new BasicNameValuePair(AgentConstants.PARAM_DIFF, toJson(request.getProjects())));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		// actual call to the service
		HttpResponse response = sendRequest(httpPost);
		
		// handle response
		if (response != null) {
			String resultString;
			try {
				resultString = readResponse(response);
				result = new UpdateInventoryResult(resultString);
				
				logDebug(FROM_SERVER + resultString);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
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
	 * Reads the response.
	 * 
	 * @param response
	 * @return
	 * 
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private String readResponse(HttpResponse response) throws IllegalStateException, IOException {
		StringBuffer wssResponse = new StringBuffer();
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		
		String line = "";
		while ((line = rd.readLine()) != null) {
			wssResponse.append(line);
		}
		return wssResponse.toString();
	}
	
	/**
	 * Converts the collection to JSON.
	 * 
	 * @return The collection represented in JSON.
	 * 
	 * @throws MojoExecutionException In case there was a JSON parsing exception.
	 */
	private String toJson(Collection<AgentProjectInfo> projectInfos) throws MojoExecutionException {
		String json = null;
		
		try {
			json = JsonUtils.toJson(projectInfos);
		} catch (JsonParsingException e) {
			throw new MojoExecutionException(Constants.ERROR_JSON_PARSING);
		}
		
		return json;
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
