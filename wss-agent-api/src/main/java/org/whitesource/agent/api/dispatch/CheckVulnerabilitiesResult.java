package org.whitesource.agent.api.dispatch;

import org.whitesource.agent.api.model.ResourceInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Result for {@link CheckVulnerabilitiesRequest}.
 *
 * @author ruslan.gogerman
 *
 * @since 2.5.5
 */
public class CheckVulnerabilitiesResult implements Serializable {

    /* --- Static Members --- */

    private static final long serialVersionUID = - 6580191828869055056L;

    /* --- Members --- */

    // TODO: 1/11/2018 Use other element ?
    private Collection<ResourceInfo> resourceInfos;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public CheckVulnerabilitiesResult() {
        resourceInfos = new ArrayList<ResourceInfo>();
    }

    /* --- Getters / Setters --- */

    public Collection<ResourceInfo> getResourceInfos() {
        return resourceInfos;
    }

    public void setResourceInfos(Collection<ResourceInfo> resourceInfos) {
        this.resourceInfos = resourceInfos;
    }
}
