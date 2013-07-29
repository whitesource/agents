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

import org.whitesource.agent.api.model.AgentProjectInfo;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Request to update organization inventory. 
 * 
 * @author tom.shapira
 */
public class UpdateInventoryRequest extends BaseRequest<UpdateInventoryResult> {

	/* --- Static members --- */
	
	private static final long serialVersionUID = 7731258010033962980L;
	
	/* --- Members --- */

	protected Collection<AgentProjectInfo> projects;

	/* --- Constructors --- */

    /**
     * Default constructor
     */
    public UpdateInventoryRequest() {
        super(RequestType.UPDATE);
        projects = new ArrayList<AgentProjectInfo>();
    }

    /**
     * Constructor
     *
     * @param projects Open Source usage statement to update White Source.
     */
    public UpdateInventoryRequest(Collection<AgentProjectInfo> projects) {
        this();
        this.projects = projects;
    }


	/**
	 * Constructor
	 * 
	 * @param orgToken WhiteSource organization token.
	 * @param projects Open Source usage statement to update White Source.
	 */
	public UpdateInventoryRequest(String orgToken, Collection<AgentProjectInfo> projects) {
		this(projects);
		this.orgToken = orgToken;
	}

	/* --- Getters / Setters --- */

	public Collection<AgentProjectInfo> getProjects() {
		return projects;
	}

	public void setProjects(Collection<AgentProjectInfo> projects) {
		this.projects = projects;
	}

}
