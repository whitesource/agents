package org.whitesource.agent.api.dispatch;

import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.ScanSummaryInfo;

import java.util.Collection;

/**
 * Request to check policies application results before actual inventory update inventory.
 * The decision if check the policies for all the inventory or just the new data is made by the user.
 * <p>
 * Created by anna.rozin
 */
public class CheckPolicyComplianceRequest extends BaseRequest<CheckPolicyComplianceResult> {

    /* --- Static members --- */

    private static final long serialVersionUID = -8257797945728036283L;

    /* --- Members --- */

    /**
     * When set to true, check that all dependencies sent to WhiteSource comply with organization policies.
     * When set to false, check that the added dependencies sent to WhiteSource comply with organization policies.
     */
    protected boolean forceCheckAllDependencies;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public CheckPolicyComplianceRequest() {
        super(RequestType.CHECK_POLICY_COMPLIANCE);
        forceCheckAllDependencies = false;
    }

    /**
     * Constructor
     *
     * @param projects                  Open Source usage statement to check against policies.
     * @param forceCheckAllDependencies Check policies for the all the Inventory or just for the new one.
     */
    public CheckPolicyComplianceRequest(Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies) {
        this();
        this.projects = projects;
        this.forceCheckAllDependencies = forceCheckAllDependencies;
    }

    /**
     * Constructor
     *
     * @param orgToken                  WhiteSource organization token.
     * @param projects                  Open Source usage statement to check against policies.
     * @param forceCheckAllDependencies Check policies for the all the Inventory or just for the new one.
     */
    public CheckPolicyComplianceRequest(String orgToken, Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies) {
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
    public CheckPolicyComplianceRequest(String orgToken, String product, String productVersion, Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies,
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
    public CheckPolicyComplianceRequest(String orgToken, String product, String productVersion, Collection<AgentProjectInfo> projects, boolean forceCheckAllDependencies,
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

    /* --- Getters / Setters --- */


    public boolean isForceCheckAllDependencies() {
        return forceCheckAllDependencies;
    }

    public void setForceCheckAllDependencies(boolean forceCheckAllDependencies) {
        this.forceCheckAllDependencies = forceCheckAllDependencies;
    }
}
