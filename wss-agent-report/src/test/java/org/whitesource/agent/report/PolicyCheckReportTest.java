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
package org.whitesource.agent.report;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.whitesource.agent.api.dispatch.CheckPoliciesResult;
import org.whitesource.agent.api.model.PolicyCheckResourceNode;
import org.whitesource.agent.api.model.RequestPolicyInfo;
import org.whitesource.agent.api.model.ResourceInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Edo.Shor
 */
public class PolicyCheckReportTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();


    /* --- Test methods --- */

    @Test(expected = IllegalStateException.class)
    public void testNullResult() throws IOException {
        PolicyCheckReport report = new PolicyCheckReport(null);
        report.generate(null, false);
    }

    @Test(expected = IOException.class)
    public void testNonExistingOutoutDirectory() throws IOException {
        PolicyCheckReport report = new PolicyCheckReport(new CheckPoliciesResult());
        report.generate(new File("/non/existing/dir"), false);
    }

    @Test
    public void testEmptyResult() throws IOException {
        CheckPoliciesResult result = new CheckPoliciesResult();
        PolicyCheckReport report = new PolicyCheckReport(result);
        report.generate(temporaryFolder.getRoot(), false);
        report.getBuildName(); // just for breakpoints
    }

    @Test
    public void testResultWEmptyProjects() throws IOException {
        PolicyCheckResourceNode root = new PolicyCheckResourceNode();
        CheckPoliciesResult result = new CheckPoliciesResult();
        result.setOrganization("Report test org");
        result.getExistingProjects().put("existing-project-1", root);
        result.getExistingProjects().put("existing-project-2", root);
        result.getNewProjects().put("new-project-1", root);
        result.getProjectNewResources().put("existing-project-1", new ArrayList<ResourceInfo>());
        result.getProjectNewResources().put("existing-project-2", new ArrayList<ResourceInfo>());
        result.getProjectNewResources().put("new-project-1", new ArrayList<ResourceInfo>());

        PolicyCheckReport report = new PolicyCheckReport(result);
        report.generate(temporaryFolder.getRoot(), false);
        report.getBuildName(); // just for breakpoints
    }

    @Test
    public void testCreateReport() throws IOException {
        CheckPoliciesResult result = createResult();
        PolicyCheckReport report = new PolicyCheckReport(result);

        report.generate(temporaryFolder.getRoot(), false);
        report.getBuildName(); // just for breakpoints

        report.generate(temporaryFolder.newFolder("package"), true);
        report.getBuildName(); // just for breakpoints

        report.setBuildName("Nightly build");
        report.setBuildNumber("124");
        report.generate(temporaryFolder.newFolder("with-build-info"), false);
        report.getBuildName(); // just for breakpoints
    }

    @Test
    public void testCreateJsonReport() throws IOException {
        CheckPoliciesResult result = createResult();
        PolicyCheckReport report = new PolicyCheckReport(result);

        report.generate(temporaryFolder.getRoot(), false);
        report.getBuildName(); // just for breakpoints

        report.generate(temporaryFolder.newFolder("package"), true);
        report.getBuildName(); // just for breakpoints

        report.setBuildName("Nightly build");
        report.setBuildNumber("124");
        report.generateJson(temporaryFolder.newFolder("with-build-info"));
    }

    /* --- Private methods --- */

    private CheckPoliciesResult createResult() {
        RequestPolicyInfo approvePolicy = new RequestPolicyInfo("Approve something");
        approvePolicy.setActionType("Approve");
        RequestPolicyInfo rejectPolicy = new RequestPolicyInfo("Reject something");
        rejectPolicy.setActionType("Reject");
        RequestPolicyInfo rejectPolicy2 = new RequestPolicyInfo("Reject other thing");
        rejectPolicy2.setActionType("Reject");

        String link = "http://www.whitesourcesoftware.com";

        PolicyCheckResourceNode root = new PolicyCheckResourceNode();
        Collection<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();

        ResourceInfo resource = new ResourceInfo("resource-1");
        resource.setLink(link);
        resource.getLicenses().add("GPL 2.0");
        resource.setSha1("1");
        resourceInfos.add(resource);
        root.getChildren().add(new PolicyCheckResourceNode(resource, rejectPolicy));

        resource = new ResourceInfo("resource-2");
        resource.setLink(link);
        resource.getLicenses().add("Unknown");
        resource.setSha1("2");
        resourceInfos.add(resource);
        root.getChildren().add(new PolicyCheckResourceNode(resource, null));

        resource = new ResourceInfo("resource-3");
        resource.setLink(link);
        resource.getLicenses().add("Apache 2.0");
        resource.setSha1("3");
        resourceInfos.add(resource);
        root.getChildren().add(new PolicyCheckResourceNode(resource, approvePolicy));

        resource = new ResourceInfo("resource-4");
        resource.setLink(link);
        resource.getLicenses().add("Apache 2.0");
        resource.setSha1("4");
        resourceInfos.add(resource);
        PolicyCheckResourceNode node = new PolicyCheckResourceNode(resource, null);

        resource= new ResourceInfo("resource-44");
        resource.setLink(link);
        resource.getLicenses().add("GPL 3.0");
        resource.setSha1("5");
        resourceInfos.add(resource);
        node.getChildren().add(new PolicyCheckResourceNode(resource, rejectPolicy));

        resource= new ResourceInfo("resource-45");
        resource.setLink(link);
        resource.getLicenses().add("GPL 3.0");
        resource.setSha1("6");
        resourceInfos.add(resource);
        node.getChildren().add(new PolicyCheckResourceNode(resource, rejectPolicy2));

        resource= new ResourceInfo("resource-46");
        resource.setLink(link);
        resource.getLicenses().add("EPL 1.0");
        resource.setSha1("7");
        resourceInfos.add(resource);
        node.getChildren().add(new PolicyCheckResourceNode(resource, null));

        resource= new ResourceInfo("resource-47");
        resource.setLink(link);
        resource.getLicenses().add("MIT");
        resource.setSha1("8");
        resourceInfos.add(resource);
        node.getChildren().add(new PolicyCheckResourceNode(resource, approvePolicy));
        root.getChildren().add(node);

        CheckPoliciesResult result = new CheckPoliciesResult();
        result.setOrganization("Report test org");
        result.getExistingProjects().put("existing-project-1", root);
        result.getExistingProjects().put("existing-project-2", root);
        result.getNewProjects().put("new-project-1", root);
        result.getProjectNewResources().put("existing-project-1", resourceInfos);
        result.getProjectNewResources().put("existing-project-2", resourceInfos);
        result.getProjectNewResources().put("new-project-1", resourceInfos);

        return result;
    }
}
