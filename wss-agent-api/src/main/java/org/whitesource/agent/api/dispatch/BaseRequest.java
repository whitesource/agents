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

import java.util.*;

/**
 * Base, abstract, implementation of the interface.
 *
 * @param <R> Type of expected result.
 * @author Edo.Shor
 */
public abstract class BaseRequest<R> implements ServiceRequest<R> {

    /* --- Static members --- */

    private static final long serialVersionUID = 4691829529579651426L;

    /* --- Members --- */

    protected final RequestType type;

    protected String agent;

    protected String agentVersion;

    protected String pluginVersion;

    protected String orgToken;

    protected String userKey;

    protected String product;

    protected String productVersion;

    protected long timeStamp;

    protected String requesterEmail;

    protected Collection<AgentProjectInfo> projects;

    protected boolean aggregateModules;

    protected boolean preserveModuleStructure;

    protected String aggregateProjectName;

    protected String aggregateProjectToken;

    protected String logData;

    protected String scanComment;

    protected String productToken;

    protected Map<String, String> extraProperties;

    protected ScanSummaryInfo scanSummaryInfo;


    /* --- Constructors --- */

    /**
     * Constructor
     *
     * @param type Request operation type.
     */
    public BaseRequest(RequestType type) {
        this(type, null, null, null);
    }

    /**
     * Constructor
     *
     * @param type  Request operation type.
     * @param agent Agent type identifier.
     */
    public BaseRequest(RequestType type, String agent) {
        this(type, agent, null, null);
    }

    /**
     * Constructor
     *
     * @param type          Request operation type.
     * @param agent         Agent type identifier.
     * @param agentVersion  Agent version.
     * @param pluginVersion Plugin version.
     */
    public BaseRequest(RequestType type, String agent, String agentVersion, String pluginVersion) {
        this.type = type;
        this.agent = agent;
        this.agentVersion = agentVersion;
        this.pluginVersion = pluginVersion;
        this.timeStamp = System.currentTimeMillis();
        projects = new ArrayList<AgentProjectInfo>();
        this.extraProperties = new HashMap<>();
    }

    /* --- Interface implementation methods --- */

    @Override
    public RequestType type() {
        return type;
    }

    @Override
    public String orgToken() {
        return orgToken;
    }

    @Override
    public String agent() {
        return agent;
    }

    @Override
    public String agentVersion() {
        return agentVersion;
    }

    @Override
    public String pluginVersion() {
        return pluginVersion;
    }

    @Override
    public String product() {
        return product;
    }

    @Override
    public String productVersion() {
        return productVersion;
    }

    @Override
    public long timeStamp() {
        return timeStamp;
    }

    @Override
    public String requesterEmail() {
        return requesterEmail;
    }

    @Override
    public String userKey() {
        return userKey;
    }

    @Override
    public boolean aggregateModules() {
        return aggregateModules;
    }

    @Override
    public boolean preserveModuleStructure() {
        return preserveModuleStructure;
    }

    @Override
    public String aggregateProjectName() {
        return aggregateProjectName;
    }

    @Override
    public String aggregateProjectToken() {
        return aggregateProjectToken;
    }

    @Override
    public String logData() {
        return logData;
    }

    @Override
    public String scanComment() {
        return scanComment;
    }


    @Override
    public String productToken() {
        return productToken;
    }

    @Override
    public Map<String, String> extraProperties() {
        return extraProperties;
    }

    /* --- Getters / Setters --- */

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    public void setOrgToken(String orgToken) {
        this.orgToken = orgToken;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public Collection<AgentProjectInfo> getProjects() {
        return projects;
    }

    public void setProjects(Collection<AgentProjectInfo> projects) {
        this.projects = projects;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void setAggregateModules(boolean aggregateModules) {
        this.aggregateModules = aggregateModules;
    }

    public void setPreserveModuleStructure(boolean preserveModuleStructure) {
        this.preserveModuleStructure = preserveModuleStructure;
    }

    public void setAggregateProjectName(String aggregateProjectName) {
        this.aggregateProjectName = aggregateProjectName;
    }

    public void setAggregateProjectToken(String aggregateProjectToken) {
        this.aggregateProjectToken = aggregateProjectToken;
    }

    public void setLogData(String logData) {
        this.logData = logData;
    }

    public void setScanComment(String scanComment) {
        this.scanComment = scanComment;
    }

    public void setProductToken(String productToken) {
        this.productToken = productToken;
    }

    public Map<String, String> getExtraProperties() {
        return extraProperties;
    }

    public void setExtraProperties(Map<String, String> extraProperties) {
        this.extraProperties = extraProperties;
    }

    public ScanSummaryInfo getScanSummaryInfo() {
        return scanSummaryInfo;
    }

    public void setScanSummaryInfo(ScanSummaryInfo scanSummaryInfo) {
        this.scanSummaryInfo = scanSummaryInfo;
    }
}
