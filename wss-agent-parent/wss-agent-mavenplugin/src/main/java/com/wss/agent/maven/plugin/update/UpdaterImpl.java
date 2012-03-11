package com.wss.agent.maven.plugin.update;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import com.wss.agent.api.UpdateInventoryRequest;
import com.wss.agent.api.UpdateInventoryResult;
import com.wss.agent.maven.plugin.update.service.UpdateService;
import com.wss.agent.maven.plugin.update.service.UpdateServiceImpl;
import com.wss.agent.model.AgentProjectInfo;
import com.wss.agent.model.DependencyInfo;

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
	
	private UpdateService service;

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
		service = UpdateServiceImpl.getInstance();
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
	 * @param mavenProject The maven project
	 */
	private void processProject(MavenProject mavenProject, Collection<AgentProjectInfo> projectInfos) {
		logDebug(Constants.DEBUG_FOUND_PROJECT + mavenProject.getArtifactId());

		AgentProjectInfo project = new AgentProjectInfo();
		setProjectToken(project, mavenProject);
		setCoordinates(project, mavenProject);
		setDependencies(project, mavenProject);

		projectInfos.add(project);
	}

	/**
	 * Iteratively sets all the project's modules.
	 * 
	 * @param project The maven project
	 */
	@SuppressWarnings("rawtypes")
	private void processChildren(MavenProject project, Collection<AgentProjectInfo> projectInfos) {
		List collectedProjects = project.getCollectedProjects();

		for (Object collectedProject : collectedProjects) {
			if (collectedProject instanceof MavenProject) {
				processProject((MavenProject) collectedProject, projectInfos);
			}
		}
	}

	/**
	 * Gets the project token parameter from the plugin configuration in the pom file.
	 * 
	 * @param project The project model.
	 * @param mavenProject The maven project.
	 */
	private void setProjectToken(AgentProjectInfo project, MavenProject mavenProject) {
		Plugin plugin = mavenProject.getPlugin(Constants.PLUGIN_KEY);
		if (plugin != null) {
			Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();
			if (configuration != null) {
				Xpp3Dom projectTokenParameter = configuration.getChild(Constants.PROJECT_TOKEN);
				if (projectTokenParameter != null) {
					project.setProjectToken(projectTokenParameter.getValue());
				}
			}
		}
	}

	/**
	 * Sets the coordinates (artifactId:groupId:version) of the project and of it's parent.
	 * 
	 * @param project The project model.
	 * @param mavenProject The maven project.
	 */
	private void setCoordinates(AgentProjectInfo project, MavenProject mavenProject) {
		// get project coordinates
		project.setCoordinates(MavenUtils.getCoordinates(mavenProject));

		// check for parent
		MavenProject parent = mavenProject.getParent();
		if (parent != null) {
			project.setParentCoordinates(MavenUtils.getCoordinates(parent));
		}
	}

	/**
	 * Sets the project dependencies.
	 * 
	 * @param project The project model.
	 * @param mavenProject The maven project.
	 */
	private void setDependencies(AgentProjectInfo project, MavenProject mavenProject) {
		List<Dependency> dependencies = mavenProject.getDependencies();
		ArrayList<DependencyInfo> dependencyInfos = new ArrayList<DependencyInfo>();

		for (Dependency dependency : dependencies) {
			dependencyInfos.add(MavenUtils.getInfo(dependency));
		}
		project.setDependencies(dependencyInfos);
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
