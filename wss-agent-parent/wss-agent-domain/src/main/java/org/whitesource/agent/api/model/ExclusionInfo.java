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
