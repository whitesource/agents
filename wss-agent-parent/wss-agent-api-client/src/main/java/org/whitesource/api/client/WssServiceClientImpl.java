package org.whitesource.api.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.whitesource.agent.api.APIConstants;
import org.whitesource.agent.api.JsonUtils;
import org.whitesource.agent.api.dispatch.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Default Implementation of the interface using Apache's HttpClient.
 * 
 * @author tom.shapira
 * @author Edo.Shor
 */
public class WssServiceClientImpl implements WssServiceClient {

	/* --- Static members --- */

	public static final int DEFAULT_CONNECTION_TIMEOUT = 5 * 60 * 1000;
	
	private static final Log logger = LogFactory.getLog(WssServiceClientImpl.class);
	
	/* --- Members --- */
	
	protected String serviceUrl;
	
	protected DefaultHttpClient httpClient;
	
	/* --- Constructors --- */
	
	/**
	 * Default constructor
	 */
	public WssServiceClientImpl() {
		this(ClientConstants.DEFAULT_SERVICE_URL);
	}

	/**
	 * Constructor
	 * 
	 * @param serviceUrl WhiteSource service URL to use.
	 */
	public WssServiceClientImpl(String serviceUrl) {
        if (serviceUrl == null || serviceUrl.length() == 0) {
            this.serviceUrl = ClientConstants.DEFAULT_SERVICE_URL;
        } else {
		    this.serviceUrl = serviceUrl;
        }

		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_CONNECTION_TIMEOUT);
		httpClient = new DefaultHttpClient(params);
	}

	/* --- Interface implementation methods --- */

	@Override
	public PropertiesResult getProperties(PropertiesRequest request) throws WssServiceException {
		return service(request);
	}

	@Override
	public UpdateInventoryResult updateInventory(UpdateInventoryRequest request) throws WssServiceException {
		return service(request);
	}
	
	@Override
	public ReportResult getReport(ReportRequest request) throws WssServiceException {
		return service(request);
	}
	
	@Override
	public void shutdown() {
		httpClient.getConnectionManager().shutdown();	
	}

	@Override
	public void setProxy(String host, int port, String username, String password) {
		HttpHost proxy = new HttpHost(host, port);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("username", "password"));
	}

	public void setConnectionTimeout(int timeout) {
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), timeout);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), timeout);
	}
	
	/* --- Protected methods --- */

	/**
	 * The method service the given request.
	 * 
	 * @param request Request to serve.
	 * 
	 * @return Result from WhiteSource service.
	 * 
	 * @throws WssServiceException In case of errors while serving the request.
	 */
	@SuppressWarnings("unchecked")
	protected <R> R service(ServiceRequest<R> request) throws WssServiceException {
		R result = null;
		
		try {
			HttpRequestBase httpRequest = createHttpRequest(request);
			
			logger.trace("Calling WhiteSource service: " +  request);
			
			HttpResponse response = httpClient.execute(httpRequest);
			StatusLine statusLine = response.getStatusLine();
	        HttpEntity entity = response.getEntity();
	        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
	            EntityUtils.consume(entity);
	            throwServiceException("Error calling WhiteSource:", statusLine);
	        } else {
	            if (entity == null) {
	            	throwServiceException("WhiteSource returned an Empty response:", statusLine);
	            } else {
	            	String data = extractResultData(response);
	            	
	            	logger.trace("Result data is: " + data);
	            	
	            	switch (request.type()) {
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
	            }
	        }
		} catch (IOException e) {
			throw new WssServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	/**
	 * The method create the HTTP post request to be sent to the remote service.
	 * 
	 * @param request Request to service.
	 * 
	 * @return Newly created HTTP post request. 
	 * 
	 * @throws IOException In case of error creating the request.
	 */
	protected <R> HttpRequestBase createHttpRequest(ServiceRequest<R> request)  throws IOException {
		HttpPost httpRequest = new HttpPost(serviceUrl);
		httpRequest.setHeader("Accept", ClientConstants.APPLICATION_JSON);
		
		RequestType type = request.type();
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(APIConstants.PARAM_REQUEST_TYPE, type.toString()));
		nvps.add(new BasicNameValuePair(APIConstants.PARAM_AGENT, request.agent()));
		nvps.add(new BasicNameValuePair(APIConstants.PARAM_AGENT_VERSION, request.agentVersion()));
		nvps.add(new BasicNameValuePair(APIConstants.PARAM_TOKEN, request.orgToken()));
		
		switch (type) {
		case UPDATE:
			nvps.add(new BasicNameValuePair(APIConstants.PARAM_TIME_STAMP, String.valueOf(request.timeStamp())));
			nvps.add(new BasicNameValuePair(APIConstants.PARAM_DIFF, JsonUtils.toJson(((UpdateInventoryRequest) request).getProjects())));
			break;
		case REPORT:
			nvps.add(new BasicNameValuePair(APIConstants.PARAM_TIME_STAMP, String.valueOf(request.timeStamp())));
			nvps.add(new BasicNameValuePair(APIConstants.PARAM_DEPENDENCIES, JsonUtils.toJson(((ReportRequest) request).getDependencies())));
			break;
		default:
			break;
		}
		
		httpRequest.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		
		return httpRequest;
	}

	/**
	 * The method extract the data from the given {@link RequestEnvelope}.
	 * 
	 * @param response Actual HTTP response.
	 * 
	 * @return String with logical result in JSON format.
	 * 
	 * @throws IOException
	 * @throws WssServiceException 
	 */
	protected String extractResultData(HttpResponse response) throws IOException, WssServiceException {
		// read and parse result envelope 
		String contents = EntityUtils.toString(response.getEntity()); 
		ResultEnvelope envelope = ResultEnvelope.fromJSON(contents);
		
		// extract info from envelope
		String message = envelope.getMessage();
		String data = envelope.getData();

		// service fault ?
		if (ResultEnvelope.STATUS_SUCCESS != envelope.getStatus()) {
			throw new WssServiceException(message + ": " + data);
		}
		
		return data;
	}

	/**
	 * The method throws an {@link IOException} using the given message and {@link StatusLine}.
	 * 
	 * @param message
	 * @param statusLine
	 * 
	 * @throws IOException
	 */
	protected void throwServiceException(String message, StatusLine statusLine) throws IOException {
        String error = new StringBuilder(message)
        	.append(" HTTP response code: ").append(statusLine.getStatusCode())
            .append(". HTTP response message: ").append(statusLine.getReasonPhrase())
            .toString();
        
        throw new IOException(error);
    }
	
	/* --- Getters  --- */
		
	public String getServiceUrl() {
		return serviceUrl;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

}