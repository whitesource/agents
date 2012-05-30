/**
 * 
 */
package org.whitesource.agent.jenkins;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.whitesource.agent.api.dispatch.PropertiesRequest;
import org.whitesource.agent.api.dispatch.PropertiesResult;
import org.whitesource.agent.api.dispatch.RequestFactory;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.jenkins.maven.MavenDependenciesRecord;
import org.whitesource.agent.jenkins.maven.MavenDependency;
import org.whitesource.api.client.WssServiceClient;
import org.whitesource.api.client.WssServiceClientImpl;
import org.whitesource.api.client.WssServiceException;


import hudson.Extension;
import hudson.Launcher;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSetBuild;
import hudson.maven.reporters.MavenAbstractArtifactRecord;
import hudson.maven.reporters.MavenArtifactRecord;
import hudson.model.Action;
import hudson.model.BuildListener;
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

/**
 * @author ramakrishna
 *
 */
@SuppressWarnings("unchecked")
public class WhiteSourcePublisher extends Recorder {

	private volatile WhiteSourceConfiguration config;
	
	public WhiteSourcePublisher(WhiteSourceConfiguration config) {
		this.config = config;
	}
	
	public WhiteSourceConfiguration getConfig() {
		return config;
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
		
		List actions = build.getProject().getActions();
		for(Action action : (List<Action>)actions) {
			listener.getLogger().println(action != null ? action.getDisplayName() : "Action Name is Null");
		}
		Collection allJobs = build.getProject().getAllJobs();
		Iterator jobIterator = allJobs.iterator();
		while(jobIterator.hasNext()) {
			Job job = (Job)jobIterator.next();
			listener.getLogger().println("Job:" + job.getName());
		}
		
		if (isBuildFromM2ReleasePlugin(build)) {
            listener.getLogger().append("M2 Release build, not uploading artifacts to Artifactory. ");
            //return true;
        }
		
		if (!(build instanceof MavenModuleSetBuild)) {
            listener.getLogger().format("Non maven build type: %s", build.getClass()).println();
        }
		
		listener.getLogger().println("---------------MavenDependenciesRecords-------------");
		MavenModuleSetBuild mavenBuild = (MavenModuleSetBuild) build;
		
		mavenBuild.getModuleLastBuilds();
		Map<MavenModule, MavenBuild> moduleBuilds = mavenBuild.getModuleLastBuilds();
		Set<MavenModule> mavenModuleCollection = moduleBuilds.keySet();
		Iterator<MavenModule> mavenModuleIterator = mavenModuleCollection.iterator();
		while(mavenModuleIterator.hasNext()) {
			MavenModule mavenModule = mavenModuleIterator.next();
			listener.getLogger().println("Maven Module --> " + mavenModule.getName());
			MavenBuild mBuild = moduleBuilds.get(mavenModule);
			
			//listener.getLogger().println("Maven Module Build --> " + mBuild.getDisplayName() + "----" + mBuild.getDescription());
			listener.getLogger().println("Co-ordiantes");
			MavenArtifactRecord action = mBuild.getAction(MavenArtifactRecord.class);
            if (action != null) {
            	listener.getLogger().println(action.pomArtifact.artifactId);
            	listener.getLogger().println(action.pomArtifact.groupId);
            	listener.getLogger().println(action.pomArtifact.version);
            	
            	
            	//action.
            }
            MavenAbstractArtifactRecord action1 = mBuild.getAction(MavenAbstractArtifactRecord.class);
           
            /*
            //listener.getLogger();
            if(mavenModule.getParent() != null) {
            	//mavenModule.getParent().get
            	//listener.getLogger().println(mavenModule.getParent().getAction(MavenArtifactRecord.class));
            }
            
            if(mBuild.getParent().getParent().) {
            	
            }
            */
			/*
			MavenAbstractArtifactRecord action = moduleBuild.getAction(MavenAbstractArtifactRecord.class);
            if (action != null) {
                actions.add(action);
            }
            */
            
            listener.getLogger().println("Dependencies");
            MavenDependenciesRecord action3 = mBuild.getAction(MavenDependenciesRecord.class);
            Set<MavenDependency> mavenDependencies = action3.getDependencies();
            Iterator<MavenDependency> mavenDependencyIterator = mavenDependencies.iterator();
			while(mavenDependencyIterator.hasNext()) {
				MavenDependency mavenDependency = mavenDependencyIterator.next();
				listener.getLogger().println(mavenDependency.groupId + "--" + mavenDependency.artifactId + "--" + mavenDependency.version);
			}
		}
		
		/*
		List<MavenDependenciesRecord> mdrs = getMavenDependenciesRecordActions(mavenBuild);
		for(MavenDependenciesRecord mdr : mdrs) {
			listener.getLogger().println("MavenDependenciesRecord:" + mdr.getDisplayName());
			
			Set<MavenDependency> mavenDependencies = mdr.getDependencies();
			Iterator<MavenDependency> mavenDependencyIterator = mavenDependencies.iterator();
			while(mavenDependencyIterator.hasNext()) {
				MavenDependency mavenDependency = mavenDependencyIterator.next();
				listener.getLogger().println(mavenDependency.groupId + "--" + mavenDependency.artifactId + "--" + mavenDependency.version);
			}
		}
		listener.getLogger().println("----------------------------");
		*/
		
		/*
		listener.getLogger().println("---------------MavenAbstractArtifactRecord-------------");
		
		List<MavenAbstractArtifactRecord> mars = getArtifactRecordActions(mavenBuild);
		for(MavenAbstractArtifactRecord mar : mars) {
			listener.getLogger().println("MavenAbstractArtifactRecord:" + mar.getDisplayName());
		}
		listener.getLogger().println("----------------------------");
		*/
		/*
		WssServiceClient client = null;
		if(StringUtils.isEmpty(config.getWssUrl())) {
			client = new WssServiceClientImpl();
		} else {
			client = new WssServiceClientImpl(config.getWssUrl());
		}
		RequestFactory requestFactory = new RequestFactory("jenkins", "1.0");
		
		PropertiesRequest request = requestFactory.newPropertiesRequest(config.getOrgToken());
		request.setTimeStamp(new Date().getTime());
		PropertiesResult result = null; 
		
		UpdateInventoryRequest inventoryRequest = null;
		try {
			result = client.getProperties(request);
			listener.getLogger().println(result != null ? result.getProperties().toString() : "Empty Result");
		} catch (WssServiceException e) {
			listener.getLogger().println("Error getting properties " + e.getMessage());
		}
		*/
		return true;
	}
	
