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
package org.whitesource.agent.client;

import com.btr.proxy.search.ProxySearch;
import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.whitesource.agent.api.APIConstants;
import org.whitesource.agent.api.dispatch.*;

import java.io.IOException;
import java.net.*;
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

    public static final String HTTP_PROXY_USER = "http.proxyUser";
    public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";

    private static final Log logger = LogFactory.getLog(WssServiceClientImpl.class);

	/* --- Members --- */

	protected String serviceUrl;
	
	protected DefaultHttpClient httpClient;

    protected Gson gson;
	
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
        gson = new Gson();

        if (serviceUrl == null || serviceUrl.length() == 0) {
            this.serviceUrl = ClientConstants.DEFAULT_SERVICE_URL;
        } else {
		    this.serviceUrl = serviceUrl;
        }

		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_CONNECTION_TIMEOUT);
        HttpClientParams.setRedirecting(params, true);
		httpClient = new DefaultHttpClient(params);
        findDefaultProxy();
	}

	/* --- Interface implementation methods --- */

	@Override
	public UpdateInventoryResult updateInventory(UpdateInventoryRequest request) throws WssServiceException {
		return service(request);
	}

    @Override
    public CheckPoliciesResult checkPolicies(CheckPoliciesRequest request) throws WssServiceException {
        return service(request);
    }

    @Override
    public GetInHouseRulesResult getInHouseRules(GetInHouseRulesRequest request) throws WssServiceException {
        return service(request);
    }

    @Override
	public void shutdown() {
		httpClient.getConnectionManager().shutdown();	
	}

	@Override
	public void setProxy(String host, int port, String username, String password) {
        if (host == null || host.trim().length() == 0) {
            return;
        }
        if (port < 0 || port > 65535) {
            return;
        }

		HttpHost proxy = new HttpHost(host, port);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        logger.info("Using proxy: " + proxy.toHostString());

        if (username != null && username.trim().length() > 0) {
            logger.info("Proxy username: " + username);
            Credentials credentials;
            if (username.indexOf('/') >= 0) {
                credentials = new NTCredentials(username + ":" + password);
            } else if (username.indexOf('\\') >= 0) {
                username = username.replace('\\','/');
                credentials = new NTCredentials(username + ":" + password);
            } else {
                credentials = new UsernamePasswordCredentials(username, password);
            }
            httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
        }
	}

    @Override
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
		R result;
		
		try {
			HttpRequestBase httpRequest = createHttpRequest(request);
			
			logger.trace("Calling White Source service: " +  request);
            String response = httpClient.execute(httpRequest, new BasicResponseHandler());

            String data = extractResultData(response);
            logger.trace("Result data is: " + data);

            switch (request.type()) {
            case UPDATE:
                result = (R) gson.fromJson(data, UpdateInventoryResult.class);
                break;
            case CHECK_POLICIES:
                result = (R) gson.fromJson(data, CheckPoliciesResult.class);
                break;
            case GET_IN_HOUSE_RULES:
                result = (R) gson.fromJson(data, GetInHouseRulesResult.class);
                break;
            default:
                throw new IllegalStateException("Unsupported request type.");
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
		nvps.add(new BasicNameValuePair(APIConstants.PARAM_PRODUCT, request.product()));
		nvps.add(new BasicNameValuePair(APIConstants.PARAM_PRODUCT_VERSION, request.productVersion()));

		switch (type) {
		case UPDATE:
			nvps.add(new BasicNameValuePair(APIConstants.PARAM_TIME_STAMP, String.valueOf(request.timeStamp())));
			nvps.add(new BasicNameValuePair(APIConstants.PARAM_DIFF, gson.toJson(((UpdateInventoryRequest) request).getProjects())));
			break;
        case CHECK_POLICIES:
            nvps.add(new BasicNameValuePair(APIConstants.PARAM_TIME_STAMP, String.valueOf(request.timeStamp())));
            nvps.add(new BasicNameValuePair(APIConstants.PARAM_DIFF, gson.toJson(((CheckPoliciesRequest) request).getProjects())));
            break;
        case GET_IN_HOUSE_RULES:
            nvps.add(new BasicNameValuePair(APIConstants.PARAM_TIME_STAMP, String.valueOf(request.timeStamp())));
            break;
		default:
			break;
		}
		
		httpRequest.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		
		return httpRequest;
	}

	/**
	 * The method extract the data from the given {@link org.whitesource.agent.api.dispatch.ResultEnvelope}.
	 * 
	 * @param response HTTP response as string.
	 * 
	 * @return String with logical result in JSON format.
	 * 
	 * @throws IOException
	 * @throws WssServiceException 
	 */
    protected String extractResultData(String response) throws IOException, WssServiceException {
        // parse response
		ResultEnvelope envelope = gson.fromJson(response, ResultEnvelope.class);
        if (envelope == null) {
            throw new WssServiceException("Empty response");
        }
		
		// extract info from envelope
		String message = envelope.getMessage();
		String data = envelope.getData();

		// service fault ?
		if (ResultEnvelope.STATUS_SUCCESS != envelope.getStatus()) {
			throw new WssServiceException(message + ": " + data);
		}
		
		return data;
	}

    /* --- Private methods --- */

    private void findDefaultProxy() {
        ProxySearch proxySearch = new ProxySearch();
        proxySearch.addStrategy(ProxySearch.Strategy.JAVA);
        proxySearch.addStrategy(ProxySearch.Strategy.ENV_VAR);
        proxySearch.addStrategy(ProxySearch.Strategy.OS_DEFAULT);
        proxySearch.addStrategy(ProxySearch.Strategy.BROWSER);
        ProxySelector proxySelector = proxySearch.getProxySelector();

        if (proxySelector != null) {
            ProxySelector.setDefault(proxySelector);
            try {
                List<Proxy> proxyList = proxySelector.select(new URI(serviceUrl));
                if (proxyList != null && !proxyList.isEmpty()) {
                    for (Proxy proxy : proxyList) {
                        InetSocketAddress address = (InetSocketAddress) proxy.address();
                        if (address != null) {
                            String host = address.getHostName();
                            int port = address.getPort();
                            String username = System.getProperty(HTTP_PROXY_USER);
                            String password = System.getProperty(HTTP_PROXY_PASSWORD);
                            setProxy(host, port, username, password);
                        }
                    }
                }
            } catch (URISyntaxException e) {
                logger.error("Bad service url: " + serviceUrl, e);
            }
        }
    }
	
	/* --- Getters  --- */
		
	public String getServiceUrl() {
		return serviceUrl;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

}