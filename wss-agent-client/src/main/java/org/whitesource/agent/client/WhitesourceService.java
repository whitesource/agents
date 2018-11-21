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

import org.whitesource.agent.api.dispatch.*;
import org.whitesource.agent.api.model.AgentProjectInfo;

import java.util.Collection;
import java.util.Map;

/**
 * A facade to the communication layer with the White Source service.
 *
 * @author Edo.Shor
 */
public class WhitesourceService {

    /* --- Members --- */

    private WssServiceClient client;

    private RequestFactory requestFactory;

    /* --- Constructors --- */

    public WhitesourceService() {
        this("generic", "1.0", "1.0");
    }

    public WhitesourceService(final String agent, final String agentVersion, String pluginVersion) {
        this(agent, agentVersion, pluginVersion, null);
    }

    public WhitesourceService(final String agent, final String agentVersion, String pluginVersion, final String serviceUrl) {
        this(agent, agentVersion, pluginVersion, serviceUrl, true);
    }

    public WhitesourceService(final String agent, final String agentVersion, String pluginVersion, final String serviceUrl, boolean setProxy) {
        this(agent, agentVersion, pluginVersion, serviceUrl, setProxy, ClientConstants.DEFAULT_CONNECTION_TIMEOUT_MINUTES);
    }

    public WhitesourceService(final String agent, final String agentVersion, String pluginVersion, final String serviceUrl, boolean setProxy,
                              int connectionTimeoutMinutes) {
        this(agent, agentVersion, pluginVersion, serviceUrl, setProxy, connectionTimeoutMinutes, false);
    }

    public WhitesourceService(final String agent, final String agentVersion, String pluginVersion, final String serviceUrl, boolean setProxy,
                              int connectionTimeoutMinutes, boolean ignoreCertificateCheck) {
        requestFactory = new RequestFactory(agent, agentVersion, pluginVersion);

        String url = serviceUrl;
        if (url == null || url.trim().length() == 0) {
            url = System.getProperty(ClientConstants.SERVICE_URL_KEYWORD, ClientConstants.DEFAULT_SERVICE_URL);
        }

        if (connectionTimeoutMinutes <= 0) {
            connectionTimeoutMinutes = Integer.parseInt(System.getProperty(ClientConstants.CONNECTION_TIMEOUT_KEYWORD,
                    String.valueOf(ClientConstants.DEFAULT_CONNECTION_TIMEOUT_MINUTES)));
        }
        client = new WssServiceClientImpl(url, setProxy, connectionTimeoutMinutes, ignoreCertificateCheck);
    }

    // backward compatibility methods (plugin version is not a parameter)

    public WhitesourceService(final String agent, final String agentVersion, final String serviceUrl, boolean setProxy) {
        this(agent, agentVersion, serviceUrl, setProxy, ClientConstants.DEFAULT_CONNECTION_TIMEOUT_MINUTES);
    }

    public WhitesourceService(final String agent, final String agentVersion, final String serviceUrl, boolean setProxy,
                              int connectionTimeoutMinutes) {
        this(agent, agentVersion, null, serviceUrl, setProxy, connectionTimeoutMinutes);
    }

    /* --- Public methods --- */

    /**
     * Updates the White Source organization account with the given OSS information.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @return Result of updating white source.
     * @throws WssServiceException In case of errors while updating white source.
     */
    public UpdateInventoryResult update(String orgToken,
                                        String product,
                                        String productVersion,
                                        Collection<AgentProjectInfo> projectInfos,
                                        String userKey)
            throws WssServiceException {
        return update(orgToken, null, product, productVersion, projectInfos, userKey);
    }

    public UpdateInventoryResult update(String orgToken,
                                        String product,
                                        String productVersion,
                                        Collection<AgentProjectInfo> projectInfos)
            throws WssServiceException {
        return update(orgToken, null, product, productVersion, projectInfos, null);
    }

    /**
     * Updates the White Source organization account with the given OSS information.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @return Result of updating white source.
     * @throws WssServiceException
     */
    public UpdateInventoryResult update(String orgToken,
                                        String requesterEmail,
                                        String product,
                                        String productVersion,
                                        Collection<AgentProjectInfo> projectInfos,
                                        String userKey)
            throws WssServiceException {
        return client.updateInventory(
                requestFactory.newUpdateInventoryRequest(orgToken, requesterEmail, product, productVersion, projectInfos, userKey));
    }

