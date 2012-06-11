/**
 * 
 */
package org.whitesource.agent.jenkins;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.ivy.IvyBuild;
import hudson.ivy.IvyModule;
import hudson.ivy.IvyModuleSetBuild;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSetBuild;
import hudson.maven.reporters.MavenArtifactRecord;
import hudson.model.BuildListener;
import hudson.model.FreeStyleBuild;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.Job;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.sonatype.aether.util.ChecksumUtils;
import org.whitesource.agent.api.dispatch.PropertiesRequest;
import org.whitesource.agent.api.dispatch.PropertiesResult;
import org.whitesource.agent.api.dispatch.RequestFactory;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.dispatch.UpdateInventoryResult;
import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.Coordinates;
import org.whitesource.agent.api.model.DependencyInfo;
import org.whitesource.agent.jenkins.ivy.IvyDependenciesRecord;
import org.whitesource.agent.jenkins.ivy.IvyDependency;
import org.whitesource.agent.jenkins.maven.MavenDependenciesRecord;
import org.whitesource.agent.jenkins.maven.MavenDependency;
import org.whitesource.agent.jenkins.plugin.Constants;
import org.whitesource.api.client.WssServiceClient;
import org.whitesource.api.client.WssServiceClientImpl;
import org.whitesource.api.client.WssServiceException;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * @author ramakrishna
 * 
 */
@SuppressWarnings("unchecked")
public class WhiteSourcePublisher extends Recorder {

	private final WhiteSourceConfiguration config;
	private final ArrayList<LibraryLocation> libLocations;
	private final String wssUrl;
	private final String orgToken;

	@DataBoundConstructor
	public WhiteSourcePublisher(String wssUrl, String orgToken,
			List<LibraryLocation> libLocations) {
		this.wssUrl = wssUrl;
		this.orgToken = orgToken;
		this.config = new WhiteSourceConfiguration(wssUrl, wssUrl);
		this.libLocations = new ArrayList<LibraryLocation>(libLocations);
	}

	public WhiteSourceConfiguration getConfig() {
		return config;
	}

	public String getWssUrl() {
		return wssUrl;
	}

	public String getOrgToken() {
		return orgToken;
	}

