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

import com.google.gson.annotations.Since;
import org.whitesource.agent.api.AgentApiVersion;
import org.whitesource.agent.api.model.ProjectDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


/**
 * Result of the update inventory operation.
 *
 * @author Edo.Shor
 */
public class UpdateInventoryResult extends BaseResult {

	/* --- Static members --- */

	private static final long serialVersionUID = 9074828488465436483L;

	/* --- Members --- */

	private boolean removeIfExist = false;

	private Collection<String> updatedProjects;

	private Collection<String> createdProjects;

	private HashMap<String, Integer> projectNamesToIds;

	/*
	 * @since 2.9.8
	 */
	@Since(AgentApiVersion.AGENT_API_VERSION_2_9_8)
	private HashMap<String, ProjectDetails> projectNamesToDetails;


	/* --- Constructors --- */
	/**
	 * Default constructor (for JSON parsing)
	 *
	 */
	public UpdateInventoryResult() {
		updatedProjects = new ArrayList<String>();
		createdProjects = new ArrayList<String>();
		projectNamesToIds = new HashMap<>();
		projectNamesToDetails = new HashMap<>();
	}

	/**
	 * Constructor
	 *
	 * @param organization Name of the domain.
	 */
	public UpdateInventoryResult(String organization) {
		this();
		setOrganization(organization);
	}

	public UpdateInventoryResult(String organization,boolean removeIfExist) {
		this(organization);
		this.removeIfExist = removeIfExist;
	}


	/* --- Getters / Setters --- */
	public Collection<String> getUpdatedProjects() {
		return updatedProjects;
	}

	public void setUpdatedProjects(Collection<String> updatedProjects) {
		this.updatedProjects = updatedProjects;
	}

	public Collection<String> getCreatedProjects() {
		return createdProjects;
	}

	public void setCreatedProjects(Collection<String> createdProjects) {
		this.createdProjects = createdProjects;
	}

	public HashMap<String, Integer> getProjectNamesToIds() {
		return projectNamesToIds;
	}

	public void setProjectNamesToIds(HashMap<String, Integer> projectNamesToIds) {
		this.projectNamesToIds = projectNamesToIds;
	}

	public HashMap<String, ProjectDetails> getProjectNamesToDetails(){
		return projectNamesToDetails;
	}

	public void setProjectNamesToDetails(HashMap<String, ProjectDetails> value){
		this.projectNamesToDetails = value;
	}
}