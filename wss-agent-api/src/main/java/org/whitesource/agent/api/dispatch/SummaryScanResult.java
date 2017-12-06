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

import java.util.ArrayList;
import java.util.Collection;


/**
 * Result of the update inventory operation. 
 * 
 * @author Edo.Shor
 */
public class SummaryScanResult extends BaseResult {

	/* --- Static members --- */

	private static final long serialVersionUID = 5277673506168727608L;

    /* --- Members --- */

	private Collection<String> updatedProjects;

	private Collection<String> createdProjects;

	/* --- Constructors --- */

	/**
	 * Default constructor (for JSON parsing)
	 *
	 */
	public SummaryScanResult() {
		updatedProjects = new ArrayList<String>();
		createdProjects = new ArrayList<String>();
	}

	/**
	 * Constructor
	 *
	 * @param organization Name of the domain.
	 */
	public SummaryScanResult(String organization) {
		this();
		setOrganization(organization);
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

}