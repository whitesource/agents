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

    /* --- Getters / Setters --- */

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

}
