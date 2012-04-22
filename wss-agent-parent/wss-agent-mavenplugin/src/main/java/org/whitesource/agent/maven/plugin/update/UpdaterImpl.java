package org.whitesource.agent.maven.plugin.update;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.aether.util.ChecksumUtils;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.dispatch.UpdateInventoryResult;
import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.Coordinates;
import org.whitesource.agent.api.model.DependencyInfo;
import org.whitesource.agent.api.model.ExclusionInfo;
import org.whitesource.agent.maven.plugin.Constants;
import org.whitesource.agent.maven.plugin.WssServiceProvider;
import org.whitesource.api.client.WssServiceException;


/**
 * Implementation class of {@link Updater}
 * 
 * @author tom.shapira
 *
 */
public class UpdaterImpl implements Updater {

	/* --- Members --- */

	private Log log;

	private Properties properties;

	private String orgToken;

	private Collection<MavenProject> projects;

	/* --- Constructors --- */

	/**
	 * Creates a new {@link UpdaterImpl}
	 * 
	 */
	public UpdaterImpl(Properties properties, String orgToken, Collection<MavenProject> projects) {
		this.properties = properties;
		this.orgToken = orgToken;
		this.projects = projects;
	}

	/* --- Concrete implementation methods --- */

	public UpdateInventoryResult update() throws MojoExecutionException {
		UpdateInventoryResult result = null;

		// analyze projects
		Collection<AgentProjectInfo> projectInfos = setProjects(projects);

		// create update request
		UpdateInventoryRequest request = WssServiceProvider.instance().requestFactory().newUpdateInventoryRequest(orgToken, projectInfos);

		logDebug(Constants.DEBUG_REQUEST_BUILT);

		// send request
		try {
			result = WssServiceProvider.instance().provider().updateInventory(request);
		} catch (WssServiceException e) {
			throw new MojoExecutionException("Error updating inventory", e);
		}

		return result;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	/* --- Private methods --- */

	/**
	 * Sets the top level project and all if its collected projects (in case this is a multi-module project) 
	 * 
	 */
	private Collection<AgentProjectInfo> setProjects(Collection<MavenProject> projects) 
			throws MojoExecutionException {
		Collection<AgentProjectInfo> projectInfos = new ArrayList<AgentProjectInfo>();

		// process all projects
		for (MavenProject project : projects) {
			projectInfos.add(processProject(project));
		}

		return projectInfos;
	}

	/**
	 * Sets the project coordinates and dependencies.
	 * 
	 * @param mavenProject Maven project.
	 */
	private AgentProjectInfo processProject(MavenProject mavenProject) {
		logDebug(Constants.DEBUG_FOUND_PROJECT + mavenProject.getArtifactId());

		AgentProjectInfo projectInfo = new AgentProjectInfo();
		populateProjectToken(projectInfo, mavenProject);
		populateCoordinates(projectInfo, mavenProject);
		populateDependencies(projectInfo, mavenProject);

		return projectInfo;
	}

	/**
	 * The method populate the project token parameter from the plugin configuration in the pom file.
	 * 
	 * @param projectInfo Project info model.
	 * @param project Maven project.
	 */
	private void populateProjectToken(AgentProjectInfo projectInfo, MavenProject project) {
		Plugin plugin = project.getPlugin(Constants.PLUGIN_KEY);
		if (plugin != null) {
			Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();
			if (configuration != null) {
				Xpp3Dom projectTokenParameter = configuration.getChild(Constants.PROJECT_TOKEN);
				if (projectTokenParameter != null) {
					projectInfo.setProjectToken(projectTokenParameter.getValue());
				}
			}
		}
	}

	/**
	 * The method populate the coordinates info.
	 * 
	 * @param projectInfo Project info model.
	 * @param project Maven project.
	 */
	private void populateCoordinates(AgentProjectInfo projectInfo, MavenProject project) {
		// project coordinates
		projectInfo.setCoordinates(extractCoordinates(project));

		// parent coordinates
		MavenProject parent = project.getParent();
		if (parent != null) {
			projectInfo.setParentCoordinates(extractCoordinates(parent));
		}
	}

	/**
	 * The method populate the dependencies info.
	 * 
	 * @param projectInfo Project info model.
	 * @param project Maven project.
	 */
	private void populateDependencies(AgentProjectInfo projectInfo, MavenProject project) {
		Collection<DependencyInfo> dependencyInfos = projectInfo.getDependencies();

		// create lookup table
		Map<Dependency, Artifact> lut = createLookupTable(project);

		// create WhiteSource API dependencies
		for (Dependency dependency : project.getDependencies()) {
			String sha1 = null;
			Artifact artifact = lut.get(dependency);
			if (artifact != null) {
				sha1 = calculateSha1(artifact);	
			}

			DependencyInfo dependencyInfo = getDependencyInfo(dependency);
			dependencyInfo.setSha1(sha1);
			dependencyInfos.add(dependencyInfo);
		}
	}

	private Map<Dependency, Artifact> createLookupTable(MavenProject project) {
		Map<Dependency, Artifact> lut = new HashMap<Dependency, Artifact>();

		for (Dependency dependency : project.getDependencies()) {
			for (Artifact dependencyArtifact : project.getDependencyArtifacts()) {
				if (match(dependency, dependencyArtifact)) {
					lut.put(dependency, dependencyArtifact);
				}
			}
		}

		return lut;
	}

	/**
	 * Compares groupId, artifactId, version and classifier of the given dependency and artifact.
	 * 
	 * @param dependency Maven dependency.
	 * @param artifact Maven artifact.
	 * 
	 * @return Whether of not the dependency and artifact have the same coordinates.
	 */
	private boolean match(Dependency dependency, Artifact artifact) {
		boolean match = dependency.getGroupId().equals(artifact.getGroupId()) &&
		dependency.getArtifactId().equals(artifact.getArtifactId()) &&
		dependency.getVersion().equals(artifact.getVersion());

		if (match) {
			if (dependency.getClassifier() == null) {
				match = artifact.getClassifier() == null;
			} else {
				match = dependency.getClassifier().equals(artifact.getClassifier());
			}
		}

		if (match) {
			String type = artifact.getType();
			if (dependency.getType() == null) {
				match = type == null || type.equals("jar");
			} else {
				match = dependency.getType().equals(type);
			}
		}

		return match;
	}

	/**
	 * Calculates SHA-1 for the specified artifact.
	 * 
	 * @param artifact Maven artifact.
	 * 
	 * @return SHA-1 calculation.
	 */
	private String calculateSha1(Artifact artifact) {
		String sha1 = null;
		File file = artifact.getFile();
		if (file != null) {
			try {
				Map<String, Object> calcMap = ChecksumUtils.calc(file, Arrays.asList(Constants.SHA1));
				sha1 = (String) calcMap.get(Constants.SHA1);
			} catch (IOException e) {
				logDebug(Constants.ERROR_SHA1);
			}
		}
		return sha1;
	}

	/**
	 * The method builds the info model from the given dependency.
	 * 
	 * @param dependency Source maven dependency.
	 *  
	 * @return Extracted info model.
	 */
	private DependencyInfo getDependencyInfo(Dependency dependency) {
		DependencyInfo info = new DependencyInfo();

		// dependency data
		info.setGroupId(dependency.getGroupId());
		info.setArtifactId(dependency.getArtifactId());
		info.setVersion(dependency.getVersion());
		info.setScope(dependency.getScope());
		info.setClassifier(dependency.getClassifier());
		info.setOptional(dependency.isOptional());
		info.setType(dependency.getType());
		info.setSystemPath(dependency.getSystemPath());

		// exclusions
		Collection<ExclusionInfo> exclusions = info.getExclusions();
		for (Exclusion exclusion : dependency.getExclusions()) {
			exclusions.add(new ExclusionInfo(exclusion.getGroupId(), exclusion.getArtifactId()));
		}

		return info;
	}

	/**
	 * The method extract the GAV from the given maven project.
	 * 
	 * @param mavenProject Maven project
	 * 
	 * @return Project coordinates
	 */
	private Coordinates extractCoordinates(MavenProject mavenProject) {
		return new Coordinates(mavenProject.getGroupId(),
				mavenProject.getArtifactId(), 
				mavenProject.getVersion());
	}

	/**
	 * Writes a debug message to the log.
	 * 
	 * @param message Debug message
	 */
	private void logDebug(String message) {
		if (log != null) {
			log.debug(message);
		}
	}

}