    public UpdateInventoryResult update(String orgToken,
                                        String requesterEmail,
                                        String product,
                                        String productVersion,
                                        Collection<AgentProjectInfo> projectInfos,
                                        String userKey,
                                        Map<String, String> extraProperties)
            throws WssServiceException {
        return client.updateInventory(
                requestFactory.newUpdateInventoryRequest(orgToken, requesterEmail, product, productVersion, projectInfos, userKey, extraProperties));
    }

    public UpdateInventoryResult update(String orgToken,
                                        String requesterEmail,
                                        String product,
                                        String productVersion,
                                        Collection<AgentProjectInfo> projectInfos)
            throws WssServiceException {
        return update(orgToken, requesterEmail, product, productVersion, projectInfos, null);
    }

    /**
     * Updates the White Source organization account with the given OSS information.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @param updateType     Request UpdateType
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @return Result of updating white source.
     * @throws WssServiceException
     */
    public UpdateInventoryResult update(String orgToken,
                                        String requesterEmail,
                                        UpdateType updateType,
                                        String product,
                                        String productVersion,
                                        Collection<AgentProjectInfo> projectInfos,
                                        String userKey)
            throws WssServiceException {
        return client.updateInventory(
                requestFactory.newUpdateInventoryRequest(orgToken, updateType, requesterEmail, product, productVersion, projectInfos, userKey, null));
    }

    /**
     * Updates the White Source organization account with the given OSS information.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @param updateType     Request UpdateType
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @param logData        list of FSA's log data events
     * @return Result of updating white source.
     * @throws WssServiceException
     */
    public UpdateInventoryResult update(String orgToken,
                                         String requesterEmail,
                                         UpdateType updateType,
                                         String product,
                                         String productVersion,
                                         Collection<AgentProjectInfo> projectInfos,
                                         String userKey,
                                         String logData)
            throws WssServiceException {
        return client.updateInventory(
                requestFactory.newUpdateInventoryRequest(orgToken, updateType, requesterEmail, product, productVersion, projectInfos, userKey, logData));
    }

    /**
     * Updates the White Source organization account with the given OSS information.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @param updateType     Request UpdateType
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @param logData        list of FSA's log data events
     * @param scanComment    scan description
     * @return Result of updating white source.
     * @throws WssServiceException
     */
    public UpdateInventoryResult update(String orgToken,
                                        String requesterEmail,
                                        UpdateType updateType,
                                        String product,
                                        String productVersion,
                                        Collection<AgentProjectInfo> projectInfos,
                                        String userKey,
                                        String logData,
                                        String scanComment)
            throws WssServiceException {
        return client.updateInventory(
                requestFactory.newUpdateInventoryRequest(orgToken, updateType, requesterEmail, product, productVersion, projectInfos, userKey, logData, scanComment));
    }

    /**
     * Updates the White Source organization account with the given OSS information.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @param updateType     Request UpdateType
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @param logData        list of FSA's log data events
     * @param scanComment    scan description
     * @return Result of updating white source.
     * @throws WssServiceException
     */
    public UpdateInventoryResult update(String orgToken,
                                        String requesterEmail,
                                        UpdateType updateType,
                                        String product,
                                        String productVersion,
                                        Collection<AgentProjectInfo> projectInfos,
                                        String userKey,
                                        String logData,
                                        String scanComment,
                                        String productToken)
            throws WssServiceException {
        return client.updateInventory(
                requestFactory.newUpdateInventoryRequest(orgToken, updateType, requesterEmail, product, productVersion,
                        projectInfos, userKey, logData, scanComment, productToken));
    }


    public UpdateInventoryResult update(String orgToken,
                                        String requesterEmail,
                                        UpdateType updateType,
                                        String product,
                                        String productVersion,
                                        Collection<AgentProjectInfo> projectInfos)
            throws WssServiceException {
        return update(orgToken, requesterEmail, updateType, product, productVersion, projectInfos, null);
    }

