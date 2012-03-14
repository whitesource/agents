package com.wss.agent.maven.plugin.update;

import java.util.Collection;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import com.wss.agent.api.PropertiesRequest;
import com.wss.agent.api.PropertiesResult;
import com.wss.agent.api.UpdateInventoryResult;
import com.wss.agent.maven.plugin.update.service.UpdateService;
import com.wss.agent.maven.plugin.update.service.UpdateServiceImpl;

/**
 * Sends an update request to White Source.
 * 
 * @goal update
 * @requiresProject true
 * @requiresOnline true
 * @aggregator false
 * 
 * @author Tom Shapira
 * 
 */
public class UpdateMojo extends AbstractMojo {

	/* --- Members --- */
	
	/**
	 * A unique token used for identifying the organization.
	 * 
	 * @parameter expression="${update.orgToken}"
	 * @required true
	 */
	private String orgToken;

	/**
	 * The Maven Project.
	 *
	 * @parameter expression="${project}" default-value="${project}"
	 * @required true
	 * @readonly true
	 */
	private MavenProject project = null;

	/**
	 * Indicates whether the build will continue even if there are errors.
	 *
	 * @parameter default-value="false"
	 */
	private boolean failOnError;

	/**
	 * Set this to 'true' to bypass artifact deploy.
	 *       
	 * @parameter default-value="false"
	 */
	private boolean skip;  
	
	/* --- Maven plugin implementation methods --- */

	public void execute() throws MojoExecutionException {
		if (skip) {
			getLog().info(Constants.SKIP_UPDATE);
		} else {
			try {
				doExecute();
			} catch (MojoExecutionException e) {
				handleError(e);
			}
		}
	}

	/* --- Private methods --- */
	
	/**
	 * Wrapper for the execute() method for easier error handling.
	 */
	private void doExecute() throws MojoExecutionException {
		validateInputs(project, orgToken);
		
		update();
	}
	
	/**
	 * Prepare and Send the update request.
	 * 
	 * @throws MojoExecutionException
	 */
	private void update() throws MojoExecutionException {
		UpdateService service = UpdateServiceImpl.getInstance();
		service.setLog(getLog());
		
		// get properties
		PropertiesRequest request = new PropertiesRequest(orgToken);
		PropertiesResult propertiesResult = service.getProperties(request);
		
		// send update request
		Updater updater = new UpdaterImpl(propertiesResult.getProperties(), orgToken, project);
		updater.setLog(getLog());
		UpdateInventoryResult result = updater.update();
		
		// show result in log
		logResult(result);
	}
	
	/**
	 * Logs the operation results. 
	 * 
	 * @param result
	 */
	private void logResult(UpdateInventoryResult result) {
		getLog().info("");
		getLog().info(Constants.INFO_DOMAIN + result.getOrganization());

		Collection<String> updatedProjects = result.getUpdatedProjects();
		if (updatedProjects.isEmpty()) {
			getLog().info(Constants.INFO_NO_PROJECTS_UPDATED);
		} else {
			getLog().info(Constants.INFO_PROJECTS_UPDATED);
			getLog().info("");
			for (String projectName : updatedProjects) {
				getLog().info(projectName);
			}
			getLog().info("");
			getLog().info(Constants.INFO_EMAIL_MESSAGE);
		}
	}

	/**
	 * Validates mandatory maven parameter inputs required for the request.
	 * 
	 * @throws MojoExecutionException In case of any invalid property.
	 */
	private void validateInputs(MavenProject project, String token) throws MojoExecutionException {
		// check if project exists
		if (project == null) {
			throw new MojoExecutionException(Constants.ERROR_NO_PROJECT);
		}
		
		// check if plugin was run from folder without POM file
		if (isStandAlonePom(project)) {
			throw new MojoExecutionException(Constants.ERROR_NOT_PROJECT_FOLDER);
		}
		
		// check token property
		if (token == null) {
			throw new MojoExecutionException(Constants.ERROR_MISSING_TOKEN);
		}
	}

	/**
	 * Checks if the plugin was run in folder without pom file.
	 * 
	 * @param project The maven project. 
	 * @return True if the folder is not a maven project folder, false o/w
	 */
	private boolean isStandAlonePom(MavenProject project) {
		return project.getArtifactId().equals(Constants.STANDALONE_POM_ARTIFACT_ID) && 
			project.getGroupId().equals(Constants.STANDALONE_POM_GROUP_ID) &&
			project.getVersion().equals(Constants.STANDALONE_POM_VERSION);
	}
	
	/**
	 * Handle error according to failOnError property.
	 * 
	 * @param e Exception thrown
	 * @throws MojoExecutionException In case failOnError is true
	 */
	private void handleError(Exception e) throws MojoExecutionException {
		String message = e.getMessage();
		
		if (failOnError) {
			throw new MojoExecutionException(message);
		} else {
			getLog().warn(message);
			getLog().debug(e);
		}
	}
}
