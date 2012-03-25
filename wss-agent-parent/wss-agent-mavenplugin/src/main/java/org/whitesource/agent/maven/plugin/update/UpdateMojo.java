package org.whitesource.agent.maven.plugin.update;

import java.util.Collection;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.whitesource.agent.api.dispatch.PropertiesRequest;
import org.whitesource.agent.api.dispatch.PropertiesResult;
import org.whitesource.agent.api.dispatch.UpdateInventoryResult;
import org.whitesource.agent.maven.plugin.Constants;
import org.whitesource.agent.maven.plugin.InputValidator;
import org.whitesource.agent.maven.plugin.service.ServiceProvider;
import org.whitesource.agent.maven.plugin.service.ServiceProviderImpl;


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
			getLog().info(Constants.INFO_SKIP_UPDATE);
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
		ServiceProvider service = ServiceProviderImpl.getInstance();
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

		// log updated projects
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
			getLog().info("");
		}
		
		// log created projects
		Collection<String> createdProjects = result.getCreatedProjects();
		if (!createdProjects.isEmpty()) {
			getLog().info(Constants.INFO_PROJECTS_CREATED);
			getLog().info("");
			for (String projectName : createdProjects) {
				getLog().info(projectName);
			}
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
		if (InputValidator.isStandAlonePom(project)) {
			throw new MojoExecutionException(Constants.ERROR_NOT_PROJECT_FOLDER);
		}
		
		// check token property
		if (token == null) {
			throw new MojoExecutionException(Constants.ERROR_MISSING_TOKEN);
		}
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
			throw new MojoExecutionException(message, e);
		} else {
			getLog().warn(message);
			getLog().debug(e);
		}
	}
}