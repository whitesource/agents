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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A node for each resource in a dependency graph with associated policy application results.
 *
 * @author Edo.Shor
 * @since 1.2.0
 */
public class PolicyCheckResourceNode implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -3206810244665473L;

    /* --- Members --- */

    private ResourceInfo resource;

    private RequestPolicyInfo policy;

    private Collection<PolicyCheckResourceNode> children;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public PolicyCheckResourceNode() {
        children = new ArrayList<PolicyCheckResourceNode>();
    }

    /**
     * Constructor
     *
     * @param resource
     * @param policy
     */
    public PolicyCheckResourceNode(ResourceInfo resource, RequestPolicyInfo policy) {
        this();
        this.resource = resource;
        this.policy = policy;
    }

    /* --- Public methods --- */

    public boolean hasRejections() {
        boolean rejections = policy != null && "Reject".equals(policy.getActionType());

        Iterator<PolicyCheckResourceNode> it = children.iterator();
        while (it.hasNext() && !rejections) {
            rejections = it.next().hasRejections();
        }

        return rejections;
    }

    /* --- Getters / Setters --- */

    public ResourceInfo getResource() {
        return resource;
    }

    public void setResource(ResourceInfo resource) {
        this.resource = resource;
    }

    public RequestPolicyInfo getPolicy() {
        return policy;
    }

    public void setPolicy(RequestPolicyInfo policy) {
        this.policy = policy;
    }

    public Collection<PolicyCheckResourceNode> getChildren() {
        return children;
    }

    public void setChildren(Collection<PolicyCheckResourceNode> children) {
        this.children = children;
    }
}
