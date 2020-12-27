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

import com.google.gson.Gson;
import org.junit.Test;
import org.whitesource.agent.api.dispatch.*;
import org.whitesource.agent.api.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Unit test for api model and operation pojos serialization.
 *
 * @author Edo.Shor
 */
public class ModelSerializationTest {

    /* --- Members --- */

    private RequestFactory requestFactory = new RequestFactory("agent", "agentVersion", "pluginVersion");

    private Gson gson = new Gson();

    /* --- Test methods --- */

    @Test
    public void testAddingRequestTokenToResultEnvelope() {
        ResultEnvelope resultEnvelope = new Gson().fromJson(
                "{" +
                        "   'envelopeVersion' : '2.6.1', " +
                        "   'status' : 1," +
                        "   'message' : 'test message'," +
                        "   'message' : 'test message'," +
                        "   'data' : 'test data'," +
                        "   'requestToken' : 'test request token'" +
                        "}", ResultEnvelope.class);
        assertNotNull(resultEnvelope);

        resultEnvelope = new Gson().fromJson(
                "{" +
                        "   'envelopeVersion' : '2.6.1', " +
                        "   'status' : 1," +
                        "   'message' : 'test message'," +
                        "   'message' : 'test message'," +
                        "   'data' : 'test data'" +
                        "}", ResultEnvelope.class);
        assertNotNull(resultEnvelope);
    }

    @Test
    public void testUpdateInventoryRequest() {
        Collection<AgentProjectInfo> projectInfos = createTestProjects();
        UpdateInventoryRequest request = requestFactory.newUpdateInventoryRequest("orgToken", projectInfos, "userKey");
        UpdateInventoryRequest deserializedRequest = testRequest(request, UpdateInventoryRequest.class);
        assertProjectsEquals(deserializedRequest.getProjects(), request.getProjects());
    }

    @Test
    public void testCheckPoliciesRequest() {
        Collection<AgentProjectInfo> projectInfos = createTestProjects();
        CheckPoliciesRequest request = requestFactory.newCheckPoliciesRequest("orgToken", projectInfos, "userKey");
        CheckPoliciesRequest deserializedRequest = testRequest(request, CheckPoliciesRequest.class);
        assertProjectsEquals(deserializedRequest.getProjects(), request.getProjects());
    }

    @Test
    public void testCheckPolicyComplianceRequest() {
        Collection<AgentProjectInfo> projectInfos = createTestProjects();
        CheckPolicyComplianceRequest request = requestFactory.newCheckPolicyComplianceRequest("orgToken", projectInfos, true, "userKey");
        CheckPolicyComplianceRequest deserializedRequest = testRequest(request, CheckPolicyComplianceRequest.class);
        assertProjectsEquals(deserializedRequest.getProjects(), request.getProjects());
    }

    @Test
    public void testUpdateInventoryResult() {
        UpdateInventoryResult result = new UpdateInventoryResult();
        result.setOrganization("organization");
        result.setCreatedProjects(Arrays.asList("created-project-1", "created-project-2", "created-project-3"));
        result.setUpdatedProjects(Arrays.asList("updated-project-1", "updated-project-2", "updated-project-3"));

        UpdateInventoryResult deserializedResult = testResult(result, UpdateInventoryResult.class);
        assertEquals(result.getOrganization(), deserializedResult.getOrganization());
        assertEquals(result.getCreatedProjects(), deserializedResult.getCreatedProjects());
        assertEquals(result.getUpdatedProjects(), deserializedResult.getUpdatedProjects());
    }

    @Test
    public void testCheckPoliciesResult() {
        CheckPoliciesResult result = createCheckPoliciesResult();
        CheckPoliciesResult deserializedResult = testResult(result, CheckPoliciesResult.class);
        assertCheckPoliciesResultEquals(deserializedResult, result);
    }

    /* --- Private methods --- */

    private <R, T extends BaseRequest<R>> T testRequest(T request, Class<T> clazz) {
        String serializedRequest = gson.toJson(request);
        T deserializedRequest = gson.fromJson(serializedRequest, clazz);
        assertRequestEquals(deserializedRequest, request);

        return deserializedRequest;
    }

    private <R> void assertRequestEquals(ServiceRequest<R> request, ServiceRequest<R> expected) {
        assertEquals(expected.type(), request.type());
        assertEquals(expected.agent(), request.agent());
        assertEquals(expected.agentVersion(), request.agentVersion());
        assertEquals(expected.orgToken(), request.orgToken());
        assertEquals(expected.timeStamp(), request.timeStamp());
    }

    private <T> T testResult(T result, Class<T> clazz) {
        String serializedResult = gson.toJson(result);
        ResultEnvelope envelope = new ResultEnvelope(ResultEnvelope.STATUS_SUCCESS, ResultEnvelope.MESSAGE_OK, serializedResult);
        String serializedEnvelop = gson.toJson(envelope);
        ResultEnvelope deserializedEnvelop = gson.fromJson(serializedEnvelop, ResultEnvelope.class);
        assertEquals(envelope.getStatus(), deserializedEnvelop.getStatus());
        assertEquals(envelope.getMessage(), deserializedEnvelop.getMessage());
        assertEquals(envelope.getData(), deserializedEnvelop.getData());

        return gson.fromJson(envelope.getData(), clazz);
    }

