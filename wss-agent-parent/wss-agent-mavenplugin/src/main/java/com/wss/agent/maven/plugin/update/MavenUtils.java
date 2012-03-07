package com.wss.agent.maven.plugin.update;

import java.util.ArrayList;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.project.MavenProject;

import com.wss.agent.model.Coordinates;
import com.wss.agent.model.DependencyInfo;
import com.wss.agent.model.ExclusionInfo;

/**
 * Utility class for getting {@link DependencyInfo} from {@link Dependency} 
 * 
 * @author tom.shapira
 *
 */
public class MavenUtils {
	
	/* --- Static methods --- */
	
	/**
	 * Get info model from {@link Dependency}
	 * 
	 */
	public static DependencyInfo getInfo(Dependency dependency) {
		DependencyInfo info = new DependencyInfo();
		
		// dependency data
		setDependencyData(dependency, info);
		
		// exclusions
		setExclusions(dependency, info);
		
		return info;
	}

	/**
	 * Get maven project coordinates
	 * 
	 * @param mavenProject Maven project
	 * @return Project coordinates
	 */
	public static Coordinates getCoordinates(MavenProject mavenProject) {
		Coordinates coordinates = new Coordinates();
		coordinates.setGroupId(mavenProject.getGroupId());
		coordinates.setArtifactId(mavenProject.getArtifactId());
		coordinates.setVersion(mavenProject.getVersion());
		
		return coordinates;
	}
	
	/* --- Private methods --- */
	
	/**
	 * Copy the data from {@link Dependency} to {@link DependencyInfo}
	 * 
	 */
	private static void setDependencyData(Dependency dependency, DependencyInfo info) {
		info.setGroupId(dependency.getGroupId());
		info.setArtifactId(dependency.getArtifactId());
		info.setVersion(dependency.getVersion());
		info.setScope(dependency.getScope());
		info.setClassifier(dependency.getClassifier());
		info.setOptional(dependency.isOptional());
		info.setType(dependency.getType());
		info.setSystemPath(dependency.getSystemPath());
	}
	
	/**
	 * Extract exclusions.
	 * 
	 */
	private static void setExclusions(Dependency dependency, DependencyInfo dependencyInfo) {
		ArrayList<ExclusionInfo> exclusions = new ArrayList<ExclusionInfo>();
		ExclusionInfo exclusionInfo;
		
		for (Object obj : dependency.getExclusions()) {
			if (obj instanceof Exclusion) {
				Exclusion exclusion = (Exclusion) obj;
				
				// set exclusion info
				exclusionInfo = new ExclusionInfo();
				exclusionInfo.setGroupId(exclusion.getGroupId());
				exclusionInfo.setArtifactId(exclusion.getArtifactId());
				
				// add to list
				exclusions.add(exclusionInfo);
			}
		}
		dependencyInfo.setExclusions(exclusions);
	}

}
