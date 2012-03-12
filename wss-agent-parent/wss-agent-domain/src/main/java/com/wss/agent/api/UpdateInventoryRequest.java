package com.wss.agent.api;

import java.util.ArrayList;
import java.util.Collection;

import com.wss.agent.model.AgentProjectInfo;

/**
 * Represents the request sent to the server from the agent after converted to JSON 
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
