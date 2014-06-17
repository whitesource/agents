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

import org.whitesource.agent.api.model.InHouseRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Result of the get in-house rules operation.
 * 
 * @author tom.shapira
 *
 * @since 1.3.0
 */
public class GetInHouseRulesResult implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -1726367964775914302L;

	/* --- Members --- */

    /**
     * Collection of {@link InHouseRule}.
     */
    private Collection<InHouseRule> inHouseRules;

	/* --- Constructors --- */

	/**
	 * Default constructor
	 */
	public GetInHouseRulesResult() {
        inHouseRules = new ArrayList<InHouseRule>();
	}

    /**
     * Constructor
     *
     * @param inHouseRules
     */
    public GetInHouseRulesResult(Collection<InHouseRule> inHouseRules) {
        this.inHouseRules = inHouseRules;
    }

    /* --- Getters / Setters --- */

    public Collection<InHouseRule> getInHouseRules() {
        return inHouseRules;
    }

    public void setInHouseRules(Collection<InHouseRule> inHouseRules) {
        this.inHouseRules = inHouseRules;
    }
}