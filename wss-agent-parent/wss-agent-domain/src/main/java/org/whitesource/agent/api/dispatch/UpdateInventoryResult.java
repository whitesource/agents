package org.whitesource.agent.api.dispatch;

import java.util.ArrayList;
import java.util.Collection;

import org.whitesource.agent.api.JsonParsingException;
import org.whitesource.agent.api.JsonUtils;


/**
 * Result of the update inventory operation. 
 * 
 * @author Edo.Shor
 */
public class UpdateInventoryResult {
	
	/* --- Members --- */
	
	private String organization;
	
	private Collection<String> updatedProjects;
	
	private Collection<String> createdProjects;
	
	/* --- Constructors --- */
	
	/**
	 * Default constructor (for JSON parsing)
	 * 
	 */
	public UpdateInventoryResult() {
		updatedProjects = new ArrayList<String>();
		createdProjects = new ArrayList<String>();
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

	public Collection<String> getCreatedProjects() {
		return createdProjects;
	}

	public void setCreatedProjects(Collection<String> createdProjects) {
		this.createdProjects = createdProjects;
	}

}