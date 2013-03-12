/**
 * Copyright (C) 2012 White Source Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		return new Coordinates(groupId, artifactId, version).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) { return false; }

		DependencyInfo other = (DependencyInfo) obj;
		
		return (groupId == null) ? (other.groupId == null) : groupId.equals(other.groupId)
                && ((artifactId == null) ? (other.artifactId == null) : artifactId.equals(other.artifactId))
                && ((version == null) ? (other.version == null) : version.equals(other.version));
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
