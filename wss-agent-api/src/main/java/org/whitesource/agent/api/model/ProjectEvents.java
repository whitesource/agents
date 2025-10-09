package org.whitesource.agent.api.model;

import java.io.Serializable;
import java.util.Collection;

public class ProjectEvents  implements Serializable {

    private static final long serialVersionUID = 3812672491893273094L;

    private String scanUuid;
    private String eventUuid;
    private String projectUuid;
    private String projectName;
    private String status;

    public ProjectEvents() {
    }

    public ProjectEvents(String scanUuid, String eventUuid, String projectUuid, String projectName, String status) {
        this.scanUuid = scanUuid;
        this.eventUuid = eventUuid;
        this.projectUuid = projectUuid;
        this.projectName = projectName;
        this.status = status;
    }

    public String getScanUuid() {
        return scanUuid;
    }

    public void setScanUuid(String scanUuid) {
        this.scanUuid = scanUuid;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
