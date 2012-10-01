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
package org.whitesource.agent.api.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Edo.Shor
 */
public class PolicyCheckResourceNodeTest {

    @Test
    public void testGetRejections() {
        RequestPolicyInfo approvePolicy = new RequestPolicyInfo("Approve something");
        approvePolicy.setActionType("Approve");
        RequestPolicyInfo rejectPolicy = new RequestPolicyInfo("Reject something");
        rejectPolicy.setActionType("Reject");

        PolicyCheckResourceNode root = new PolicyCheckResourceNode();

        root.getChildren().add(new PolicyCheckResourceNode(new ResourceInfo("resource-1"), rejectPolicy));
        root.getChildren().add(new PolicyCheckResourceNode(new ResourceInfo("resource-2"), null));
        root.getChildren().add(new PolicyCheckResourceNode(new ResourceInfo("resource-3"), approvePolicy));

        PolicyCheckResourceNode node = new PolicyCheckResourceNode(new ResourceInfo("resource-4"), null);
        node.getChildren().add(new PolicyCheckResourceNode(new ResourceInfo("resource-44"), rejectPolicy));
        node.getChildren().add(new PolicyCheckResourceNode(new ResourceInfo("resource-45"), rejectPolicy));
        node.getChildren().add(new PolicyCheckResourceNode(new ResourceInfo("resource-46"), null));
        node.getChildren().add(new PolicyCheckResourceNode(new ResourceInfo("resource-47"), approvePolicy));
        root.getChildren().add(node);

        assertTrue(root.hasRejections());
        assertTrue(node.hasRejections());
    }
}