    private Collection<AgentProjectInfo> createTestProjects() {
        Collection<AgentProjectInfo> projectInfos = new ArrayList<AgentProjectInfo>();

        AgentProjectInfo projectInfo = new AgentProjectInfo();
        projectInfos.add(projectInfo);
        projectInfo.setProjectToken("projectToken");
        projectInfo.setCoordinates(new Coordinates("groupId", "artifactId", "version"));
        projectInfo.setParentCoordinates(new Coordinates("groupId", "artifactId", "version"));

        for (int i = 0; i < 5; i++) {
            DependencyInfo dependencyInfo = new DependencyInfo("groupId-" + i, "artifactId-" + i, "version-" + i);
            dependencyInfo.setClassifier("classifier-" + i);
            dependencyInfo.setType("type-" + i);
            dependencyInfo.setScope("scope-" + i);
            dependencyInfo.setSha1("sha1-" + i);
            dependencyInfo.setSystemPath("systemPath-" + i);
            dependencyInfo.setOptional(true);

            projectInfo.getDependencies().add(dependencyInfo);
        }

        return projectInfos;
    }

    private void assertProjectsEquals(Collection<AgentProjectInfo> projectInfos, Collection<AgentProjectInfo> expected) {
        if (expected == null) {
            assertNull(projectInfos);
            return;
        }

        assertNotNull(projectInfos);
        assertEquals(expected.size(), projectInfos.size());
        Iterator<AgentProjectInfo> iterator = projectInfos.iterator();
        Iterator<AgentProjectInfo> expectedIterator = expected.iterator();
        while (iterator.hasNext()) {
            AgentProjectInfo project = iterator.next();
            AgentProjectInfo expectedProject = expectedIterator.next();
            assertNotNull(project);
            assertEquals(expectedProject.getProjectToken(), project.getProjectToken());
            assertEquals(expectedProject.getCoordinates(), project.getCoordinates());
            assertEquals(expectedProject.getParentCoordinates(), project.getParentCoordinates());
            testDependencies(project.getDependencies(), expectedProject.getDependencies());
        }

    }

    private void testDependencies(Collection<DependencyInfo> dependencies, Collection<DependencyInfo> expected) {
        if (expected == null) {
            assertNull(dependencies);
            return;
        }

        assertNotNull(dependencies);
        assertEquals(expected.size(), dependencies.size());


        Iterator<DependencyInfo> iterator = dependencies.iterator();
        Iterator<DependencyInfo> expectedIterator = expected.iterator();
        while (iterator.hasNext()) {
            DependencyInfo dependency = iterator.next();
            DependencyInfo expectedDependency = expectedIterator.next();
            assertEquals(expectedDependency.getGroupId(), dependency.getGroupId());
            assertEquals(expectedDependency.getArtifactId(), dependency.getArtifactId());
            assertEquals(expectedDependency.getVersion(), dependency.getVersion());
            assertEquals(expectedDependency.getClassifier(), dependency.getClassifier());
            assertEquals(expectedDependency.getType(), dependency.getType());
            assertEquals(expectedDependency.getScope(), dependency.getScope());
            assertEquals(expectedDependency.getSha1(), dependency.getSha1());
            assertEquals(expectedDependency.getSystemPath(), dependency.getSystemPath());
            assertEquals(expectedDependency.isOptional(), dependency.getOptional());
        }
    }

    private void testExclusions(Collection<ExclusionInfo> exclusions, Collection<ExclusionInfo> expected) {
        if (expected == null) {
            assertNull(exclusions);
            return;
        }

        assertNotNull(exclusions);
        assertEquals(expected.size(), exclusions.size());
        Iterator<ExclusionInfo> iterator = exclusions.iterator();
        Iterator<ExclusionInfo> expectedIterator = expected.iterator();
        while (iterator.hasNext()) {
            ExclusionInfo exclusion = iterator.next();
            ExclusionInfo expectedExclusion = expectedIterator.next();

            assertEquals(expectedExclusion.getGroupId(), exclusion.getGroupId());
            assertEquals(expectedExclusion.getArtifactId(), exclusion.getArtifactId());
        }

    }

    private CheckPoliciesResult createCheckPoliciesResult() {
        CheckPoliciesResult result = new CheckPoliciesResult();
        result.setOrganization("organization");

        PolicyCheckResourceNode root = new PolicyCheckResourceNode();
        result.getNewProjects().put("newProject - 0.0.1", root);
        result.getExistingProjects().put("existingProject - 1.0", root);

        Collection<ResourceInfo> resources = new ArrayList<ResourceInfo>();

        ResourceInfo resource = new ResourceInfo("artifactId-1");
        resources.add(resource);
        resource.setLink("link-to-resource-1");
        resource.setLicenses(Arrays.asList("license-1", "license-2"));
        root.getChildren().add(new PolicyCheckResourceNode(resource, new RequestPolicyInfo("policy-1")));

        resource = new ResourceInfo("artifactId-2");
        resources.add(resource);
        resource.setLink("link-to-resource-2");
        PolicyCheckResourceNode node = new PolicyCheckResourceNode(resource, new RequestPolicyInfo("policy-2"));
        root.getChildren().add(node);

        resource = new ResourceInfo("artifactId-3");
        resources.add(resource);
        resource.setLink("link-to-resource-3");
        node.getChildren().add(new PolicyCheckResourceNode(resource, null));
        resource = new ResourceInfo("artifactId-4");
        resources.add(resource);
        resource.setLink("link-to-resource-4");
        node.getChildren().add(new PolicyCheckResourceNode(resource, null));

        result.getProjectNewResources().put("newProject - 0.0.1", resources);
        result.getProjectNewResources().put("existingProject - 1.0", resources);

        return result;
    }

    public void assertCheckPoliciesResultEquals(CheckPoliciesResult result, CheckPoliciesResult expected) {
        assertEquals(expected.getOrganization(), result.getOrganization());

        // TODO: implement
    }

}