    /**
     * Updates the White Source organization account with the given OSS information.
     *
     * @param orgToken                Organization token uniquely identifying the account at white source.
     * @param requesterEmail          Email of the WhiteSource user that requests to update WhiteSource.
     * @param product                 The product name or token to update.
     * @param productVersion          The product version.
     * @param projectInfos            OSS usage information to send to white source.
     * @param userKey                 user key uniquely identifying the account at white source.
     * @param aggregateModules        to combine all pom modules into a single WhiteSource project with an aggregated dependency flat list (no hierarchy).
     * @param preserveModuleStructure combine all pom modules to be dependencies of single project, each module will be represented as a parent of its dependencies.
     * @param aggregateProjectName    aggregate project name identifier.
     * @param aggregateProjectToken   aggregate project token identifier.
     * @return Result of updating white source.
     * @throws WssServiceException
     */
    public UpdateInventoryResult update(String orgToken,
                                        String requesterEmail,
                                        String product,
                                        String productVersion,
                                        Collection<AgentProjectInfo> projectInfos,
                                        String userKey,
                                        boolean aggregateModules,
                                        boolean preserveModuleStructure,
                                        String aggregateProjectName,
                                        String aggregateProjectToken)
            throws WssServiceException {
        return client.updateInventory(
                requestFactory.newUpdateInventoryRequest(orgToken, requesterEmail, product, productVersion, projectInfos, userKey,
                        aggregateModules, preserveModuleStructure, aggregateProjectName, aggregateProjectToken));
    }


    /**
     * Generates a file with json representation of the update request.
     *
     * @param orgToken        Organization token uniquely identifying the account at white source.
     * @param product         The product name or token to update.
     * @param removeBeforeAdd Should remove before add
     * @param productVersion  The product version.
     * @param projectInfos    OSS usage information to send to white source.
     * @param userKey         user key uniquely identifying the account at white source.
     * @param requesterEmail  Email of the WhiteSource user that requests to update WhiteSource.
     * @param scanComment     User comment.
     * @return UpdateInventoryRequest.
     */

    public UpdateInventoryRequest offlineUpdate(String orgToken,
                                                String product,
                                                Boolean removeBeforeAdd,
                                                String productVersion,
                                                Collection<AgentProjectInfo> projectInfos,
                                                String userKey,
                                                String requesterEmail,
                                                String scanComment,
                                                String productToken) {
        return requestFactory.newUpdateInventoryRequest(orgToken, requesterEmail, product, productVersion, projectInfos, userKey, scanComment, productToken);
    }


    public UpdateInventoryRequest offlineUpdate(String orgToken,
                                                String product,
                                                Boolean removeBeforeAdd,
                                                String productVersion,
                                                Collection<AgentProjectInfo> projectInfos,
                                                String userKey,
                                                String requesterEmail,
                                                String scanComment) {
        return offlineUpdate(orgToken, product, removeBeforeAdd, productVersion, projectInfos, userKey, requesterEmail, scanComment, null);
    }


    public UpdateInventoryRequest offlineUpdate(String orgToken,
                                                String product,
                                                Boolean removeBeforeAdd,
                                                String productVersion,
                                                Collection<AgentProjectInfo> projectInfos,
                                                String userKey) {
        return offlineUpdate(orgToken, product, removeBeforeAdd, productVersion, projectInfos, userKey, null, null);
    }

    public UpdateInventoryRequest offlineUpdate(String orgToken,
                                                String product,
                                                Boolean removeBeforeAdd,
                                                String productVersion,
                                                Collection<AgentProjectInfo> projectInfos) {
        return offlineUpdate(orgToken, product, removeBeforeAdd, productVersion, projectInfos, null);
    }

    /**
     * Generates a file with json representation of the update request.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @return UpdateInventoryRequest.
     */
    public UpdateInventoryRequest offlineUpdate(String orgToken,
                                                String product,
                                                String productVersion,
                                                Collection<AgentProjectInfo> projectInfos,
                                                String userKey) {
        // init remove beforeBeforeAdd to false - dummy init
        return offlineUpdate(orgToken, product, false, productVersion, projectInfos, null, null,null);
    }

    public UpdateInventoryRequest offlineUpdate(String orgToken,
                                                String product,
                                                String productVersion,
                                                Collection<AgentProjectInfo> projectInfos,
                                                String userKey,
                                                String scanComment) {
        // init remove beforeBeforeAdd to false - dummy init
        return offlineUpdate(orgToken, product, false, productVersion, projectInfos, null, null, scanComment);
    }

