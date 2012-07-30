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
package org.whitesource.agent.maven.plugin;

import org.apache.maven.project.MavenProject;

public final class InputValidator {
	
	/* --- Static members --- */
	
	private static final String STANDALONE_POM_GROUP_ID = "org.apache.maven";
	
	private static final String STANDALONE_POM_ARTIFACT_ID = "standalone-pom";
	
	private static final String STANDALONE_POM_VERSION = "1";
	
	/* --- Static methods --- */
	
	/**
	 * Checks if the plugin was run in folder without pom file.
	 * 
	 * @param project The maven project. 
	 * @return True if the folder is not a maven project folder, false o/w
	 */
	public static boolean isStandAlonePom(MavenProject project) {
		return project.getArtifactId().equals(STANDALONE_POM_ARTIFACT_ID) && 
			project.getGroupId().equals(STANDALONE_POM_GROUP_ID) &&
			project.getVersion().equals(STANDALONE_POM_VERSION);
	}
	
/* --- Constructors --- */
	
	/**
	 * Private default constructor
	 */
	private InputValidator() {
		// avoid instantiation
	}
}