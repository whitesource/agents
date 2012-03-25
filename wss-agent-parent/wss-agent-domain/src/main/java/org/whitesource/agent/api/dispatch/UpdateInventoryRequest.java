package org.whitesource.agent.api.dispatch;

import java.util.ArrayList;
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

	private Collection<AgentProjectInfo> projects;

	/* --- Constructors --- */

	/**
	 * Default constructor
	 */
	public UpdateInventoryRequest() {
		this(null, new ArrayList<AgentProjectInfo>());
	}
	
	/**
	 * Constructor
	 * 
	 * @param token
	 * @param projects
	 */
	public UpdateInventoryRequest(String token, Collection<AgentProjectInfo> projects) {
		super(RequestType.UPDATE, token);
		
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
