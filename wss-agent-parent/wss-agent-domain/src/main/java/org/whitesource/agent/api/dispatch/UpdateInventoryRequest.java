package org.whitesource.agent.api.dispatch;

import java.util.Collection;

import org.whitesource.agent.api.model.AgentProjectInfo;

/**
 * Request to update organization inventory. 
 * 
 * @author tom.shapira
 */
public class UpdateInventoryRequest extends BaseRequest<UpdateInventoryResult> {

	/* --- Static members --- */
	
	private static final long serialVersionUID = 7731258010033962980L;
	
	/* --- Members --- */

	protected Collection<AgentProjectInfo> projects;

	/* --- Constructors --- */

	/**
	 * Constructor
	 * 
	 * @param orgToken WhiteSource organization token.
	 * @param projects Projects status statement to update.
	 */
	public UpdateInventoryRequest(String orgToken, Collection<AgentProjectInfo> projects) {
		super(RequestType.UPDATE);
		
		this.orgToken = orgToken;
		this.projects = projects;
	}

	/* --- Getters / Setters --- */

	public Collection<AgentProjectInfo> getProjects() {
		return projects;
	}

	public void setProjects(Collection<AgentProjectInfo> projects) {
		this.projects = projects;
	}

}
