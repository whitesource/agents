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
 * Request to check policies application results before actual inventory update inventory.
 * 
 * @author Edo.Shor
 *
 * @since 1.2.0
 *
 * @deprecated Use {@link CheckPolicyComplianceRequest}.
 */
public class CheckPoliciesRequest extends BaseRequest<CheckPoliciesResult> {

    /* --- Static members --- */

    private static final long serialVersionUID = -6894332358170182935L;

	/* --- Members --- */

	protected Collection<AgentProjectInfo> projects;

	/* --- Constructors --- */

    /**
     * Default constructor
     */
    public CheckPoliciesRequest() {
        super(RequestType.CHECK_POLICIES);
    }

    /**
     * Constructor
     *
     * @param projects Open Source usage statement to check against policies.
     */
    public CheckPoliciesRequest(Collection<AgentProjectInfo> projects) {
        this();
        this.projects = projects;
    }

	/**
	 * Constructor
	 *
	 * @param orgToken WhiteSource organization token.
	 * @param projects Open Source usage statement to check against policies.
	 */
	public CheckPoliciesRequest(String orgToken, Collection<AgentProjectInfo> projects) {
		this(projects);
		this.orgToken = orgToken;
	}

}
