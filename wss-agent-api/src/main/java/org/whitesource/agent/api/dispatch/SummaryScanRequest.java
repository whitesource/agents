/**
 * Copyright (C) 2012 White Source Ltd.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whitesource.agent.api.dispatch;

import org.whitesource.agent.api.model.AgentProjectInfo;

import java.util.Collection;

/**
 * Request to update organization inventory.
 *
 * @author tom.shapira
 */
public class SummaryScanRequest extends BaseRequest<SummaryScanResult> {

    /* --- Static members --- */

    private static final long serialVersionUID = -276101415154446987L;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public SummaryScanRequest() {
        super(RequestType.SUMMARY_SCAN);
    }

    /**
     * Constructor
     *
     * @param projects Open Source usage statement to update White Source.
     */
    public SummaryScanRequest(Collection<AgentProjectInfo> projects) {
        this();
        this.projects = projects;
    }

    /**
     * Constructor
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Open Source usage statement to update White Source.
     */
    public SummaryScanRequest(String orgToken, Collection<AgentProjectInfo> projects) {
        this(projects);
        this.orgToken = orgToken;
    }

    /**
     * Constructor
     *
     * @param orgToken       Organization token uniquely identifying the account at white source.
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projects       Open Source usage statement to check against policies.
     */
    public SummaryScanRequest(String orgToken, String product, String productVersion, Collection<AgentProjectInfo> projects) {
        this(orgToken, projects);
        this.product = product;
        this.productVersion = productVersion;
    }
}
