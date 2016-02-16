package org.whitesource.agent.api.dispatch;

import org.whitesource.agent.api.model.AgentProjectInfo;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Request to check policies application results before actual inventory update inventory.
 * The decision if check the policies for all the inventory or just the new data is made by the user.
 *
 * Created by anna.rozin
 */
public class CheckPolicyComplianceRequest extends BaseRequest<CheckPolicyComplianceResult> {

    /* --- Static members --- */

    private static final long serialVersionUID = -8257797945728036283L;

    /* --- Members --- */

    protected Collection<AgentProjectInfo> projects;

    /**
     * When set to true, check that all dependencies sent to WhiteSource comply with organization policies.
     * When set to false, check that the added dependencies sent to WhiteSource comply with organization policies.
     */
    protected boolean allDependencies;

     /* --- Constructors --- */

    /**
     * Default constructor
     */
    public CheckPolicyComplianceRequest() {
        super(RequestType.CHECK_POLICY_COMPLIANCE);
        allDependencies = false;
    }

    /**
     * Constructor
     *
     * @param projects Open Source usage statement to check against policies.
     * @param allDependencies Check policies for the all the Inventory or just for the new one.
     */
    public CheckPolicyComplianceRequest(Collection<AgentProjectInfo> projects, boolean allDependencies) {
        this();
        this.projects = projects;
        this.allDependencies = allDependencies;
    }

    /**
     * Constructor
     *
     * @param orgToken WhiteSource organization token.
     * @param projects Open Source usage statement to check against policies.
     * @param allDependencies Check policies for the all the Inventory or just for the new one.
     */
    public CheckPolicyComplianceRequest(String orgToken, Collection<AgentProjectInfo> projects, boolean allDependencies) {
        this(projects, allDependencies);
        this.orgToken = orgToken;
    }

    /* --- Getters / Setters --- */


    public boolean isAllDependencies() {
        return allDependencies;
    }

    public void setAllDependencies(boolean allDependencies) {
        this.allDependencies = allDependencies;
    }


    @Override
    public Collection<AgentProjectInfo> getProjects() {
        return projects;
    }

    @Override
    public void setProjects(Collection<AgentProjectInfo> projects) {
        this.projects = projects;
    }
}