    public UpdateInventoryRequest offlineUpdate(String orgToken,
                                                String product,
                                                String productVersion,
                                                Collection<AgentProjectInfo> projectInfos) {
        return offlineUpdate(orgToken, product, productVersion, projectInfos, null);
    }

    /**
     * Checks the policies application of the given OSS information.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @return Potential result of applying the currently defined policies.
     * @throws WssServiceException In case of errors while checking the policies with white source.
     * @deprecated Use {@link WhitesourceService#checkPolicyCompliance(String, String, String, Collection, boolean, String)}.
     */
    public CheckPoliciesResult checkPolicies(String orgToken,
                                             String product,
                                             String productVersion,
                                             Collection<AgentProjectInfo> projectInfos,
                                             String userKey,
                                             String requesterEmail)
            throws WssServiceException {
        return client.checkPolicies(
                requestFactory.newCheckPoliciesRequest(orgToken, product, productVersion, projectInfos, userKey, requesterEmail));
    }

    public CheckPoliciesResult checkPolicies(String orgToken,
                                             String product,
                                             String productVersion,
                                             Collection<AgentProjectInfo> projectInfos,
                                             String userKey)
            throws WssServiceException {
        return checkPolicies(orgToken, product, productVersion, projectInfos, userKey, null);
    }

    /**
     * Checks the policies application of the given OSS information.
     *
     * @param orgToken                  Organization token uniquely identifying the account at white source.
     * @param product                   The product name or token to update.
     * @param productVersion            The product version.
     * @param projectInfos              OSS usage information to send to white source.
     * @param forceCheckAllDependencies Boolean to check new data only or not.
     * @param userKey                   user key uniquely identifying the account at white source.
     * @param requesterEmail            Email of the WhiteSource user that requests to update WhiteSource.
     * @param logData                   list of FSA's log data events
     * @param productToken               The product token
     * @return Potential result of applying the currently defined policies.
     * @throws WssServiceException In case of errors while checking the policies with white source.
     *
     */

    public CheckPolicyComplianceResult checkPolicyCompliance(String orgToken,
                                                             String product,
                                                             String productVersion,
                                                             Collection<AgentProjectInfo> projectInfos,
                                                             boolean forceCheckAllDependencies,
                                                             String userKey,
                                                             String requesterEmail,
                                                             String logData,
                                                             String productToken)
            throws WssServiceException {
        return client.checkPolicyCompliance(
                requestFactory.newCheckPolicyComplianceRequest(orgToken, product, productVersion, projectInfos, forceCheckAllDependencies,
                        userKey, requesterEmail, logData, productToken));
    }

    public CheckPolicyComplianceResult checkPolicyCompliance(String orgToken,
                                                             String product,
                                                             String productVersion,
                                                             Collection<AgentProjectInfo> projectInfos,
                                                             boolean forceCheckAllDependencies,
                                                             String userKey,
                                                             String requesterEmail,
                                                             String logData) throws WssServiceException {
        return checkPolicyCompliance(orgToken, product, productVersion, projectInfos, forceCheckAllDependencies, userKey, requesterEmail, logData, null);
    }


    public CheckPolicyComplianceResult checkPolicyCompliance(String orgToken,
                                                             String product,
                                                             String productVersion,
                                                             Collection<AgentProjectInfo> projectInfos,
                                                             boolean forceCheckAllDependencies,
                                                             String userKey,
                                                             String requesterEmail)
            throws WssServiceException {
        return checkPolicyCompliance(orgToken, product, productVersion, projectInfos, forceCheckAllDependencies, userKey, requesterEmail, null);

    }

    public CheckPolicyComplianceResult checkPolicyCompliance(String orgToken,
                                                             String product,
                                                             String productVersion,
                                                             Collection<AgentProjectInfo> projectInfos,
                                                             boolean forceCheckAllDependencies,
                                                             String userKey)   throws WssServiceException {
    return checkPolicyCompliance(orgToken, product, productVersion, projectInfos, forceCheckAllDependencies, userKey, null);

}

    public CheckPolicyComplianceResult checkPolicyCompliance(String orgToken,
                                                             String product,
                                                             String productVersion,
                                                             Collection<AgentProjectInfo> projectInfos,
                                                             boolean forceCheckAllDependencies) throws WssServiceException {
        return checkPolicyCompliance(orgToken, product, productVersion, projectInfos, forceCheckAllDependencies, null);
    }

