/**
 * 
 */
package org.whitesource.agent.jenkins;

import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author c_rsharv
 * 
 */
public class LibraryLocation implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String libLocation;

	@DataBoundConstructor
	public LibraryLocation(String libLocation) {
		this.libLocation = libLocation;
	}

	public String getLibLocation() {
		return libLocation;
	}

}
