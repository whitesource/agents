package org.whitesource.agent.api.dispatch;

import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.ScanSummaryInfo;

import java.util.Collection;

public class AsyncCheckPolicyComplianceRequest extends BaseRequest<AsyncCheckPolicyComplianceResult> {

    /* --- Static Members --- */

    private static final long serialVersionUID = 925625972820830788L;

    /* --- Members --- */

    /**
     * When set to true, check that all dependencies sent to WhiteSource comply with organization policies.
     * When set to false, check that the added dependencies sent to WhiteSource comply with organization policies.
     */
    protected boolean forceCheckAllDependencies;
    /**
     * When set to true, get vulnerabilities for all dependencies without updating project.
     * When set to false, will not get vulnerabilities for all dependencies.
     */
    protected boolean populateVulnerabilities;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public AsyncCheckPolicyComplianceRequest() {
        super(RequestType.ASYNC_CHECK_POLICY_COMPLIANCE);
        forceCheckAllDependencies = false;
        populateVulnerabilities = false;
    }

    /**
     * Constructor
     *
     * @param projects                  Open Source usage statement to check against policies.
     * @param forceCheckAllDependencies Check policies for the all the Inventory or just for the new one.
     */
    public AsyncCheckPolicyComplianceRequest(Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies) {
        this();
        this.projects = projects;
        this.forceCheckAllDependencies = forceCheckAllDependencies;
    }

    /**
     * Constructor
     *
     * @param projects                  Open Source usage statement to check against policies.
     * @param forceCheckAllDependencies Check policies for the all the Inventory or just for the new one.
     * @param populateVulnerabilities   get vulnerabilities for each dependency without updating project
     */
    public AsyncCheckPolicyComplianceRequest(Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies, boolean populateVulnerabilities) {
        this();
        this.projects = projects;
        this.forceCheckAllDependencies = forceCheckAllDependencies;
        this.populateVulnerabilities = populateVulnerabilities;
    }

    /**
     * Constructor
     *
     * @param orgToken                  WhiteSource organization token.
     * @param projects                  Open Source usage statement to check against policies.
     * @param forceCheckAllDependencies Check policies for the all the Inventory or just for the new one.
     * @param populateVulnerabilities   get vulnerabilities for each dependency without updating project
     */
    public AsyncCheckPolicyComplianceRequest(String orgToken, Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies, boolean populateVulnerabilities) {
        this(projects, forceCheckAllDependencies, populateVulnerabilities);
        this.orgToken = orgToken;

    }

    /**
     * Constructor
     *
     * @param orgToken                  WhiteSource organization token.
     * @param projects                  Open Source usage statement to check against policies.
     * @param forceCheckAllDependencies Check policies for the all the Inventory or just for the new one.
     */
    public AsyncCheckPolicyComplianceRequest(String orgToken, Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies) {
        this(projects, forceCheckAllDependencies);
        this.orgToken = orgToken;
    }

    /**
     * Constructor
     *
     * @param orgToken                  Organization token uniquely identifying the account at white source.
     * @param product                   The product name or token to update.
     * @param productVersion            The product version.
     * @param projects                  Open Source usage statement to check against policies.
     * @param forceCheckAllDependencies Boolean to check new data only or not.
     * @param userKey                   user key uniquely identifying the account at white source.
     * @param requesterEmail            Email of the WhiteSource user that requests to update WhiteSource.
     * @param logData                   list of FSA's log data events
     * @param productToken              The product token
     */
    public AsyncCheckPolicyComplianceRequest(String orgToken, String product, String productVersion, Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies,
                                        String userKey, String requesterEmail, String logData, String productToken) {
        this(orgToken, projects, forceCheckAllDependencies);
        this.product = product;
        this.productVersion = productVersion;
        this.userKey = userKey;
        this.requesterEmail = requesterEmail;
        this.logData = logData;
        this.productToken = productToken;
    }

    /**
     * Constructor
     *
     * @param orgToken                  Organization token uniquely identifying the account at white source.
     * @param product                   The product name or token to update.
     * @param productVersion            The product version.
     * @param projects                  Open Source usage statement to check against policies.
     * @param forceCheckAllDependencies Boolean to check new data only or not.
     * @param userKey                   user key uniquely identifying the account at white source.
     * @param requesterEmail            Email of the WhiteSource user that requests to update WhiteSource.
     * @param logData                   list of FSA's log data events
     * @param productToken              The product token
     * @param scanSummaryInfo           Summary statistics for each step in Unified Agent
     */
    public AsyncCheckPolicyComplianceRequest(String orgToken, String product, String productVersion, Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies,
                                        String userKey, String requesterEmail, String logData, String productToken, ScanSummaryInfo scanSummaryInfo) {
        this(orgToken, projects, forceCheckAllDependencies);
        this.product = product;
        this.productVersion = productVersion;
        this.userKey = userKey;
        this.requesterEmail = requesterEmail;
        this.logData = logData;
        this.productToken = productToken;
        this.scanSummaryInfo = scanSummaryInfo;
    }

    /**
     * Constructor
     *
     * @param orgToken                  Organization token uniquely identifying the account at white source.
     * @param product                   The product name or token to update.
     * @param productVersion            The product version.
     * @param projects                  Open Source usage statement to check against policies.
     * @param forceCheckAllDependencies Boolean to check new data only or not.
     * @param userKey                   user key uniquely identifying the account at white source.
     * @param requesterEmail            Email of the WhiteSource user that requests to update WhiteSource.
     * @param logData                   list of FSA's log data events
     * @param productToken              The product token
     * @param scanSummaryInfo           Summary statistics for each step in Unified Agent
     * @param populateVulnerabilities   get vulnerabilities for each dependency without updating project
     */
    public AsyncCheckPolicyComplianceRequest(String orgToken, String product, String productVersion, Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies,
                                        boolean populateVulnerabilities, String userKey, String requesterEmail, String logData, String productToken, ScanSummaryInfo scanSummaryInfo) {
        this(orgToken, projects, forceCheckAllDependencies);
        this.product = product;
        this.productVersion = productVersion;
        this.userKey = userKey;
        this.requesterEmail = requesterEmail;
        this.logData = logData;
        this.productToken = productToken;
        this.scanSummaryInfo = scanSummaryInfo;
        this.populateVulnerabilities = populateVulnerabilities;
    }

    /* --- Getters / Setters --- */

    public boolean isForceCheckAllDependencies() {
        return forceCheckAllDependencies;
    }

    public void setForceCheckAllDependencies(boolean forceCheckAllDependencies) {
        this.forceCheckAllDependencies = forceCheckAllDependencies;
    }

    public boolean isPopulateVulnerabilities() {
        return populateVulnerabilities;
    }

    public void setPopulateVulnerabilities(boolean populateVulnerabilities) {
        this.populateVulnerabilities = populateVulnerabilities;
    }
}
