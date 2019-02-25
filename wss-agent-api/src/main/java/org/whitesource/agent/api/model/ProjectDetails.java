package org.whitesource.agent.api.model;

import com.google.gson.annotations.Since;
import org.whitesource.agent.api.AgentApiVersion;

/**
 * @author eRez Huberman
 * @since 2.9.8
 **/
@Since(AgentApiVersion.AGENT_API_VERSION_2_9_8)
public class ProjectDetails {

    /* --- Members --- */
    private String projectName;
    private Integer projectId;
    private String projectToken;

    /* --- Constructors --- */
    public ProjectDetails(String name, Integer id, String token){
        this.projectName = name;
        this.projectId = id;
        this.projectToken = token;
    }

    /* --- Getters / Setters --- */
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectToken() {
        return projectToken;
    }

    public void setProjectToken(String projectToken) {
        this.projectToken = projectToken;
    }
}