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

import org.whitesource.agent.api.dispatch.*;

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
     * @throws WssServiceException In case an error occurred during the call to White Source server.
     */
    UpdateInventoryResult updateInventory(UpdateInventoryRequest request) throws WssServiceException;

    /**
     * The method call the White Source service for checking policies application.
     * @param request Check Policies request.
     * @return Check Policies result.
     * @throws WssServiceException In case an error occurred during the call to White Source server.
     * @deprecated Use {@link WssServiceClient#checkPolicyCompliance(CheckPolicyComplianceRequest)}
     */
    CheckPoliciesResult checkPolicies(CheckPoliciesRequest request) throws WssServiceException;

    /**
     * The method call the White Source service for checking policies application.
     * @param request Check Policies request.
     * @return Check Policies result.
     * @throws WssServiceException In case an error occurred during the call to White Source server.
     */
    CheckPolicyComplianceResult checkPolicyCompliance(CheckPolicyComplianceRequest request) throws WssServiceException;

    /**
     * The method close all connections and release resources.
     * No communication can be done on this client after the method is invoked.
     */
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
}
