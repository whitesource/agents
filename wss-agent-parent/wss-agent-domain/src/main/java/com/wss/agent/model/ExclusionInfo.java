package com.wss.agent.model;

/**
 * WSS model for exclusion of transitive dependencies. 
 * 
 * @author tom.shapira
 *
 */
public class ExclusionInfo {

	/* --- Members --- */
	
	private String artifactId;
	
	private String groupId;
	
	/* --- Overridden methods --- */
	
	@Override
	public String toString() {
		return groupId + ":" + artifactId;
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
