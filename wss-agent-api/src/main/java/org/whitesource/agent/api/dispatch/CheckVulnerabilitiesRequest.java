package org.whitesource.agent.api.dispatch;


import org.whitesource.agent.api.model.AgentProjectInfo;

import java.util.Collection;

/**
 * @author ruslan.gogerman
 * @since 2.5.5
 */
public class CheckVulnerabilitiesRequest extends BaseRequest<CheckVulnerabilitiesResult> {

    /* --- Static Members --- */

    private static final long serialVersionUID = -945532206429866395L;

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
     * @param projects Open Source usage statement to check vulnerabilities.
     */
    public CheckVulnerabilitiesRequest(Collection<AgentProjectInfo> projects) {
        this();
        this.projects = projects;
    }

    /**
     * Constructor
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Open Source usage statement to check vulnerabilities.
     */
    public CheckVulnerabilitiesRequest(String orgToken, Collection<AgentProjectInfo> projects) {
        this();
        this.orgToken = orgToken;
        this.projects = projects;
    }

    /**
     * Constructor
     *
     * @param orgToken       WhiteSource organization token.
     * @param product        The product name or token to update.
     * @param productVersion The product version.
     * @param projects       Open Source usage statement to check vulnerabilities.
     */
    public CheckVulnerabilitiesRequest(String orgToken, String product, String productVersion, Collection<AgentProjectInfo> projects) {
        this(orgToken, projects);
        this.product = product;
        this.productVersion = productVersion;

    }
}