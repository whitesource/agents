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
import org.whitesource.agent.api.model.ScanSummaryInfo;

import java.util.Collection;

/**
 * Request to update organization inventory.
 *
 * @author tom.shapira
 */
public class UpdateInventoryRequest extends BaseRequest<UpdateInventoryResult> {

    /* --- Static members --- */

    private static final long serialVersionUID = 7731258010033962980L;

    private UpdateType updateType = UpdateType.OVERRIDE;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public UpdateInventoryRequest() {
        super(RequestType.UPDATE);
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
     * @param projects   Open Source usage statement to update White Source.
     * @param updateType Request UpdateType
     */
    public UpdateInventoryRequest(Collection<AgentProjectInfo> projects, UpdateType updateType) {
        this(projects);
        this.updateType = updateType;
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

    /**
     * Constructor
     *
     * @param orgToken   WhiteSource organization token.
     * @param projects   Open Source usage statement to update White Source.
     * @param updateType Request UpdateType
     */
    public UpdateInventoryRequest(String orgToken, Collection<AgentProjectInfo> projects, UpdateType updateType) {
        this(orgToken, projects);
        this.updateType = updateType;
    }


    /**
     * Constructor
     *
     * @param orgToken        Organization token uniquely identifying the account at white source.
     * @param requesterEmail  Email of the WhiteSource user that requests to update WhiteSource.
     * @param updateType      Request UpdateType
     * @param product         The product name or token to update.
     * @param productVersion  The product version.
     * @param productToken    The product token.
     * @param projects        OSS usage information to send to white source.
     * @param userKey         user key uniquely identifying the account at white source.
     * @param logData         list of FSA's log data events
     * @param scanComment     scan description
     */
    public UpdateInventoryRequest(String orgToken, String requesterEmail, UpdateType updateType, String product, String productVersion, Collection<AgentProjectInfo> projects,
                                  String userKey, String logData, String scanComment, String productToken) {
        this(orgToken, projects, updateType);
        this.requesterEmail = requesterEmail;
        this.product = product;
        this.productVersion = productVersion;
        this.userKey = userKey;
        this.logData = logData;
        this.scanComment = scanComment;
        this.productToken = productToken;
    }

    /**
     * Constructor
     *
     * @param orgToken        Organization token uniquely identifying the account at white source.
     * @param requesterEmail  Email of the WhiteSource user that requests to update WhiteSource.
     * @param updateType      Request UpdateType
     * @param product         The product name or token to update.
     * @param productVersion  The product version.
     * @param productToken    The product token.
     * @param projects        OSS usage information to send to white source.
     * @param userKey         user key uniquely identifying the account at white source.
     * @param logData         list of FSA's log data events
     * @param scanComment     scan description
     * @param scanSummaryInfo Summary statistics for each step in Unified Agent
     */
    public UpdateInventoryRequest(String orgToken, String requesterEmail, UpdateType updateType, String product, String productVersion, Collection<AgentProjectInfo> projects,
                                  String userKey, String logData, String scanComment, String productToken, ScanSummaryInfo scanSummaryInfo) {
        this(orgToken, projects, updateType);
        this.requesterEmail = requesterEmail;
        this.product = product;
        this.productVersion = productVersion;
        this.userKey = userKey;
        this.logData = logData;
        this.scanComment = scanComment;
        this.productToken = productToken;
        this.scanSummaryInfo = scanSummaryInfo;
    }

    /**
     * Constructor to create offline request
     *
     * @param orgToken        Organization token uniquely identifying the account at white source.
     * @param product         The product name or token to update.
     * @param productVersion  The product version.
     * @param projects        OSS usage information to send to white source.
     * @param userKey         user key uniquely identifying the account at white source.
     * @param scanComment     scan description
     */
    public UpdateInventoryRequest(String orgToken, String product, String productVersion, Collection<AgentProjectInfo> projects, String userKey, String scanComment) {
        this(orgToken, projects);
        this.product = product;
        this.productVersion = productVersion;
        this.userKey = userKey;
        this.scanComment = scanComment;
    }

    /**
     * Constructor to create offline request
     *
     * @param orgToken        Organization token uniquely identifying the account at white source.
     * @param product         The product name or token to update.
     * @param productVersion  The product version.
     * @param projects        OSS usage information to send to white source.
     * @param userKey         user key uniquely identifying the account at white source.
     * @param scanComment     scan description
     * @param scanSummaryInfo Summary statistics for each step in Unified Agent
     */
    public UpdateInventoryRequest(String orgToken, String product, String productVersion, Collection<AgentProjectInfo> projects, String userKey, String scanComment,
                                  ScanSummaryInfo scanSummaryInfo) {
        this(orgToken, projects);
        this.product = product;
        this.productVersion = productVersion;
        this.userKey = userKey;
        this.scanComment = scanComment;
        this.scanSummaryInfo = scanSummaryInfo;
    }



    /**
     * @return Request UpdateType
     */
    public UpdateType getUpdateType() {
        return updateType;
    }

    /**
     * @param updateType Request UpdateType
     */
    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }
}
