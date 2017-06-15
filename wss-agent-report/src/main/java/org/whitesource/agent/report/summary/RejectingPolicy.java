/**
 * Copyright (C) 2016 White Source Ltd.
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
package org.whitesource.agent.report.summary;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * POJO for holding the summarized policy rejection information about a rejecting policy.
 *
 * @author tom.shapira
 * @since 2.3.2
 */
public class RejectingPolicy implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 1062040498656193399L;

    /* --- Members --- */

    private String policyName;
    private String filterType;
    private boolean productLevel;
    private boolean inclusive;
    private Set<RejectedLibrary> rejectedLibraries;

    /* --- Constructors --- */

    public RejectingPolicy() {
        rejectedLibraries = new HashSet<RejectedLibrary>();
    }

    public RejectingPolicy(String policyName, String filterType, boolean productLevel, boolean inclusive) {
        this();
        this.policyName = policyName;
        this.filterType = filterType;
        this.productLevel = productLevel;
        this.inclusive = inclusive;
    }

    /* --- Getters / Setters --- */

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public boolean isProductLevel() {
        return productLevel;
    }

    public void setProductLevel(boolean productLevel) {
        this.productLevel = productLevel;
    }

    public Set<RejectedLibrary> getRejectedLibraries() {
        return rejectedLibraries;
    }

    public void setRejectedLibraries(Set<RejectedLibrary> rejectedLibraries) {
        this.rejectedLibraries = rejectedLibraries;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }
}
