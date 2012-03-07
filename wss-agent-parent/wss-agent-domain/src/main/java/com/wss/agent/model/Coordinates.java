package com.wss.agent.model;

/**
 * Represents coordinates of a maven artifact
 * 
 * @author tom.shapira
 *
 */
public class Coordinates {
	
	/* --- Private members --- */
	
	private String groupId;
	
	private String artifactId;
	
	private String version;

	/* --- Overridden methods --- */
	
	@Override
	public String toString() {
		return groupId + ":" + artifactId + ":" + version;
	}
	
	/* --- Getters / Setters --- */ 
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
