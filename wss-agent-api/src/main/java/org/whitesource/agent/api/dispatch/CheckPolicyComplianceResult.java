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

/**
 * Result of the check policies operation.
 * 
 * @author anna.rozin
 *
 * @since 2.2.7
 */
public class CheckPolicyComplianceResult extends BaseCheckPoliciesResult {

    /* --- Static members --- */

    private static final long serialVersionUID = -3031495415514845777L;

	/* --- Constructors --- */

	/**
	 * Default constructor
	 */
	public CheckPolicyComplianceResult() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param organization Name of the domain.
	 */
	public CheckPolicyComplianceResult(String organization) {
		super(organization);
	}
}