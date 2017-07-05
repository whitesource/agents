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

import java.util.*;

/**
 * Base, abstract Result of check policy requests.
 * 
 * @author anna.rozin
 */
public abstract class BaseCheckPoliciesResult extends BaseResult {

    /* --- Static members --- */

	private static final long serialVersionUID = -4268012175086999291L;

	/* --- Members --- */

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
	public BaseCheckPoliciesResult() {
		existingProjects = new HashMap<String, PolicyCheckResourceNode>();
		newProjects = new HashMap<String, PolicyCheckResourceNode>();
		projectNewResources = new HashMap<String, Collection<ResourceInfo>>();
	}

	/**
	 * Constructor
	 *
	 * @param organization Name of the domain.
	 */
	public BaseCheckPoliciesResult(String organization) {
		this();
		setOrganization(organization);
	}

    /* --- Public methods --- */

	/**
	 * @return True if some project in this result have some rejected dependency.
	 */
	public boolean hasRejections() {
		boolean hasRejections = false;

		Collection<PolicyCheckResourceNode> roots = new ArrayList<PolicyCheckResourceNode>();
		roots.addAll(getExistingProjects().values());
		roots.addAll(getNewProjects().values());

		Iterator<PolicyCheckResourceNode> iterator = roots.iterator();
		while (iterator.hasNext() && !hasRejections ) {
			hasRejections = iterator.next().hasRejections();
		}

		return hasRejections;
	}

	/* --- Getters / Setters --- */

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