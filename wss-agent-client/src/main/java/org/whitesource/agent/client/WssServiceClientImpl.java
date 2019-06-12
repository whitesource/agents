/**
 * Copyright (C) 2012 White Source Ltd.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whitesource.agent.client;

import com.btr.proxy.search.ProxySearch;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.whitesource.agent.api.APIConstants;
import org.whitesource.agent.api.dispatch.*;
import org.whitesource.agent.utils.ZipUtils;

import java.io.IOException;
import java.net.*;
import java.security.KeyStore;
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

    private static final String HTTP_PROXY_USER = "http.proxyUser";
    private static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";
    private static final int TO_MILLISECONDS = 60 * 1000;
    private static final String UTF_8 = "UTF-8";

    private static final Log logger = LogFactory.getLog(WssServiceClientImpl.class);
    private static final String TLS = "TLS";
    public static final String SOME_PASSWORD = "some password";

    /* --- Members --- */

    protected String serviceUrl;
    protected CloseableHttpClient httpClient;
    protected Gson gson;
    protected int connectionTimeout;

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
        this(serviceUrl, true);
    }

    /**
     * Constructor
     *
     * @param serviceUrl WhiteSource service URL to use.
     * @param setProxy   WhiteSource set proxy, whether the proxy settings defined or not.
     */
    public WssServiceClientImpl(String serviceUrl, boolean setProxy) {
        this(serviceUrl, setProxy, ClientConstants.DEFAULT_CONNECTION_TIMEOUT_MINUTES);
    }

    public WssServiceClientImpl(String serviceUrl, boolean setProxy, int connectionTimeoutMinutes) {
        this(serviceUrl, setProxy, connectionTimeoutMinutes, false);
    }

    /**
     * Constructor
     *
     * @param serviceUrl               WhiteSource service URL to use.
     * @param setProxy                 WhiteSource set proxy, whether the proxy settings is defined or not.
     * @param connectionTimeoutMinutes WhiteSource connection timeout, whether the connection timeout is defined or not (default to 60 minutes).
     */
    public WssServiceClientImpl(String serviceUrl, boolean setProxy, int connectionTimeoutMinutes, boolean ignoreCertificateCheck) {
        gson = new Gson();

        if (serviceUrl == null || serviceUrl.length() == 0) {
            this.serviceUrl = ClientConstants.DEFAULT_SERVICE_URL;
        } else {
            this.serviceUrl = serviceUrl;
        }

        if (connectionTimeoutMinutes <= 0) {
            this.connectionTimeout = ClientConstants.DEFAULT_CONNECTION_TIMEOUT_MINUTES * TO_MILLISECONDS;
        } else {
            this.connectionTimeout = connectionTimeoutMinutes * TO_MILLISECONDS;
        }

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, this.connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, this.connectionTimeout);
        HttpClientParams.setRedirecting(params, true);

        httpClient = new DefaultHttpClient();

        if (ignoreCertificateCheck) {
            try {
                logger.warn("Security Warning - Trusting all certificates");

                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                char[] password = SOME_PASSWORD.toCharArray();
                keystore.load(null, password);

                WssSSLSocketFactory sf = new WssSSLSocketFactory(keystore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                httpClient = new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        if (setProxy) {
            findDefaultProxy();
        }
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
    public CheckPolicyComplianceResult checkPolicyCompliance(CheckPolicyComplianceRequest request) throws WssServiceException {
        return service(request);
    }

    @Override
    public GetDependencyDataResult getDependencyData(GetDependencyDataRequest request) throws WssServiceException {
        return service(request);
    }

    @Override
    public SummaryScanResult summaryScan(SummaryScanRequest request) throws WssServiceException {
        return service(request);
    }

    @Override
    public CheckVulnerabilitiesResult checkVulnerabilities(CheckVulnerabilitiesRequest request) throws WssServiceException {
        return service(request);
    }

    @Override
    public ConfigurationResult getConfiguration(ConfigurationRequest request) throws WssServiceException {
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
        //		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();
        logger.info("Using proxy: " + proxy.toHostString());

        if (username != null && username.trim().length() > 0) {
            logger.info("Proxy username: " + username);
            Credentials credentials;
            if (username.indexOf('/') >= 0) {
                credentials = new NTCredentials(username + ":" + password);
            } else if (username.indexOf('\\') >= 0) {
                username = username.replace('\\', '/');
                credentials = new NTCredentials(username + ":" + password);
            } else {
                credentials = new UsernamePasswordCredentials(username, password);
            }
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(AuthScope.ANY, credentials);
            // TODO check
            httpClient = HttpClientBuilder.create().setProxy(proxy).setDefaultCredentialsProvider(credsProvider).build();

            //            httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
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
     * @return Result from WhiteSource service.
     * @throws WssServiceException In case of errors while serving the request.
     */
    @SuppressWarnings("unchecked")
    protected <R> R service(ServiceRequest<R> request) throws WssServiceException {
        R result;
        String response = "";
        try {
            HttpRequestBase httpRequest = createHttpRequest(request);
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
            httpRequest.setConfig(requestConfig);

            logger.trace("Calling White Source service: " + request);
            response = httpClient.execute(httpRequest, new BasicResponseHandler());

            String data = extractResultData(response);
            logger.trace("Result data is: " + data);

            switch (request.type()) {
                case UPDATE:
                    result = (R) gson.fromJson(data, UpdateInventoryResult.class);
                    break;
                case CHECK_POLICIES:
                    result = (R) gson.fromJson(data, CheckPoliciesResult.class);
                    break;
                case CHECK_POLICY_COMPLIANCE:
                    result = (R) gson.fromJson(data, CheckPolicyComplianceResult.class);
                    break;
                case CHECK_VULNERABILITIES:
                    result = (R) gson.fromJson(data, CheckVulnerabilitiesResult.class);
                    break;
                case GET_DEPENDENCY_DATA:
                    result = (R) gson.fromJson(data, GetDependencyDataResult.class);
                    break;
                case SUMMARY_SCAN:
                    result = (R) gson.fromJson(data, SummaryScanResult.class);
                    break;
                case GET_CONFIGURATION:
                    result = (R) gson.fromJson(data, ConfigurationResult.class);
                    break;
                default:
                    throw new IllegalStateException("Unsupported request type.");
            }
        } catch (JsonSyntaxException e) {
            throw new WssServiceException("JsonSyntax exception. Response data is:  " + response + e.getMessage(), e);
        } catch (IOException e) {
            throw new WssServiceException("Unexpected error. Response data is: " + response + e.getMessage(), e);
        }

        return result;
    }

    /**
     * The method create the HTTP post request to be sent to the remote service.
     *
     * @param request Request to service.
     * @return Newly created HTTP post request.
     * @throws IOException In case of error creating the request.
     */
    protected <R> HttpRequestBase createHttpRequest(ServiceRequest<R> request) throws IOException, WssServiceException {
        HttpPost httpRequest = new HttpPost(serviceUrl);
        httpRequest.setHeader("Accept", ClientConstants.APPLICATION_JSON);

        RequestType requestType = request.type();
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(APIConstants.PARAM_REQUEST_TYPE, requestType.toString()));
        nvps.add(new BasicNameValuePair(APIConstants.PARAM_AGENT, request.agent()));
        nvps.add(new BasicNameValuePair(APIConstants.PARAM_AGENT_VERSION, request.agentVersion()));
        nvps.add(new BasicNameValuePair(APIConstants.PARAM_TOKEN, request.orgToken()));
        nvps.add(new BasicNameValuePair(APIConstants.USER_KEY, request.userKey()));
        nvps.add(new BasicNameValuePair(APIConstants.PARAM_REQUESTER_EMAIL, request.requesterEmail()));
        nvps.add(new BasicNameValuePair(APIConstants.PARAM_PRODUCT, request.product()));
        nvps.add(new BasicNameValuePair(APIConstants.PARAM_PRODUCT_VERSION, request.productVersion()));
        nvps.add(new BasicNameValuePair(APIConstants.PARAM_TIME_STAMP, String.valueOf(request.timeStamp())));
        nvps.add(new BasicNameValuePair(APIConstants.PARAM_PLUGIN_VERSION, String.valueOf(request.pluginVersion())));
        nvps.add(new BasicNameValuePair(APIConstants.AGGREGATE_MODULES, String.valueOf(request.aggregateModules())));
        nvps.add(new BasicNameValuePair(APIConstants.PRESERVE_MODULE_STRUCTURE, String.valueOf(request.preserveModuleStructure())));
        nvps.add(new BasicNameValuePair(APIConstants.AGGREGATE_PROJECT_NAME, request.aggregateProjectName()));
        nvps.add(new BasicNameValuePair(APIConstants.AGGREGATE_PROJECT_TOKEN, request.aggregateProjectToken()));
        nvps.add(new BasicNameValuePair(APIConstants.LOG_DATA, request.logData()));
        nvps.add(new BasicNameValuePair(APIConstants.SCAN_COMMENT, request.scanComment()));
        nvps.add(new BasicNameValuePair(APIConstants.PRODUCT_TOKEN, request.productToken()));

        if (request.extraProperties() != null) {
            String strExtraProperties = gson.toJson(request.extraProperties());
            nvps.add(new BasicNameValuePair(APIConstants.EXTRA_PROPERTIES, strExtraProperties));
        } else {
            nvps.add(new BasicNameValuePair(APIConstants.EXTRA_PROPERTIES, "{}"));
        }

        String jsonDiff = null;
        switch (requestType) {
            case UPDATE:
                UpdateInventoryRequest updateInventoryRequest = (UpdateInventoryRequest) request;
                nvps.add(new BasicNameValuePair(APIConstants.SCAN_SUMMARY_INFO, gson.toJson(updateInventoryRequest.getScanSummaryInfo())));
                nvps.add(new BasicNameValuePair(APIConstants.PARAM_UPDATE_TYPE, updateInventoryRequest.getUpdateType().toString()));
                jsonDiff = gson.toJson(updateInventoryRequest.getProjects());
                break;
            case CHECK_POLICIES:
                jsonDiff = gson.toJson(((CheckPoliciesRequest) request).getProjects());
                break;
            case CHECK_POLICY_COMPLIANCE:
                CheckPolicyComplianceRequest checkPolicyComplianceRequest = (CheckPolicyComplianceRequest) request;
                nvps.add(new BasicNameValuePair(APIConstants.SCAN_SUMMARY_INFO, this.gson.toJson(checkPolicyComplianceRequest.getScanSummaryInfo())));
                nvps.add(new BasicNameValuePair(APIConstants.PARAM_FORCE_CHECK_ALL_DEPENDENCIES,
                        String.valueOf(checkPolicyComplianceRequest.isForceCheckAllDependencies())));
                jsonDiff = gson.toJson(checkPolicyComplianceRequest.getProjects());
                break;
            case CHECK_VULNERABILITIES:
                jsonDiff = gson.toJson(((CheckVulnerabilitiesRequest) request).getProjects());
                break;
            case GET_DEPENDENCY_DATA:
                jsonDiff = gson.toJson(((GetDependencyDataRequest) request).getProjects());
                break;
            case SUMMARY_SCAN:
                SummaryScanRequest summaryScanRequest = (SummaryScanRequest) request;
                jsonDiff = gson.toJson(summaryScanRequest.getProjects());
                break;
            case GET_CONFIGURATION:
                jsonDiff = gson.toJson(((ConfigurationRequest) request).getProjects());
                break;
            default:
                break;
        }

        // compress json before sending
        String compressedString = ZipUtils.compressString(jsonDiff);
        nvps.add(new BasicNameValuePair(APIConstants.PARAM_DIFF, compressedString));

        httpRequest.setEntity(new UrlEncodedFormEntity(nvps, UTF_8));

        // whitesource service http request size should be below 200 MB
        String httpRequestJson = gson.toJson(httpRequest);
        //        if(httpRequestJson.getBytes().length > APIConstants.MAX_POST_SIZE) {
        //            logger.error("WhiteSource service http request size have exceeded max limit " + APIConstants.MAX_POST_SIZE + " bytes");
        //            throw new WssServiceException("WhiteSource service http request size have exceeded max limit " + APIConstants.MAX_POST_SIZE + " bytes");
        //        }

        return httpRequest;
    }

    /**
     * The method extract the data from the given {@link org.whitesource.agent.api.dispatch.ResultEnvelope}.
     *
     * @param response HTTP response as string.
     * @return String with logical result in JSON format.
     * @throws IOException
     * @throws WssServiceException
     */
    protected String extractResultData(String response) throws IOException, WssServiceException {
        // parse response
        ResultEnvelope envelope = gson.fromJson(response, ResultEnvelope.class);
        if (envelope == null) {
            throw new WssServiceException("Empty response, response data is: " + response);
        }

        // extract info from envelope
        String message = envelope.getMessage();
        String data = envelope.getData();
        String requestToken = envelope.getRequestToken();

        // service fault ?
        if (ResultEnvelope.STATUS_SUCCESS != envelope.getStatus()) {
            throw new WssServiceException(message + ": " + data, requestToken);
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

    public int getConnectionTimeout() {
        return connectionTimeout;
    }
}