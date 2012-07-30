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
package org.whitesource.agent.maven.plugin.report;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.whitesource.agent.api.dispatch.ReportRequest;
import org.whitesource.agent.api.dispatch.ReportResult;
import org.whitesource.agent.api.model.DependencyInfo;
import org.whitesource.agent.maven.plugin.Constants;
import org.whitesource.agent.maven.plugin.InputValidator;
import org.whitesource.agent.maven.plugin.WssServiceProvider;
import org.whitesource.api.client.WssServiceException;


/**
 * Sends information regarding the dependencies to White Source and receives the License analysis. 
 *
 * @goal report
 * @requiresProject true
 * @requiresOnline true
 * @aggregator false
 * @requiresDependencyResolution compile
 * 
 * @author tom.shapira
 */
public class ReportMojo extends AbstractMojo {

	/* --- Members --- */

	/**
	 * The Maven Project.
	 *
	 * @parameter expression="${project}" default-value="${project}"
	 * @required true
	 * @readonly true
	 */
	private MavenProject project = null;

	/**
	 * The output directory for the intermediate XML report.
	 *
	 * @parameter default-value="${project.build.directory}/whitesource-reports"
	 * @required
	 */
	protected File outputDirectory;
	
	/**
	 * Set this to 'true' to bypass artifact deploy.
	 *       
	 * @parameter default-value="false"
	 */
	private boolean skip;

	/**
	 * Indicates whether the build will continue even if there are errors.
	 *
	 * @parameter default-value="false"
	 */
	private boolean failOnError;

	/**
	 * Set this to 'false' to disable generating an operation report.
	 * 
	 * @parameter default-value="true"
	 */
	private boolean generateReport;

	/* --- Maven plugin implementation methods --- */

	public void execute() throws MojoExecutionException {
		if (skip) {
			getLog().info(Constants.INFO_SKIP_LICENSES);
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
	 * 
	 */
	private void doExecute() throws MojoExecutionException{
		validateInputs(project);

		// collect dependencies
		Set<DependencyInfo> dependencies = new HashSet<DependencyInfo>();
		processProject(project, dependencies);
		processChildren(project, dependencies);

		if (dependencies.isEmpty()) {
			getLog().info(Constants.INFO_NO_DEPENDENCIES);
		} else {
			// call WhiteSource service
			ReportResult result = getReport(dependencies);

			// log result
			ReportRenderer.renderToLog(result, getLog());

			// generate xml report
			if (generateReport) {
				getLog().info(Constants.INFO_REPORT_DIRECTORY + outputDirectory.getAbsolutePath());
				try {
					ReportRenderer.renderToXML(outputDirectory, result);
				} catch (IOException e) {
					throw new MojoExecutionException("Unable to generate report", e);
				}
			}
		}
	}

	/**
	 * The method call the WhiteSource service to perform the license analysis procedure.
	 * 
	 * @param dependencies Dependencies to analyze.
	 * 
	 * @return License analysis result.
	 * 
	 * @throws MojoExecutionException In case of errors during the operation.
	 */
	private ReportResult getReport(Collection<DependencyInfo> dependencies) throws MojoExecutionException {
		ReportResult result = null;
		
		try {
			ReportRequest request = WssServiceProvider.instance().requestFactory().newReportRequest(dependencies);
			result = WssServiceProvider.instance().provider().getReport(request);
		} catch (WssServiceException e) {
			throw new MojoExecutionException("Error getting report", e);
		}
		
		return result;
	}

	/**
	 * Gets the project dependencies.
	 * 
	 * @param mavenProject Maven project.
	 */
	private void processProject(MavenProject project, Set<DependencyInfo> dependencies) {
		for (Artifact artifact : project.getArtifacts()) {
			dependencies.add(getDependencyInfo(artifact));
		}
	}

	/**
	 * The method builds the info model from the given artifact.
	 * 
	 * @param artifact Source maven artifact.
	 *  
	 * @return Extracted info model.
	 */
	public static DependencyInfo getDependencyInfo(Artifact artifact) {
		DependencyInfo info = new DependencyInfo();

		// dependency data
		info.setGroupId(artifact.getGroupId());
		info.setArtifactId(artifact.getArtifactId());
		info.setVersion(artifact.getVersion());
		info.setScope(artifact.getScope());
		info.setClassifier(artifact.getClassifier());
		info.setOptional(artifact.isOptional());
		info.setType(artifact.getType());

		return info;
	}
	
	/**
	 * The method process the collected projects of the given project. 
	 * 
	 * @param project Maven project.
	 */
	private void processChildren(MavenProject project, Set<DependencyInfo> dependencies) {
		List<MavenProject> collectedProjects = project.getCollectedProjects();
		if (collectedProjects != null) {
			for (MavenProject collectedProject : collectedProjects) {
				processProject(collectedProject, dependencies);
			}	
		}
	}

	/**
	 * Validates mandatory maven parameter inputs required for the request.
	 * 
	 * @throws MojoExecutionException In case of any invalid property.
	 */
	private void validateInputs(MavenProject project) throws MojoExecutionException {
		// check if project exists
		if (project == null) {
			throw new MojoExecutionException(Constants.ERROR_NO_PROJECT);
		}

		// check if plugin was run from folder without POM file
		if (InputValidator.isStandAlonePom(project)) {
			throw new MojoExecutionException(Constants.ERROR_NOT_PROJECT_FOLDER);
		}
	}

	/**
	 * Handle error according to failOnError property.
	 * 
	 * @param e Exception thrown
	 * 
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
