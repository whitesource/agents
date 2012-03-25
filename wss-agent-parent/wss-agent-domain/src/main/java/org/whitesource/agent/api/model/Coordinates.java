package org.whitesource.agent.api.model;

import java.io.Serializable;

/**
 * WhiteSource model for artifact's coordinates. 
 * 
 * @author tom.shapira
 */
public class Coordinates implements Serializable {
	
	/* --- Static members --- */
	
	private static final long serialVersionUID = 4962222806117752812L;
	
	/* --- Members --- */
	
	private String groupId;
	
	private String artifactId;
	
	private String version;
	
	/* --- Constructors --- */

	/**
	 * Default constructor
	 */
	public Coordinates() {
		
	}

	/**
	 * Constructor
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 */
	public Coordinates(String groupId, String artifactId, String version) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}
	
	/* --- Overridden methods --- */
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Coordinates@").append(Integer.toHexString(hashCode()))
			.append("[")
			.append("groupId= ").append(groupId).append(",")
			.append("artifactId= ").append(artifactId).append(",")
			.append("version= ").append(version)
			.append(" ]");
		
		return sb.toString();
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