	private boolean isBuildFromM2ReleasePlugin(AbstractBuild<?, ?> build) {
        List<Cause> causes = build.getCauses();
        for(Cause cause : causes) {
        	System.out.println(cause.getClass().getName());
        }
        return !causes.isEmpty() && Iterables.any(causes, new Predicate<Cause>() {
            public boolean apply(Cause input) {
            	
                return "org.jvnet.hudson.plugins.m2release.ReleaseCause".equals(input.getClass().getName());
            }
        });
    }
	
	protected List<MavenAbstractArtifactRecord> getArtifactRecordActions(MavenModuleSetBuild build) {
        List<MavenAbstractArtifactRecord> actions = Lists.newArrayList();
        for (MavenBuild moduleBuild : build.getModuleLastBuilds().values()) {
        	MavenAbstractArtifactRecord action = moduleBuild.getAction(MavenAbstractArtifactRecord.class);
            if (action != null) {
                actions.add(action);
            }
        }
        return actions;
    }
	
	protected List<MavenDependenciesRecord> getMavenDependenciesRecordActions(MavenModuleSetBuild build) {
        List<MavenDependenciesRecord> actions = Lists.newArrayList();
        for (MavenBuild moduleBuild : build.getModuleLastBuilds().values()) {
        	MavenDependenciesRecord action = moduleBuild.getAction(MavenDependenciesRecord.class);
            if (action != null) {
                actions.add(action);
            }
        }
        return actions;
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
		
		public FormValidation doCheckWssUrl(@AncestorInPath final AbstractProject<?, ?> project,
                @QueryParameter final String wssUrl) throws IOException, ServletException {
        	project.checkPermission(Job.CONFIGURE);
            return WhiteSourceUtils.validateWssUrl(wssUrl);
        }
		
		public FormValidation doCheckOrgToken(@AncestorInPath final AbstractProject<?, ?> project,
                @QueryParameter final String orgToken) throws IOException, ServletException {
        	project.checkPermission(Job.CONFIGURE);
            return WhiteSourceUtils.validateOrgToken(orgToken);
        }
		
		@Override
		public String getDisplayName() {
			return "White Source Service";
		}

		@Override
		public String getHelpFile() {
			return "/plugin/WhiteSourceAgent/help/main.html";
		}

		@Override
		public WhiteSourcePublisher newInstance(StaplerRequest req,
				JSONObject formData) throws FormException {
			System.out.println("-----------------------");
			System.out.println(formData.toString());
			System.out.println(req.getParameterMap().toString());
			// if(req.getParameter("postbuild-task.")!=null)
			System.out.println("-----------------------");
			
			WhiteSourceConfiguration config = req.bindParameters(WhiteSourceConfiguration.class, "WhiteSourceAgent.whitesourceConfiguration.");
			return new WhiteSourcePublisher(config);
		}
	}	
	
}
