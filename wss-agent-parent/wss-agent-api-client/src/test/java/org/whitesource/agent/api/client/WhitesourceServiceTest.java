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
package org.whitesource.agent.api.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.whitesource.agent.api.dispatch.PropertiesRequest;
import org.whitesource.agent.api.dispatch.ReportRequest;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.DependencyInfo;
import org.whitesource.api.client.*;

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

        PropertiesRequest propertiesRequest = service.getRequestFactory().newPropertiesRequest("orgToken");
        assertEquals("generic", propertiesRequest.agent());
        assertEquals("1.0", propertiesRequest.agentVersion());
        assertEquals(ClientConstants.DEFAULT_SERVICE_URL, ((WssServiceClientImpl) service.getClient()).getServiceUrl());

        service = new WhitesourceService("agent", "agentVersion");
        propertiesRequest = service.getRequestFactory().newPropertiesRequest("orgToken");
        assertEquals("agent", propertiesRequest.agent());
        assertEquals("agentVersion", propertiesRequest.agentVersion());
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
        service.getProperties("orgToken");
        ArgumentCaptor<PropertiesRequest> propertiesCaptor = ArgumentCaptor.forClass(PropertiesRequest.class);
        verify(client).getProperties(propertiesCaptor.capture());
        assertEquals("orgToken", propertiesCaptor.getValue().orgToken());

        service.update("orgToken", new ArrayList<AgentProjectInfo>());
        ArgumentCaptor<UpdateInventoryRequest> updateCaptor = ArgumentCaptor.forClass(UpdateInventoryRequest.class);
        verify(client).updateInventory(updateCaptor.capture());
        assertEquals("orgToken", updateCaptor.getValue().orgToken());
        assertTrue(updateCaptor.getValue().getProjects().isEmpty());

        service.getReport(new ArrayList<DependencyInfo>());
        ArgumentCaptor<ReportRequest> reportCaptor = ArgumentCaptor.forClass(ReportRequest.class);
        verify(client).getReport(reportCaptor.capture());
        assertTrue(reportCaptor.getValue().getDependencies().isEmpty());
    }

}
