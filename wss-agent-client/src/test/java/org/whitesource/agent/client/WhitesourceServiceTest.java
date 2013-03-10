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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.whitesource.agent.api.dispatch.CheckPoliciesRequest;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.DependencyInfo;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * @author Edo.Shor
 */
@RunWith(MockitoJUnitRunner.class)
public class WhitesourceServiceTest {

    /* --- Members --- */

    @Mock private WssServiceClient client;

    /* --- Test methods --- */

    @Test
    public void testDefaultConstructor() {
        WhitesourceService service = new WhitesourceService();

        UpdateInventoryRequest updateRequest = service.getRequestFactory().newUpdateInventoryRequest("orgToken", null);
        assertEquals("generic", updateRequest.agent());
        assertEquals("1.0", updateRequest.agentVersion());
        assertEquals(ClientConstants.DEFAULT_SERVICE_URL, ((WssServiceClientImpl) service.getClient()).getServiceUrl());

        service = new WhitesourceService("agent", "agentVersion");
        updateRequest = service.getRequestFactory().newUpdateInventoryRequest("orgToken", null);
        assertEquals("agent", updateRequest.agent());
        assertEquals("agentVersion", updateRequest.agentVersion());
        assertEquals(ClientConstants.DEFAULT_SERVICE_URL, ((WssServiceClientImpl) service.getClient()).getServiceUrl());

        System.setProperty(ClientConstants.SERVICE_URL_KEYWORD, "serviceUrl");
        service = new WhitesourceService();
        assertEquals("serviceUrl", ((WssServiceClientImpl) service.getClient()).getServiceUrl());

        service = new WhitesourceService("agent", "agentVersion", "serviceUrl");
        assertEquals("serviceUrl", ((WssServiceClientImpl) service.getClient()).getServiceUrl());

    }

    @Test
    public void testServiceMethods() throws WssServiceException {
        WhitesourceService service = new WhitesourceService();
        service.setClient(client);

        service.update("orgToken", new ArrayList<AgentProjectInfo>());
        ArgumentCaptor<UpdateInventoryRequest> updateCaptor = ArgumentCaptor.forClass(UpdateInventoryRequest.class);
        verify(client).updateInventory(updateCaptor.capture());
        assertEquals("orgToken", updateCaptor.getValue().orgToken());
        assertTrue(updateCaptor.getValue().getProjects().isEmpty());

        service.checkPolicies("orgToken", new ArrayList<AgentProjectInfo>());
        ArgumentCaptor<CheckPoliciesRequest> checkPoliciesCaptor = ArgumentCaptor.forClass(CheckPoliciesRequest.class);
        verify(client).checkPolicies(checkPoliciesCaptor.capture());
        assertEquals("orgToken", checkPoliciesCaptor.getValue().orgToken());
    }

}
