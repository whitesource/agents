package org.whitesource.agent.api.model.contribution;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * This object represents the code contributions made in a single repository.
 *
 * @author tom.shapira
 */
public class ContributionInfo implements Serializable {

    private static final long serialVersionUID = 6052569974965476656L;

    private String repository;
    private Collection<ContributingDeveloperInfo> contributingDevelopers;

    public ContributionInfo() {
        contributingDevelopers = new LinkedList<>();
    }

    public ContributionInfo(String repository) {
        this.repository = repository;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public Collection<ContributingDeveloperInfo> getContributingDevelopers() {
        return contributingDevelopers;
    }

    public void setContributingDevelopers(Collection<ContributingDeveloperInfo> contributingDevelopers) {
        this.contributingDevelopers = contributingDevelopers;
    }
}