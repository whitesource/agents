package org.whitesource.agent.maven.plugin.update;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.dispatch.UpdateInventoryResult;
import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.Coordinates;
import org.whitesource.agent.api.model.DependencyInfo;
import org.whitesource.agent.api.model.ExclusionInfo;
import org.whitesource.agent.maven.plugin.Constants;
import org.whitesource.agent.maven.plugin.service.ServiceProvider;
import org.whitesource.agent.maven.plugin.service.ServiceProviderImpl;


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

	private MavenProject project;
	
	private ServiceProvider service;

	/* --- Constructors --- */

	/**
	 * Creates a new {@link UpdaterImpl}
	 * 
	 */
	public UpdaterImpl(Properties properties, String orgToken, MavenProject project) {
		this.properties = properties;
		this.orgToken = orgToken;
		this.project = project;
	}

	/* --- Concrete implementation methods --- */

	public UpdateInventoryResult update() throws MojoExecutionException {
		UpdateInventoryResult result = null;
		
		// analyze projects
		Collection<AgentProjectInfo> projectInfos = setProjects(project);

		// create update request
		UpdateInventoryRequest request = new UpdateInventoryRequest(orgToken, projectInfos);

		logDebug(Constants.DEBUG_REQUEST_BUILT);

		// send request
		service = ServiceProviderImpl.getInstance();
		result = service.updateInventory(request);

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
	private Collection<AgentProjectInfo> setProjects(MavenProject project) 
	throws MojoExecutionException {
		Collection<AgentProjectInfo> projectInfos = new ArrayList<AgentProjectInfo>();

		// process top-level project
		processProject(project, projectInfos);

		// go over all children 
		processChildren(project, projectInfos);

		return projectInfos;
	}

	/**
	 * Sets the project coordinates and dependencies.
	 * 
	 * @param mavenProject Maven project.
	 */
	private void processProject(MavenProject mavenProject, Collection<AgentProjectInfo> projectInfos) {
		logDebug(Constants.DEBUG_FOUND_PROJECT + mavenProject.getArtifactId());

		AgentProjectInfo projectInfo = new AgentProjectInfo();
		populateProjectToken(projectInfo, mavenProject);
		populateCoordinates(projectInfo, mavenProject);
		populateDependencies(projectInfo, mavenProject);

		projectInfos.add(projectInfo);
	}

	/**
	 * The method process the collected projects of the given project. 
	 * 
	 * @param project Maven project.
	 */
	private void processChildren(MavenProject project, Collection<AgentProjectInfo> projectInfos) {
		List<MavenProject> collectedProjects = project.getCollectedProjects();
		if (collectedProjects != null) {
			for (MavenProject collectedProject : collectedProjects) {
				processProject(collectedProject, projectInfos);
			}	
		}
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
		for (Dependency dependency : project.getDependencies()) {
			dependencyInfos.add(getDependencyInfo(dependency));
		}
	}

	/**
	 * The method builds the info model from the given dependency.
	 * 
	 * @param dependency Source maven dependency.
	 *  
	 * @return Extracted info model.
	 */
	public static DependencyInfo getDependencyInfo(Dependency dependency) {
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
	 * The method extract the gav from the given maven project.
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