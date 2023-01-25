package org.whitesource.agent.api.dispatch;

import java.io.Serializable;

public class AsyncCheckPolicyComplianceResult extends BaseResult {

    /* --- Static Members --- */

    private static final long serialVersionUID = 7236121775887105115L;

    /* --- Members --- */

    private String identifier;

    /**
     * Default constructor
     */
    public AsyncCheckPolicyComplianceResult() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