	public ArrayList<LibraryLocation> getLibLocations() {
		return libLocations;
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	@Override
	public boolean perform(AbstractBuild build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		String buildLog = build.getLog();
		listener.getLogger().println("In White Source Publisher ...");
		Result pr = build.getResult();

		if (isBuildFromM2ReleasePlugin(build)) {
			listener.getLogger()
					.append("M2 Release build, not uploading artifacts to Artifactory. ");
			// return true;
		}

		if (!(build instanceof MavenModuleSetBuild)) {
			listener.getLogger()
					.format("Non maven build type: %s", build.getClass())
					.println();
		}

		Collection<AgentProjectInfo> projectInfos = new ArrayList<AgentProjectInfo>();
		if ((build instanceof MavenModuleSetBuild)) {
			listener.getLogger().println(
					"---------------Maven Modules-------------");
			MavenModuleSetBuild mavenBuild = (MavenModuleSetBuild) build;

			Map<MavenModule, MavenBuild> moduleBuilds = mavenBuild
					.getModuleLastBuilds();
			Set<MavenModule> mavenModuleCollection = moduleBuilds.keySet();
			Iterator<MavenModule> mavenModuleIterator = mavenModuleCollection
					.iterator();
			while (mavenModuleIterator.hasNext()) {
				AgentProjectInfo projectInfo = new AgentProjectInfo();
				MavenModule mavenModule = mavenModuleIterator.next();
				listener.getLogger().println(
						"Maven Module --> " + mavenModule.getName());
				listener.getLogger().println(
						"-------------------------------------------");
				MavenBuild mBuild = moduleBuilds.get(mavenModule);

				listener.getLogger().println("Co-ordiantes");
				listener.getLogger().println(
						"-------------------------------------------");
				MavenArtifactRecord action = mBuild
						.getAction(MavenArtifactRecord.class);
				if (action != null) {
					listener.getLogger().println(action.pomArtifact.artifactId);
					listener.getLogger().println(action.pomArtifact.groupId);
					listener.getLogger().println(action.pomArtifact.version);

					projectInfo.setCoordinates(new Coordinates(
							action.pomArtifact.groupId,
							action.pomArtifact.artifactId,
							action.pomArtifact.version));

				}

				listener.getLogger().println("Dependencies");
				listener.getLogger().println(
						"-------------------------------------------");
				Collection<DependencyInfo> dependencyInfos = projectInfo
						.getDependencies();
				MavenDependenciesRecord action3 = mBuild
						.getAction(MavenDependenciesRecord.class);
				Set<MavenDependency> mavenDependencies = action3
						.getDependencies();
				Iterator<MavenDependency> mavenDependencyIterator = mavenDependencies
						.iterator();
				while (mavenDependencyIterator.hasNext()) {
					MavenDependency mavenDependency = mavenDependencyIterator
							.next();
					listener.getLogger().println(
							mavenDependency.groupId + "--"
									+ mavenDependency.artifactId + "--"
									+ mavenDependency.version);

					DependencyInfo info = new DependencyInfo();
					// dependency data
					info.setGroupId(mavenDependency.groupId);
					info.setArtifactId(mavenDependency.artifactId);
					info.setVersion(mavenDependency.version);
					info.setScope(mavenDependency.scope);
					info.setClassifier(mavenDependency.classifier);
					// info.setOptional(mavenDependency.isOptional());
					info.setType(mavenDependency.type);
					// info.setSystemPath(mavenDependency.getSystemPath());
					dependencyInfos.add(info);
				}
			}
		} else if ((build instanceof FreeStyleBuild)) {
			AgentProjectInfo projectInfo = new AgentProjectInfo();
			listener.getLogger().println(
					"---------------Free Style Build Modules-------------");
			FreeStyleBuild freeStyleBuild = (FreeStyleBuild) build;
			Collection<DependencyInfo> dependencyInfos = projectInfo
					.getDependencies();

			listener.getLogger().println(
					"Workspace Location : " + freeStyleBuild.getWorkspace());
			if (getLibLocations() != null && getLibLocations().size() > 0) {
				for (LibraryLocation libLocation : getLibLocations()) {
					FilePath libraryFiles = new FilePath(
							freeStyleBuild.getWorkspace(),
							libLocation.getLibLocation());
					// listener.getLogger().println("Workspace Location : " +
					// freeStyleBuild.getWorkspace().getName());
					listener.getLogger().println(
							"Libraries Location : " + libraryFiles.getName());
					if (libraryFiles.isDirectory()) {
						List<FilePath> libFileList = libraryFiles.list();
						for (FilePath libFile : libFileList) {
							if (!libFile.isDirectory()) {
								listener.getLogger().println();
								DependencyInfo info = new DependencyInfo();
								info.setSystemPath(libFile.getRemote());
								info.setArtifactId(libFile.getName());
								listener.getLogger().println(
										libFile.getName() + "--"
												+ libFile.getRemote());
								info.setSha1(calculateSha1(
										new File(libFile.getRemote()),
										listener.getLogger()));
								dependencyInfos.add(info);
							}
						}
					}
				}
			}

		} else if ((build instanceof IvyModuleSetBuild)) {
			listener.getLogger().println(
					"---------------Ivy ModuleSet Build Modules-------------");
			IvyModuleSetBuild ivyModuleSetBuild = (IvyModuleSetBuild) build;
			Map<IvyModule, IvyBuild> moduleBuilds = ivyModuleSetBuild
					.getModuleLastBuilds();
			Set<IvyModule> ivyModuleCollection = moduleBuilds.keySet();
			Iterator<IvyModule> ivyModuleIterator = ivyModuleCollection
					.iterator();
			while (ivyModuleIterator.hasNext()) {
				AgentProjectInfo projectInfo = new AgentProjectInfo();
				IvyModule ivyModule = ivyModuleIterator.next();
				listener.getLogger().println(
						"Ivy Module --> " + ivyModule.getName());
				listener.getLogger().println(
						"-------------------------------------------");
				IvyBuild iBuild = moduleBuilds.get(ivyModule);

				listener.getLogger().println("Co-ordiantes");
				listener.getLogger().println(
						"-------------------------------------------");

				listener.getLogger().println("Dependencies");
				listener.getLogger().println(
						"-------------------------------------------");
				Collection<DependencyInfo> dependencyInfos = projectInfo
						.getDependencies();
				IvyDependenciesRecord action3 = iBuild
						.getAction(IvyDependenciesRecord.class);
				Set<IvyDependency> ivyDependencies = action3.getDependencies();
				Iterator<IvyDependency> ivyDependencyIterator = ivyDependencies
						.iterator();
				while (ivyDependencyIterator.hasNext()) {
					IvyDependency ivyDependency = ivyDependencyIterator.next();
				}
			}
		} else {
			listener.getLogger().format("Build Type: %s", build.getClass())
					.println();
		}

		/*
		WssServiceClient client = null;
		if (StringUtils.isEmpty(config.getWssUrl())) {
			client = new WssServiceClientImpl();
		} else {
			client = new WssServiceClientImpl(config.getWssUrl());
		}
		RequestFactory requestFactory = new RequestFactory("jenkins", "1.0");
		PropertiesRequest request = requestFactory.newPropertiesRequest(config
				.getOrgToken());
		request.setTimeStamp(new Date().getTime());
		PropertiesResult result = null;
		try {
			result = client.getProperties(request);
			listener.getLogger().println(
					result != null ? result.getProperties().toString()
							: "Empty Result");
		} catch (WssServiceException e) {
			listener.getLogger().println(
					"Error getting properties " + e.getMessage());
		}

		// create update request
		UpdateInventoryRequest investoryRequest = requestFactory
				.newUpdateInventoryRequest(config.getOrgToken(), projectInfos);

		UpdateInventoryResult inventoryResult = null;
		// send request
		try {
			inventoryResult = client.updateInventory(investoryRequest);
			logResult(inventoryResult, listener.getLogger());
		} catch (WssServiceException e) {
			e.printStackTrace();
		}
		*/ 
		return true;
	}

	/**
	 * Logs the operation results.
	 * 
	 * @param result
	 */
	private void logResult(UpdateInventoryResult result, PrintStream logger) {
		logger.println("");
		logger.println(Constants.INFO_DOMAIN + result.getOrganization());

		// log updated projects
		Collection<String> updatedProjects = result.getUpdatedProjects();
		if (updatedProjects.isEmpty()) {
			logger.println(Constants.INFO_NO_PROJECTS_UPDATED);
		} else {
			logger.println(Constants.INFO_PROJECTS_UPDATED);
			logger.println("");
			for (String projectName : updatedProjects) {
				logger.println(projectName);
			}
			logger.println("");
			logger.println(Constants.INFO_EMAIL_MESSAGE);
			logger.println("");
		}

		// log created projects
		Collection<String> createdProjects = result.getCreatedProjects();
		if (!createdProjects.isEmpty()) {
			logger.println(Constants.INFO_PROJECTS_CREATED);
			logger.println("");
			for (String projectName : createdProjects) {
				logger.println(projectName);
			}
		}
	}

	private boolean isBuildFromM2ReleasePlugin(AbstractBuild<?, ?> build) {
		List<Cause> causes = build.getCauses();
		for (Cause cause : causes) {
			System.out.println(cause.getClass().getName());
		}
		return !causes.isEmpty()
				&& Iterables.any(causes, new Predicate<Cause>() {
					public boolean apply(Cause input) {

						return "org.jvnet.hudson.plugins.m2release.ReleaseCause"
								.equals(input.getClass().getName());
					}
				});
	}

	/**
	 * Calculates SHA-1 for the specified artifact.
	 * 
	 * @param artifact
	 *            Maven artifact.
	 * 
	 * @return SHA-1 calculation.
	 */

	private String calculateSha1(File file, PrintStream logger) {
		String sha1 = null;
		if (file != null) {
			try {
				Map<String, Object> calcMap = ChecksumUtils.calc(file,
						Arrays.asList(Constants.SHA1));
				sha1 = (String) calcMap.get(Constants.SHA1);
			} catch (IOException e) {
				logger.println(Constants.ERROR_SHA1);
			}
		}
		return sha1;
	}

	@Extension
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Publisher> {
		public DescriptorImpl() {
			super(WhiteSourcePublisher.class);
			load();
		}

		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		public FormValidation doCheckWssUrl(
				@AncestorInPath final AbstractProject<?, ?> project,
				@QueryParameter final String wssUrl) throws IOException,
				ServletException {
			project.checkPermission(Job.CONFIGURE);
			return WhiteSourceUtils.validateWssUrl(wssUrl);
		}

		public FormValidation doCheckOrgToken(
				@AncestorInPath final AbstractProject<?, ?> project,
				@QueryParameter final String orgToken) throws IOException,
				ServletException {
			project.checkPermission(Job.CONFIGURE);
			return WhiteSourceUtils.validateOrgToken(orgToken);
		}

		public FormValidation doCheckLibLocation(
				@AncestorInPath final AbstractProject<?, ?> project,
				@QueryParameter final String libLocation) throws IOException,
				ServletException {
			project.checkPermission(Job.CONFIGURE);
			/*
			 * if(project.getLastBuild() != null && project.getLastBuild()
			 * instanceof FreeStyleBuild) { //To Do: Logic to check correct path
			 * w.r.t workspace for free style builds. }
			 */
			return WhiteSourceUtils.validateLibLocation(libLocation);
		}

		@Override
		public String getDisplayName() {
			return "White Source Service";
		}

		@Override
		public String getHelpFile() {
			return "/plugin/WhiteSourceAgent/help/main.html";
		}

		/*
		 * @Override public WhiteSourcePublisher newInstance(StaplerRequest req,
		 * JSONObject formData) throws FormException {
		 * 
		 * WhiteSourceConfiguration config = req.bindParameters(
		 * WhiteSourceConfiguration.class,
		 * "WhiteSourceAgent.whitesourceConfiguration."); return new
		 * WhiteSourcePublisher(config); }
		 */
	}

}
