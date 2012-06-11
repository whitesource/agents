/**
 * 
 */
package org.whitesource.agent.jenkins.ivy;

import java.io.Serializable;

import org.whitesource.agent.jenkins.maven.MavenDependency;

/**
 * @author c_rsharv
 *
 */
public class IvyDependency implements Serializable {
	public String id;
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MavenDependency that = (MavenDependency) o;

        if (!id.equals(that.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
