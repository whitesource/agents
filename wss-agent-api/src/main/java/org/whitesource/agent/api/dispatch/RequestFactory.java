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

    /* --- Constructors --- */

    /**
     * Constructor
     *
     * @param agent        Agent type identifier.
     * @param agentVersion Agent version.
     */
    public RequestFactory(String agent, String agentVersion) {
        this.agent = agent;
        this.agentVersion = agentVersion;
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
        return newUpdateInventoryRequest(orgToken, null, null, projects);
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
        return (UpdateInventoryRequest) prepareRequest(new UpdateInventoryRequest(projects), orgToken, product, productVersion);
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
     */
    public CheckPoliciesRequest newCheckPoliciesRequest(String orgToken,
                                                        String product,
                                                        String productVersion,
                                                        Collection<AgentProjectInfo> projects) {
        return (CheckPoliciesRequest) prepareRequest(new CheckPoliciesRequest(projects), orgToken, product, productVersion);
    }

    /**
     * Creates new Get In House rules request.
     *
     * @param orgToken WhiteSource organization token.
     * @return Newly created request to get in house rules.
     */
    public GetInHouseRulesRequest newGetInHouseRulesRequest(String orgToken) {
        return (GetInHouseRulesRequest) prepareRequest(new GetInHouseRulesRequest(), orgToken, null, null);
    }

    /* --- Protected methods --- */

    protected <R> BaseRequest<R> prepareRequest(BaseRequest<R> request, String orgToken, String product, String productVersion) {
        request.setAgent(agent);
        request.setAgentVersion(agentVersion);
        request.setOrgToken(orgToken);
        request.setProduct(product);
        request.setProductVersion(productVersion);

        return request;
    }

}
