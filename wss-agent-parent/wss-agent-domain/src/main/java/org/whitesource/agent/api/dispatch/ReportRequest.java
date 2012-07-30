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

import org.whitesource.agent.api.model.DependencyInfo;

/**
 * Request for WhiteSource anonymous report.
 * 
 * @author tom.shapira
 */
public class ReportRequest extends BaseRequest<ReportResult> {

	/* --- Static members --- */
	
	private static final long serialVersionUID = 909591223876500674L;
	
	/* --- Members --- */
	
	protected Collection<DependencyInfo> dependencies;
	
	/* --- Constructors --- */
	
	/**
	 * Constructor
	 * 
	 * @param dependencies Dependencies for analysis.
	 */
	public ReportRequest(Collection<DependencyInfo> dependencies) {
		super(RequestType.REPORT);
		
		this.dependencies = dependencies;
	}
	
	/* --- Getters / Setters --- */
	
	public Collection<DependencyInfo> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Collection<DependencyInfo> dependencies) {
		this.dependencies = dependencies;
	}
	
}