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
import org.whitesource.agent.api.model.CloudNativeInfo;

import java.io.Serializable;
import java.util.Collection;

/**
 * Result of the get cloud native vulnerabilities check.
 * 
 * @author sami salami
 *
 * @since 2.9.9.81
 */
public class GetCloudNativeVulnerabilitiesRequest extends BaseRequest<CheckVulnerabilitiesResult> {

    /* --- Static members --- */

    private static final long serialVersionUID = -3031495415514845777L;

	/* --- Members --- */


	/* --- Constructors --- */

	/**
	 * Default constructor
	 */
	public GetCloudNativeVulnerabilitiesRequest() {
		super(RequestType.GET_CLOUD_NATIVE_VULNERABILITIES);
	}

	/**
	 * Constructor
	 *
	 * @param projects Open Source usage statement to check vulnerabilities.
	 */
	public GetCloudNativeVulnerabilitiesRequest(Collection<AgentProjectInfo> projects) {
		this();
		this.projects = projects;
	}

}