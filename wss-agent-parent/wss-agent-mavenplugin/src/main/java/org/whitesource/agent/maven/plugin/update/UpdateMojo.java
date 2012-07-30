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
import org.whitesource.agent.maven.plugin.WssServiceProvider;
import org.whitesource.api.client.WssServiceException;


/**
 * Sends an update request to White Source.
 * 
 * @goal update
 * @requiresProject true
 * @requiresOnline true
 * @requiresDependencyResolution runtime
 * @aggregator
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
	 * @parameter default-value="${reactorProjects}"
	 * @required true
	 * @readonly true
	 */ 
	private Collection<MavenProject> projects;

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
			} finally {
				WssServiceProvider.instance().shutdown();
			}
		}
	}

	/* --- Private methods --- */
	
	/**
	 * Wrapper for the execute() method for easier error handling.
	 */
	private void doExecute() throws MojoExecutionException {
		validateInputs();
		update();
	}
	
	/**
	 * Prepare and Send the update request.
	 * 
	 * @throws MojoExecutionException
	 */
	private void update() throws MojoExecutionException {
		// get properties
		PropertiesRequest request = WssServiceProvider.instance().requestFactory().newPropertiesRequest(orgToken);
		PropertiesResult propertiesResult;
		try {
			propertiesResult = WssServiceProvider.instance().provider().getProperties(request);
		} catch (WssServiceException e) {
			throw new MojoExecutionException("Error getting properties from WhiteSource", e);
		}
		
		// send update request
		Updater updater = new UpdaterImpl(propertiesResult.getProperties(), orgToken, projects);
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
	private void validateInputs() throws MojoExecutionException {
		// check if projects exists
		if (projects == null || projects.isEmpty()) {
			throw new MojoExecutionException(Constants.ERROR_NO_PROJECT);
		}
		
		// check if plugin was run from folder without POM file
		if (InputValidator.isStandAlonePom(projects.iterator().next())) {
			throw new MojoExecutionException(Constants.ERROR_NOT_PROJECT_FOLDER);
		}
		
		// check token property
		if (orgToken == null) {
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