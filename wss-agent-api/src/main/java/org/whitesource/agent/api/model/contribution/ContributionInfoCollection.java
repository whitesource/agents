package org.whitesource.agent.api.model.contribution;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This object represents a collection of code contributions made in a single repository.
 *
 * @author tom.shapira
 */
public class ContributionInfoCollection extends LinkedList<ContributionInfo> {

    private static final long serialVersionUID = -5141569905340706621L;

    public ContributionInfoCollection() {
    }

    public ContributionInfoCollection(Collection<? extends ContributionInfo> c) {
        super(c);
    }
}