    /**
     * Checks the policies application of the given OSS information.
     *
     * @param orgToken                  Organization token uniquely identifying the account at white source.
     * @param product                   The product name or token to update.
     * @param productVersion            The product version.
     * @param projectInfos              OSS usage information to send to white source.
     * @param forceCheckAllDependencies Boolean to check new data only or not.
     * @param userKey                   user key uniquely identifying the account at white source.
     * @param aggregateModules          to combine all pom modules into a single WhiteSource project with an aggregated dependency flat list (no hierarchy).
     * @param preserveModuleStructure   combine all pom modules to be dependencies of single project, each module will be represented as a parent of its dependencies.
     * @param aggregateProjectName      aggregate project name identifier.
     * @param aggregateProjectToken     aggregate project token identifier.
     * @return Potential result of applying the currently defined policies.
     * @throws WssServiceException In case of errors while checking the policies with white source.
     */
    public CheckPolicyComplianceResult checkPolicyCompliance(String orgToken,
                                                             String product,
                                                             String productVersion,
                                                             Collection<AgentProjectInfo> projectInfos,
                                                             boolean forceCheckAllDependencies,
                                                             String userKey,
                                                             String requesterEmail,
                                                             Boolean aggregateModules,
                                                             Boolean preserveModuleStructure,
                                                             String aggregateProjectName,
                                                             String aggregateProjectToken)
            throws WssServiceException {
        return client.checkPolicyCompliance(
                requestFactory.newCheckPolicyComplianceRequest(orgToken, product, productVersion, projectInfos, forceCheckAllDependencies,
                        userKey, requesterEmail, aggregateModules, preserveModuleStructure, aggregateProjectName, aggregateProjectToken));
    }

    /**
     * Gets additional data for given dependencies.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @return Potential result of the dependencies additional data(license, description, homePageUrl, vulnerabilities, sha1 and displayName).
     * @throws WssServiceException In case of errors while getting additional dependency data with white source.
     */

    public GetDependencyDataResult getDependencyData(String orgToken,
                                                     String product,
                                                     String productVersion,
                                                     Collection<AgentProjectInfo> projectInfos,
                                                     String userKey,
                                                     String requesterEmail,
                                                     String productToken)
            throws WssServiceException {
        return client.getDependencyData(
                requestFactory.newDependencyDataRequest(orgToken, product, productVersion, projectInfos, userKey, requesterEmail, productToken));
    }


    public GetDependencyDataResult getDependencyData(String orgToken,
                                                     String product,
                                                     String productVersion,
                                                     Collection<AgentProjectInfo> projectInfos,
                                                     String userKey,
                                                     String requesterEmail)
            throws WssServiceException {
        return getDependencyData(orgToken, product, productVersion, projectInfos, userKey, requesterEmail, null);
    }

    public GetDependencyDataResult getDependencyData(String orgToken,
                                                     String product,
                                                     String productVersion,
                                                     Collection<AgentProjectInfo> projectInfos,
                                                     String userKey)
            throws WssServiceException {
        return getDependencyData(orgToken, product, productVersion, projectInfos, userKey, null);
    }

    public GetDependencyDataResult getDependencyData(String orgToken,
                                                     String product,
                                                     String productVersion,
                                                     Collection<AgentProjectInfo> projectInfos)
            throws WssServiceException {
        return getDependencyData(orgToken, product, productVersion, projectInfos, null);
    }

    /**
     * Updates the White Source organization account with the given OSS information.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @return Result of updating white source.
     * @throws WssServiceException
     */

    public SummaryScanResult summaryScan(String orgToken,
                                         String product,
                                         String productVersion,
                                         Collection<AgentProjectInfo> projectInfos,
                                         String userKey,
                                         String requesterEmail,
                                         String productToken)
            throws WssServiceException {
        return client.summaryScan(
                requestFactory.newSummaryScanRequest(orgToken, product, productVersion, projectInfos, userKey, requesterEmail, productToken));
    }

