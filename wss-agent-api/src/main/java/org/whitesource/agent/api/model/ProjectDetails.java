package org.whitesource.agent.api.model;

/**
 * @author eRez Huberman
 **/
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