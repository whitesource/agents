package com.wss.agent.model;

import java.io.Serializable;

/**
 * WhiteSource model for exclusion of transitive dependencies. 
 * 
 * @author tom.shapira
 */
public class ExclusionInfo implements Serializable {

	/* --- Static members --- */
	
	private static final long serialVersionUID = -3900555692407177279L;

	/* --- Members --- */
	
	private String artifactId;
	
	private String groupId;
	
	/* --- Constructors --- */
	
	/**
	 * Default constructor
	 */
	public ExclusionInfo() {
		
	}

	/**
	 * Constructor
	 * 
	 * @param artifactId
	 * @param groupId
	 */
	public ExclusionInfo(String artifactId, String groupId) {
		this.artifactId = artifactId;
		this.groupId = groupId;
	}

	/* --- Overridden methods --- */
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("ExclusionInfo@").append(Integer.toHexString(hashCode()))
			.append("[")
			.append("groupId= ").append(groupId).append(",")
			.append("artifactId= ").append(artifactId)
			.append(" ]");
		
		return sb.toString();
	}
	
	/* --- Getters / Setters --- */
	
	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