    public SummaryScanResult summaryScan(String orgToken,
                                         String product,
                                         String productVersion,
                                         Collection<AgentProjectInfo> projectInfos,
                                         String userKey,
                                         String requesterEmail)
            throws WssServiceException {
        return summaryScan(orgToken, product, productVersion, projectInfos, userKey, requesterEmail, null);
    }

    public SummaryScanResult summaryScan(String orgToken,
                                         String product,
                                         String productVersion,
                                         Collection<AgentProjectInfo> projectInfos,
                                         String userKey)
            throws WssServiceException {
        return summaryScan(orgToken, product, productVersion, projectInfos, userKey, null);
    }

    public SummaryScanResult summaryScan(String orgToken,
                                         String product,
                                         String productVersion,
                                         Collection<AgentProjectInfo> projectInfos)
            throws WssServiceException {
        return summaryScan(orgToken, product, productVersion, projectInfos, null);
    }

    /**
     * Gets vulnerabilities data for given dependencies.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projectInfos   OSS usage information to send to white source.
     * @param userKey        user key uniquely identifying the account at white source.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @return Vulnerabilities for given dependencies.
     * @throws WssServiceException
     */

    public CheckVulnerabilitiesResult checkVulnerabilities(String orgToken,
                                                           String product,
                                                           String productVersion,
                                                           Collection<AgentProjectInfo> projectInfos,
                                                           String userKey,
                                                           String requesterEmail,
                                                           String productToken)
            throws WssServiceException {
        return client.checkVulnerabilities(
                requestFactory.newCheckVulnerabilitiesRequest(orgToken, product, productVersion, projectInfos, userKey, requesterEmail, productToken));
    }

    public CheckVulnerabilitiesResult checkVulnerabilities(String orgToken,
                                                           String product,
                                                           String productVersion,
                                                           Collection<AgentProjectInfo> projectInfos,
                                                           String userKey,
                                                           String requesterEmail)
            throws WssServiceException {
        return checkVulnerabilities(orgToken, product, productVersion, projectInfos, userKey, requesterEmail, null);
    }

    public CheckVulnerabilitiesResult checkVulnerabilities(String orgToken,
                                                           String product,
                                                           String productVersion,
                                                           Collection<AgentProjectInfo> projectInfos,
                                                           String userKey)
            throws WssServiceException {
        return checkVulnerabilities(orgToken, product, productVersion, projectInfos, userKey, null);
    }

    public CheckVulnerabilitiesResult checkVulnerabilities(String orgToken,
                                                           String product,
                                                           String productVersion,
                                                           Collection<AgentProjectInfo> projectInfos)
            throws WssServiceException {
        return checkVulnerabilities(orgToken, product, productVersion, projectInfos, null);
    }

    /**
     * Updates the White Source organization account with the given OSS information.
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param userKey        user key uniquely identifying the account at white source.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @return Result of updating white source.
     * @throws WssServiceException
     */

    public ConfigurationResult getConfiguration(String orgToken,
                                                String product,
                                                String productVersion,
                                                String userKey,
                                                String requesterEmail,
                                                String productToken)
            throws WssServiceException {
        return client.getConfiguration(requestFactory.newConfigurationRequest(orgToken, product, productVersion, userKey, requesterEmail, productToken));
    }

    public ConfigurationResult getConfiguration(String orgToken,
                                                String product,
                                                String productVersion,
                                                String userKey,
                                                String requesterEmail)
            throws WssServiceException {
        return getConfiguration(orgToken, product, productVersion, userKey, requesterEmail, null);
    }

    public ConfigurationResult getConfiguration(String orgToken,
                                                String product,
                                                String productVersion,
                                                String userKey)
            throws WssServiceException {
        return client.getConfiguration(requestFactory.newConfigurationRequest(orgToken, product, productVersion, userKey));
    }

    public ConfigurationResult getConfiguration(String orgToken,
                                                String product,
                                                String productVersion)
            throws WssServiceException {
        return getConfiguration(orgToken, product, productVersion, null);
    }

    /**
     * The method close the underlying client to the White Source service.
     *
     * @see org.whitesource.agent.client.WssServiceClient#shutdown()
     */
    public void shutdown() {
        client.shutdown();
    }

    /* --- Getters / Setters --- */

    public WssServiceClient getClient() {
        return client;
    }

    public void setClient(WssServiceClient client) {
        this.client = client;
    }

    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

    public void setRequestFactory(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

}
