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

import org.whitesource.agent.api.model.PolicyCheckResourceNode;
import org.whitesource.agent.api.model.ResourceInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Result of the check policies operation.
 * 
 * @author Edo.Shor
 *
 * @since 1.2.0
 */
public class CheckPoliciesResult {

	/* --- Members --- */

    /**
     * Name of organization in.
     */
	private String organization;

    /**
     * Map of project name to the root of its dependency graph with policies application result.
     */
	private Map<String, PolicyCheckResourceNode> existingProjects;

    /**
     * Map of project name to the root of its dependency graph with policies application result.
     */
	private Map<String, PolicyCheckResourceNode> newProjects;

    /**
     * Map of project name to its set of new resources to insert into inventory.
     */
    private Map<String, Collection<ResourceInfo>> projectNewResources;

	/* --- Constructors --- */

	/**
	 * Default constructor
	 */
	public CheckPoliciesResult() {
		existingProjects = new HashMap<String, PolicyCheckResourceNode>();
        newProjects = new HashMap<String, PolicyCheckResourceNode>();
        projectNewResources = new HashMap<String, Collection<ResourceInfo>>();
	}

	/**
	 * Constructor
	 *
	 * @param organization Name of the domain.
	 */
	public CheckPoliciesResult(String organization) {
		this();
		this.organization = organization;
	}

	/* --- Getters / Setters --- */ 

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

    public Map<String, PolicyCheckResourceNode> getNewProjects() {
        return newProjects;
    }

    public void setNewProjects(Map<String, PolicyCheckResourceNode> newProjects) {
        this.newProjects = newProjects;
    }

    public Map<String, PolicyCheckResourceNode> getExistingProjects() {
        return existingProjects;
    }

    public void setExistingProjects(Map<String, PolicyCheckResourceNode> existingProjects) {
        this.existingProjects = existingProjects;
    }

    public Map<String, Collection<ResourceInfo>> getProjectNewResources() {
        return projectNewResources;
    }

    public void setProjectNewResources(Map<String, Collection<ResourceInfo>> projectNewResources) {
        this.projectNewResources = projectNewResources;
    }
}