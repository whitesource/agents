/**
 * 
 */
package org.whitesource.agent.jenkins.ivy;

import hudson.ivy.IvyBuild;
import hudson.model.Action;

import java.util.Set;

/**
 * @author c_rsharv
 * 
 */
public class IvyDependenciesRecord implements Action {

	private final IvyBuild build;
    private final Set<IvyDependency> dependencies;

    public IvyDependenciesRecord(IvyBuild build, Set<IvyDependency> dependencies) {
        this.build = build;
        this.dependencies = dependencies;
    }
	
	
	public String getIconFileName() {
		return null;
	}

	public String getDisplayName() {
		return null;
	}

	public String getUrlName() {
		return null;
	}

	public Set<IvyDependency> getDependencies() {
        return dependencies;
    }
}
