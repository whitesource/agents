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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.whitesource.agent.api.dispatch.CheckPoliciesRequest;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.model.AgentProjectInfo;

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

    @Mock
    private WssServiceClient client;

    /* --- Test methods --- */

    @Test
    public void testDefaultConstructor() {
        WhitesourceService service = new WhitesourceService();

        UpdateInventoryRequest updateRequest = service.getRequestFactory().newUpdateInventoryRequest("orgToken", null, "userKey" );
        assertEquals("generic", updateRequest.agent());
        assertEquals("1.0", updateRequest.agentVersion());
        assertEquals(ClientConstants.DEFAULT_SERVICE_URL, ((WssServiceClientImpl) service.getClient()).getServiceUrl());

        service = new WhitesourceService("agent", "agentVersion", "pluginVersion");
        updateRequest = service.getRequestFactory().newUpdateInventoryRequest("orgToken", null, "userKey");
        assertEquals("agent", updateRequest.agent());
        assertEquals("agentVersion", updateRequest.agentVersion());
        assertEquals(ClientConstants.DEFAULT_SERVICE_URL, ((WssServiceClientImpl) service.getClient()).getServiceUrl());

        System.setProperty(ClientConstants.SERVICE_URL_KEYWORD, "serviceUrl");
        service = new WhitesourceService();
        assertEquals("serviceUrl", ((WssServiceClientImpl) service.getClient()).getServiceUrl());

        service = new WhitesourceService("agent", "agentVersion", "pluginVersion", "serviceUrl");
        assertEquals("serviceUrl", ((WssServiceClientImpl) service.getClient()).getServiceUrl());
    }

    @Test
    public void testServiceMethods() throws WssServiceException {
        WhitesourceService service = new WhitesourceService();
        service.setClient(client);

        service.update("orgToken", "product", "productVersion", new ArrayList<AgentProjectInfo>(), "userKey");
        ArgumentCaptor<UpdateInventoryRequest> updateCaptor = ArgumentCaptor.forClass(UpdateInventoryRequest.class);
        verify(client).updateInventory(updateCaptor.capture());
        assertEquals("orgToken", updateCaptor.getValue().orgToken());
        assertEquals("product", updateCaptor.getValue().product());
        assertEquals("productVersion", updateCaptor.getValue().productVersion());
        assertTrue(updateCaptor.getValue().getProjects().isEmpty());

        service.checkPolicies("orgToken", "product", "productVersion", new ArrayList<AgentProjectInfo>(), "userKey");
        ArgumentCaptor<CheckPoliciesRequest> checkPoliciesCaptor = ArgumentCaptor.forClass(CheckPoliciesRequest.class);
        verify(client).checkPolicies(checkPoliciesCaptor.capture());
        assertEquals("orgToken", checkPoliciesCaptor.getValue().orgToken());
        assertEquals("product", updateCaptor.getValue().product());
        assertEquals("productVersion", updateCaptor.getValue().productVersion());

    }

}
