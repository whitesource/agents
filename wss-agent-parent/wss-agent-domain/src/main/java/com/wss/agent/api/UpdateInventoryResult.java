package com.wss.agent.api;

import java.util.ArrayList;
import java.util.Collection;

import com.wss.agent.exception.JsonParsingException;
import com.wss.agent.utils.JsonUtils;

/**
 * Result of the update inventory operation. 
 * 
 * @author Edo.Shor
 */
public class UpdateInventoryResult {
	
	/* --- Members --- */
	
	private String organization;
	
	private Collection<String> updatedProjects;
	
	/* --- Constructors --- */
	
	/**
	 * Default constructor (for JSON parsing)
	 * 
	 */
	public UpdateInventoryResult() {
		updatedProjects = new ArrayList<String>();
	}
	
	/**
	 * Constructor
	 * 
	 * @param organization Name of the domain.
	 */
	public UpdateInventoryResult(String organization) {
		this();
		this.organization = organization;
	}

	/* --- Static methods --- */
	
	public static UpdateInventoryResult fromJSON(String json) throws JsonParsingException {
		return JsonUtils.fromJson(json, UpdateInventoryResult.class);
	}

	/* --- Getters / Setters --- */ 

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	public Collection<String> getUpdatedProjects() {
		return updatedProjects;
	}

	public void setUpdatedProjects(Collection<String> updatedProjects) {
		this.updatedProjects = updatedProjects;
	}

}