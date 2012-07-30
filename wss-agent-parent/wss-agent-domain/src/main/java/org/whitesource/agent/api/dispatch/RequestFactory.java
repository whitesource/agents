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

import java.util.Collection;

import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.DependencyInfo;

/**
 * Factory for constructing {@link ServiceRequest}.
 * 
 * @author Edo.Shor
 */
public class RequestFactory {

	/* --- Members --- */
	
	private String agent;
	
	private String agentVersion;
	
	/* --- Constructors --- */
	
	/**
	 * Constructor
	 * 
	 * @param agent Agent type identifier.
	 * @param agentVersion Agent version.
	 */
	public RequestFactory(String agent, String agentVersion) {
		this.agent = agent;
		this.agentVersion = agentVersion;
	}
	
	/* --- Public methods --- */

	/**
	 * Create new Properties request.
	 * 
	 * @param orgToken WhiteSource organization token.
	 * 
	 * @return Newly created request for properties.
	 */
	public PropertiesRequest newPropertiesRequest(String orgToken) {
		PropertiesRequest request = new PropertiesRequest(orgToken);
		prepareRequest(request);
		
		return request;
	}
	
	/**
	 * Create new Report request.
	 * 
	 * @param dependencies List of dependencies for analysis.
	 * 
	 * @return Newly created request for analysis.
	 */
	public ReportRequest newReportRequest(Collection<DependencyInfo> dependencies) {
		ReportRequest request  = new ReportRequest(dependencies);
		prepareRequest(request);
		
		return request;
	}
	
	/**
	 * Create new Inventory Update request.
	 * 
	 * @param orgToken WhiteSource organization token.
	 * @param projects Projects status statement to update.
	 * 
	 * @return Newly created request to update organization inventory.
	 */
	public UpdateInventoryRequest newUpdateInventoryRequest(String orgToken, Collection<AgentProjectInfo> projects) {
		UpdateInventoryRequest request = new UpdateInventoryRequest(orgToken, projects);
		prepareRequest(request);
		
		return request;
	}
	
	/* --- Protected methods --- */
	
	/**
	 * The method populate the given request with basic information.
	 * 
	 * @param request Service request to prepare.
	 */
	protected void prepareRequest(BaseRequest<?> request) {
		request.setAgent(agent);
		request.setAgentVersion(agentVersion);
	}
	
}
