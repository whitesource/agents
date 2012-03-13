package com.wss.agent.api;

import java.util.Collection;

import com.wss.agent.exception.JsonParsingException;
import com.wss.agent.utils.JsonUtils;

public class UpdateInventoryResult {
	
	/* --- Members --- */
	
	private String domainName;
	
	private Collection<String> updatedProjects;
	
	/* --- Constructors --- */
	
	/**
	 * Default constructor (for JSON parsing)
	 * 
	 */
	public UpdateInventoryResult() {
	}
	
	/**
	 * Constructor
	 * 
	 * @param domainName Name of the domain.
	 * @param updatedProjectsMap Map of updated projects and the name approver.
	 */
	public UpdateInventoryResult(String domainName, Collection<String> updatedProjects) {
		this.domainName = domainName;
		this.updatedProjects = updatedProjects;
	}

	/* --- Static methods --- */
	
	public static UpdateInventoryResult fromJSON(String json) throws JsonParsingException {
		return JsonUtils.fromJson(json, UpdateInventoryResult.class);
	}

	/* --- Getters / Setters --- */ 
	
	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Collection<String> getUpdatedProjects() {
		return updatedProjects;
	}

	public void setUpdatedProjects(Collection<String> updatedProjects) {
		this.updatedProjects = updatedProjects;
	}

}