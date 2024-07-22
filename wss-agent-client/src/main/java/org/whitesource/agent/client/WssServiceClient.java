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

import org.apache.http.client.HttpClient;
import org.whitesource.agent.api.dispatch.*;

import java.util.Map;

/**
 * The interface describes the functionality to be exposed by a client implementation to the White Source agent service.
 *
 * @author Edo.Shor
 * @author Tom.Shapira
 */
public interface WssServiceClient {

    /**
     * The method calls the White Source service for inventory update.
     *
     * @param request Inventory update request.
     * @return Inventory update result.
     * @throws WssServiceException In case an error occurred during the call to WhiteSource server.
     */
    UpdateInventoryResult updateInventory(UpdateInventoryRequest request) throws WssServiceException;

    /**
     * The method call the White Source service for checking policies application.
     *
     * @param request Check Policies request.
     * @return Check Policies result.
     * @throws WssServiceException In case an error occurred during the call to WhiteSource server.
     * @deprecated Use {@link WssServiceClient#checkPolicyCompliance(CheckPolicyComplianceRequest)}
     */
    CheckPoliciesResult checkPolicies(CheckPoliciesRequest request) throws WssServiceException;

    /**
     * The method call the White Source service for checking policies application.
     *
     * @param request Check Policies request.
     * @return Check Policies result.
     * @throws WssServiceException In case an error occurred during the call to WhiteSource server.
     */
    CheckPolicyComplianceResult checkPolicyCompliance(CheckPolicyComplianceRequest request) throws WssServiceException;

    AsyncCheckPolicyComplianceResult asyncCheckPolicyCompliance(AsyncCheckPolicyComplianceRequest request) throws WssServiceException;

    AsyncCheckPolicyComplianceStatusResult asyncCheckPolicyComplianceStatus(AsyncCheckPolicyComplianceStatusRequest request) throws WssServiceException;

    AsyncCheckPolicyComplianceResponseResult asyncCheckPolicyComplianceResponse(AsyncCheckPolicyComplianceResponseRequest request) throws WssServiceException;

    /**
     * The method call the White Source service for getting additional dependency data.
     *
     * @param request Get Dependency Data request.
     * @return Get Dependency Data result.
     * @throws WssServiceException In case an error occurred during the call to WhiteSource server.
     */
    GetDependencyDataResult getDependencyData(GetDependencyDataRequest request) throws WssServiceException;

    /**
     * The method close all connections and release resources.
     * No communication can be done on this client after the method is invoked.
     */

    /**
     * The method calls the White Source service for inventory update.
     *
     * @param request Inventory update request.
     * @return Inventory update result.
     * @throws WssServiceException In case an error occurred during the call to WhiteSource server.
     */
    SummaryScanResult summaryScan(SummaryScanRequest request) throws WssServiceException;

    /**
     * The method calls the White Source service for check vulnerabilities.
     *
     * @param request Check vulnerabilities request.
     * @return Vulnerabilities report result.
     * @throws WssServiceException In case an error occurred during the call to WhiteSource server.
     */
    CheckVulnerabilitiesResult checkVulnerabilities(CheckVulnerabilitiesRequest request) throws WssServiceException;

    /**
     * The method calls the White Source service for check vulnerabilities.
     *
     * @param request plugin configuration request.
     * @return ConfigurationResult user configuration on the server.
     * @throws WssServiceException In case an error occurred during the call to WhiteSource server.
     */
    ConfigurationResult getConfiguration(ConfigurationRequest request) throws WssServiceException;


    SendMetricsResult sendMetrics(SendMetricsRequest request) throws WssServiceException;

    /**
     * @return serviceUrl
     */
    public String getServiceUrl();

    /**
     * @return HttpClient
     */
    public HttpClient getHttpClient();

    /**
     * @return ConnectionTimeout
     */
    /**
     *
     * @return ConnectionTimeout in ms
     */
    public int getConnectionTimeout();

    /**
     * @return isProxy
     */
    /**
     *
     * @return ConnectionTimeout in minutes
     */
    public int getConnectionTimeoutMinutes();

  /**
   *
   * @return isProxy
   */
    public boolean isProxy();

    /**
     * @return ProxyHost
     */
    public String getProxyHost();

    /**
     * @return ProxyPort
     */
    public int getProxyPort();

    /**
     * @return ProxyUsername
     */
    public String getProxyUsername();

    /**
     * @return ProxyPassword
     */
    public String getProxyPassword();

    /**
     * @return IgnoreCertificateCheck
     */
    boolean getIgnoreCertificateCheck();

    void shutdown();


    /**
     * The method configure the client to use a proxy specified by the given parameters.
     *
     * @param host     Proxy host address.
     * @param port     Proxy port.
     * @param username Optional. Proxy username.
     * @param password Optional. Proxy password.
     */
    void setProxy(String host, int port, String username, String password);

    /**
     * The method adjust the connection timeout limit to White Source servers.
     *
     * @param timeout In milliseconds.
     */
    void setConnectionTimeout(int timeout);

    /**
     * The method configures custom headers to be appended to each http request
     *
     * @param headers the custom headers
     */
    void setHeaders(Map<String, String> headers);

    /**
     * @return the customer headers configured for this client
     */
    Map<String, String> getHeaders();

    JwtAccessTokenResult jwtAccessToken(JwtAccessTokenRequest request) throws WssServiceException;
}
