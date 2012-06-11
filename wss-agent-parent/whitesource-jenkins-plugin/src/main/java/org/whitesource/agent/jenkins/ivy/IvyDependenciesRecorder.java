/**
 * 
 */
package org.whitesource.agent.jenkins.ivy;

import hudson.Extension;
import hudson.ivy.IvyBuildProxy;
import hudson.ivy.IvyBuildProxy.BuildCallable;
import hudson.ivy.IvyReporter;
import hudson.ivy.IvyReporterDescriptor;
import hudson.ivy.IvyBuild;
import hudson.ivy.IvyModule;
import hudson.model.BuildListener;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.tools.ant.BuildEvent;

/**
 * @author c_rsharv
 *
 */
public class IvyDependenciesRecorder extends IvyReporter {
	
	/**
     * All dependencies this module used, including transitive ones.
     */
    private transient Set<IvyDependency> dependencies;

    @Override
    public boolean preBuild(IvyBuildProxy build, BuildEvent event, BuildListener listener) {
        listener.getLogger().println("[Jenkins] Collecting dependencies info");
        dependencies = new HashSet<IvyDependency>();
        return true;
    }

    /**
     * Mojos perform different dependency resolution, so we add dependencies for each mojo.
     */
    /*
    @Override
    public boolean postExecute(IvyBuildProxy build, BuildEvent event, MojoInfo mojo, BuildListener listener,
            Throwable error) {
        //listener.getLogger().println("[MavenDependenciesRecorder] mojo: " + mojo.getClass() + ":" + mojo.getGoal());
        //listener.getLogger().println("[MavenDependenciesRecorder] dependencies: " + pom.getArtifacts());
        recordMavenDependencies(pom.getArtifacts());
        return true;
    }
    */
    /**
     * Sends the collected dependencies over to the master and record them.
     */
    @Override
    public boolean postBuild(IvyBuildProxy build, BuildEvent event, BuildListener listener)
            throws InterruptedException, IOException {
    	build.executeAsync(new BuildCallable<Void, IOException>() {
            // record is transient, so needs to make a copy first
            private final Set<IvyDependency> d = dependencies;

            public Void call(IvyBuild build) throws IOException, InterruptedException {
                // add the action
                //TODO: [by yl] These actions are persisted into the build.xml of each build run - we need another
                //context to store these actions
                build.getActions().add(new IvyDependenciesRecord(build, d));
                return null;
            }
        });
        return true;
    }

    private void recordMavenDependencies(Set<Artifact> artifacts) {
        if (artifacts != null) {
            for (Artifact dependency : artifacts) {
                if (dependency.isResolved() && dependency.getFile() != null) {
                    IvyDependency ivyDependency = new IvyDependency();
                    ivyDependency.id = dependency.getId();
                    //ivyDependency.groupId = dependency.getGroupId();
                    //ivyDependency.artifactId = dependency.getArtifactId();
                    //ivyDependency.version = dependency.getVersion();
                    //ivyDependency.classifier = dependency.getClassifier();
                    //ivyDependency.scope = dependency.getScope();
                    //ivyDependency.fileName = dependency.getFile().getName();
                    //ivyDependency.type = dependency.getType();
                    dependencies.add(ivyDependency);
                }
            }
        }
    }

    @Extension
    public static final class DescriptorImpl extends IvyReporterDescriptor {
        @Override
        public String getDisplayName() {
            return "Record Maven Dependencies";
        }

        @Override
        public IvyReporter newAutoInstance(IvyModule module) {
            return new IvyDependenciesRecorder();
        }
    }

    private static final long serialVersionUID = 1L;
}
