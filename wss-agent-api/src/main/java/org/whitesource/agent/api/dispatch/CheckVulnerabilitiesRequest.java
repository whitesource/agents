package org.whitesource.agent.api.dispatch;


import org.whitesource.agent.api.model.DependencyInfo;

import java.util.Collection;

/**
 * @author ruslan.gogerman
 *
 * @since 2.5.5
 */

public class CheckVulnerabilitiesRequest extends BaseRequest<CheckVulnerabilitiesRequest> {

    /* --- Static Members --- */

    private static final long serialVersionUID = - 945532206429866395L;

    /* --- Members --- */

    private Collection<DependencyInfo> dependencyInfos;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public CheckVulnerabilitiesRequest() {
        super(RequestType.CHECK_VULNERABILITIES);
    }

    /**
     * Constructor
     *
     * @param orgToken WhiteSource organization token.
     */
    public CheckVulnerabilitiesRequest(String orgToken, Collection<DependencyInfo> dependencyInfos) {
        this();
        this.orgToken = orgToken;
    }

    public Collection<DependencyInfo> getDependencyInfos() {
        return dependencyInfos;
    }

    public void setDependencyInfos(Collection<DependencyInfo> dependencyInfos) {
        this.dependencyInfos = dependencyInfos;
    }
}
