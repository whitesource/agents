package org.whitesource.agent.api.model.contribution;

import java.io.Serializable;

/**
 * This object represents the contribution of a developer in a single repository.
 *
 * @author tom.shapira
 */
public class ContributingDeveloperInfo implements Serializable {

    private static final long serialVersionUID = 8797583403121221703L;

    private String email;
    private int commits;

    public ContributingDeveloperInfo() {
    }

    public ContributingDeveloperInfo(String email, int commits) {
        this.email = email;
        this.commits = commits;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCommits() {
        return commits;
    }

    public void setCommits(int commits) {
        this.commits = commits;
    }
}