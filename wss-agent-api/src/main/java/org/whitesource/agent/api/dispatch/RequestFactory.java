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
package org.whitesource.agent.api.dispatch;

import org.whitesource.agent.api.model.AgentProjectInfo;

import java.util.Collection;

/**
 * Factory for constructing {@link ServiceRequest}.
 *
 * @author Edo.Shor
 */
public class RequestFactory {

    /* --- Members --- */

    private String agent;

    private String agentVersion;

    private String pluginVersion;

    /* --- Constructors --- */

    /**
     * Constructor
     *
     * @param agent         Agent type identifier.
     * @param agentVersion  Agent API version.
     * @param pluginVersion Plugin version.
     */
    public RequestFactory(String agent, String agentVersion, String pluginVersion) {
        this.agent = agent;
        this.agentVersion = agentVersion;
        this.pluginVersion = pluginVersion;
    }

    /* --- Public methods --- */

    /**
     * Create new Inventory Update request.
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Projects status statement to update.
     * @return Newly created request to update organization inventory.
     */
    public UpdateInventoryRequest newUpdateInventoryRequest(String orgToken, Collection<AgentProjectInfo> projects) {
        return newUpdateInventoryRequest(orgToken, null, null, null, projects);
    }

    /**
     * Create new Inventory Update request.
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Projects status statement to update.
     * @param product Name or WhiteSource service token of the product to update.
     * @param productVersion Version of the product to update.
     * @return Newly created request to update organization inventory.
     */
    public UpdateInventoryRequest newUpdateInventoryRequest(String orgToken,
                                                            String product,
                                                            String productVersion,
                                                            Collection<AgentProjectInfo> projects) {
        return newUpdateInventoryRequest(orgToken, null, product, productVersion, projects);
    }

    /**
     * Create new Inventory Update request.
     *
     * @param orgToken WhiteSource organization token.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @param updateType Request UpdateType
     * @param projects Projects status statement to update.
     * @param product Name or WhiteSource service token of the product to update.
     * @param productVersion Version of the product to update.
     * @return Newly created request to update organization inventory.
     */
    public UpdateInventoryRequest newUpdateInventoryRequest(String orgToken,
                                                            UpdateType updateType,
                                                            String requesterEmail,
                                                            String product,
                                                            String productVersion,
                                                            Collection<AgentProjectInfo> projects) {
        UpdateInventoryRequest updateInventoryRequest = new UpdateInventoryRequest(projects, updateType);
        return (UpdateInventoryRequest) prepareRequest(updateInventoryRequest, orgToken, requesterEmail, product, productVersion);
    }

    /**
     * Create new Inventory Update request.
     *
     * @param orgToken WhiteSource organization token.
     * @param requesterEmail Email of the WhiteSource user that requests to update WhiteSource.
     * @param projects Projects status statement to update.
     * @param product Name or WhiteSource service token of the product to update.
     * @param productVersion Version of the product to update.
     * @return Newly created request to update organization inventory.
     */
    public UpdateInventoryRequest newUpdateInventoryRequest(String orgToken,
                                                            String requesterEmail,
                                                            String product,
                                                            String productVersion,
                                                            Collection<AgentProjectInfo> projects) {
        return (UpdateInventoryRequest) prepareRequest(new UpdateInventoryRequest(projects), orgToken, requesterEmail, product, productVersion);
    }

    /**
     * Create new Check policies request.
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Projects status statement to check.
     * @return Newly created request to check policies application.
     */

    public CheckPoliciesRequest newCheckPoliciesRequest(String orgToken, Collection<AgentProjectInfo> projects) {
        return newCheckPoliciesRequest(orgToken, null, null, projects);
    }

    /**
     * Create new Check policies request.
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Projects status statement to check.
     * @param product Name or WhiteSource service token of the product whose policies to check.
     * @param productVersion Version of the product whose policies to check.
     * @return Newly created request to check policies application.
     * @deprecated Use {@link RequestFactory#newCheckPolicyComplianceRequest(String, String, String, Collection, boolean)}
     */
    public CheckPoliciesRequest newCheckPoliciesRequest(String orgToken,
                                                        String product,
                                                        String productVersion,
                                                        Collection<AgentProjectInfo> projects) {
        return (CheckPoliciesRequest) prepareRequest(new CheckPoliciesRequest(projects), orgToken, null, product, productVersion);
    }

    /**
     * Create new Check policies request.
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Projects status statement to check.
     * @param forceCheckAllDependencies boolean check that all/added dependencies sent to WhiteSource
     * @return Newly created request to check policies application.
     */

    public CheckPolicyComplianceRequest newCheckPolicyComplianceRequest(String orgToken, Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies) {
        return newCheckPolicyComplianceRequest(orgToken, null, null, projects, forceCheckAllDependencies);
    }
    
    /**
     * Create new Check policies request.
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Projects status statement to check.
     * @param product Name or WhiteSource service token of the product whose policies to check.
     * @param productVersion Version of the product whose policies to check.
     * @param forceCheckAllDependencies boolean check that all/added dependencies sent to WhiteSource
     * @return Newly created request to check policies application.
     */
    public CheckPolicyComplianceRequest newCheckPolicyComplianceRequest(String orgToken,
                                                                        String product,
                                                                        String productVersion,
                                                                        Collection<AgentProjectInfo> projects,
                                                                        boolean forceCheckAllDependencies) {
        return (CheckPolicyComplianceRequest) prepareRequest(new CheckPolicyComplianceRequest(projects, forceCheckAllDependencies), orgToken, null, product, productVersion);
    }


    /**
     * Create new Dependency Data request.
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Projects status statement to check.
     * @return Newly created request to get Dependency Additional Data (Licenses, Description, homepageUrl and Vulnerabilities).
     */

    public GetDependencyDataRequest newDependencyDataRequest(String orgToken, Collection<AgentProjectInfo> projects) {
        return newDependencyDataRequest(orgToken, null, null, projects);
    }

    /**
     * Create new Dependency Data request.
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Projects status statement to check.
     * @param product Name or WhiteSource service token of the product whose policies to check.
     * @param productVersion Version of the product whose policies to check.
     * @return Newly created request to get Dependency Additional Data (Licenses, Description, homepageUrl and Vulnerabilities).
     */
    public GetDependencyDataRequest newDependencyDataRequest(String orgToken,
                                                             String product,
                                                             String productVersion,
                                                             Collection<AgentProjectInfo> projects) {
        return (GetDependencyDataRequest) prepareRequest(new GetDependencyDataRequest(projects), orgToken, null, product, productVersion);
    }


    /**
     * Create new Inventory Update request.
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Projects status statement to update.
     * @param product Name or WhiteSource service token of the product to update.
     * @param productVersion Version of the product to update.
     * @return Newly created request to update organization inventory.
     */
    public SummaryScanRequest newSummaryScanRequest(String orgToken,
                                                    String product,
                                                    String productVersion,
                                                    Collection<AgentProjectInfo> projects) {
        return (SummaryScanRequest) prepareRequest(new SummaryScanRequest(projects), orgToken, null, product, productVersion);
    }

    /* --- Protected methods --- */

    protected <R> BaseRequest<R> prepareRequest(BaseRequest<R> request, String orgToken, String requesterEmail, String product, String productVersion) {
        request.setAgent(agent);
        request.setAgentVersion(agentVersion);
        request.setPluginVersion(pluginVersion);
        request.setOrgToken(orgToken);
        request.setProduct(product);
        request.setProductVersion(productVersion);
        request.setRequesterEmail(requesterEmail);
        return request;
    }

}
