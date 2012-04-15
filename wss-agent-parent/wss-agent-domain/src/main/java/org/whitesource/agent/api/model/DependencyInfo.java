package org.whitesource.agent.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * WhiteSource Model for a project's dependency 
 * 
 * @author tom.shapira
 *
 */
public class DependencyInfo implements Serializable {

	/* --- Static members --- */

	private static final long serialVersionUID = -6212622409560068635L;

	/* --- Members --- */

	private String groupId;

	private String artifactId;

	private String version;

	private String type;

	private String classifier;

	private String scope;
	
	private String sha1;

	private String systemPath;

	private Collection<ExclusionInfo> exclusions;

	private boolean optional;

	/* --- Constructors --- */

	/**
	 * Default constructor
	 */
	public DependencyInfo() {
		exclusions = new ArrayList<ExclusionInfo>();
	}
	
	/**
	 * Constructor
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 */
	public DependencyInfo(String groupId, String artifactId, String version) {
		this();
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}

	/**
	 * Constructor
	 * 
	 * @param sha1
	 */
	public DependencyInfo(String sha1) {
		this();
		this.sha1 = sha1;
	}

	/* --- Overridden methods --- */

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("DependencyInfo@").append(Integer.toHexString(hashCode()))
			.append("[")
			.append("groupId= ").append(groupId).append(",")
			.append("artifactId= ").append(artifactId).append(",")
			.append("version= ").append(version)
			.append(" ]");

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int multiplier = 23;
		
		int code = 133;
		code = multiplier * code + ((groupId == null) ? 0 : groupId.hashCode());
		code = multiplier * code + ((artifactId == null) ? 0 : artifactId.hashCode());
		code = multiplier * code + ((version == null) ? 0 : version.hashCode());
		
		return code;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false; 
		}
	    if (obj == this) {
	    	return true; 
	    }
	    if (obj.getClass() != getClass()) {
	      return false;
	    }
	    
		DependencyInfo other = (DependencyInfo) obj;
		
		boolean isEqual = (groupId == null) ? (other.groupId == null) : groupId.equals(other.groupId);
		isEqual = isEqual && ((artifactId == null) ? (other.artifactId == null) : artifactId.equals(other.artifactId));
		isEqual = isEqual && ((version == null) ? (other.version == null) : version.equals(other.version));
		
		return isEqual;    
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public String getSystemPath() {
		return systemPath;
	}

	public void setSystemPath(String systemPath) {
		this.systemPath = systemPath;
	}

	public Collection<ExclusionInfo> getExclusions() {
		return exclusions;
	}

	public void setExclusions(Collection<ExclusionInfo> exclusions) {
		this.exclusions = exclusions;
	}

	public boolean getOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

}
