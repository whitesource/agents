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
 * Request to get in-house rules from WhiteSource server.
 * 
 * @author tom.shapira
 *
 * @since 1.3.0
 */
public class GetInHouseRulesRequest extends BaseRequest<GetInHouseRulesResult> {

    /* --- Static members --- */

    private static final long serialVersionUID = -7706915950401595008L;

	/* --- Constructors --- */

    /**
     * Default constructor
     */
    public GetInHouseRulesRequest() {
        super(RequestType.GET_IN_HOUSE_RULES);
    }

    /**
     * Constructor
     *
     * @param orgToken WhiteSource organization token.
     */
    public GetInHouseRulesRequest(String orgToken) {
        this();
        this.orgToken = orgToken;
    }

}
