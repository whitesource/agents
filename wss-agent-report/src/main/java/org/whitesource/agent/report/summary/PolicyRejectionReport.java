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
import java.util.ArrayList;
import java.util.Collection;

/**
 * POJO for holding all information about rejected libraries and policies.
 *
 * @author tom.shapira
 * @since 2.3.2
 */
public class PolicyRejectionReport implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -2814747793458938516L;

    /* --- Members --- */

    private Collection<RejectingPolicy> rejectingPolicies;
    private PolicyRejectionSummary summary;

    /* --- Constructors --- */

    public PolicyRejectionReport() {
        rejectingPolicies = new ArrayList<RejectingPolicy>();
        summary = new PolicyRejectionSummary();
    }

    /* --- Getters / Setters --- */

    public Collection<RejectingPolicy> getRejectingPolicies() {
        return rejectingPolicies;
    }

    public void setRejectingPolicies(Collection<RejectingPolicy> policies) {
        this.rejectingPolicies = policies;
    }

    public PolicyRejectionSummary getSummary() {
        return summary;
    }

    public void setSummary(PolicyRejectionSummary summary) {
        this.summary = summary;
    }
}
