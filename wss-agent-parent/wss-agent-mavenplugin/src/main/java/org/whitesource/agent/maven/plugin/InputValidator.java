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