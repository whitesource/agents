package org.whitesource.agent.api.dispatch;

import org.whitesource.agent.api.model.ProjectEvents;

import java.io.Serializable;
import java.util.Collection;

public class AsyncCheckPolicyComplianceStatusResult implements Serializable {

    /* --- Static Members --- */

    private static final long serialVersionUID = -2767549958986147436L;

    /* --- Members --- */

    // stores the sha1 of the euaDep.json used to calculate the vulnerabilities
    private String status;
    private Collection<ProjectEvents> projectEvents;

    /**
     * Default constructor
     */
    public AsyncCheckPolicyComplianceStatusResult() {
    }

    public AsyncCheckPolicyComplianceStatusResult(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<ProjectEvents> getProjectEvents() {
        return projectEvents;
    }

    public void setProjectEvents(Collection<ProjectEvents> projectEvents) {
        this.projectEvents = projectEvents;
    }
}
