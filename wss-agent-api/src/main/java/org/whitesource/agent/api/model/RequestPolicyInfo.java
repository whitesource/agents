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
package org.whitesource.agent.api.model;

import com.google.gson.annotations.Since;
import org.whitesource.agent.api.AgentApiVersion;

import java.io.Serializable;

/**
 * Info object describing a policy for inventory requests.
 *
 * @author Edo.Shor
 * @since 1.2.0
 */
public class RequestPolicyInfo implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 6475167885231211089L;

    /* --- Members --- */

    private String displayName;

    private String filterType;

    private String filterLogic;

    private String actionType;

    private String actionLogic;

    private boolean projectLevel;

    private boolean inclusive;

    @Since(AgentApiVersion.AGENT_API_VERSION_2_9_8)
    private String policyLevel;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public RequestPolicyInfo() {

    }

    /**
     * Constructor
     *
     * @param displayName
     */
    public RequestPolicyInfo(String displayName) {
        this.displayName = displayName;
    }

    /* --- Overridden methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestPolicyInfo)) return false;

        RequestPolicyInfo that = (RequestPolicyInfo) o;

        if (projectLevel != that.projectLevel) return false;
        if (inclusive != that.inclusive) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (filterType != null ? !filterType.equals(that.filterType) : that.filterType != null) return false;
        if (filterLogic != null ? !filterLogic.equals(that.filterLogic) : that.filterLogic != null) return false;
        if (actionType != null ? !actionType.equals(that.actionType) : that.actionType != null) return false;
        if (policyLevel != null ? !policyLevel.equals(that.policyLevel) : that.policyLevel != null) return false;
        return actionLogic != null ? actionLogic.equals(that.actionLogic) : that.actionLogic == null;

    }

    @Override
    public int hashCode() {
        int result = displayName != null ? displayName.hashCode() : 0;
        result = 31 * result + (filterType != null ? filterType.hashCode() : 0);
        result = 31 * result + (filterLogic != null ? filterLogic.hashCode() : 0);
        result = 31 * result + (actionType != null ? actionType.hashCode() : 0);
        result = 31 * result + (actionLogic != null ? actionLogic.hashCode() : 0);
        result = 31 * result + (policyLevel != null ? policyLevel.hashCode() : 0);
        result = 31 * result + (projectLevel ? 1 : 0);
        result = 31 * result + (inclusive ? 1 : 0);
        return result;
    }

    /* --- Getters / Setters --- */

    public String getPolicyLevel() {
        return policyLevel;
    }

    public void setPolicyLevel(String policyLevel) {
        this.policyLevel = policyLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterLogic() {
        return filterLogic;
    }

    public void setFilterLogic(String filterLogic) {
        this.filterLogic = filterLogic;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionLogic() {
        return actionLogic;
    }

    public void setActionLogic(String actionLogic) {
        this.actionLogic = actionLogic;
    }

    public boolean isProjectLevel() {
        return projectLevel;
    }

    public void setProjectLevel(boolean projectLevel) {
        this.projectLevel = projectLevel;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }
}
