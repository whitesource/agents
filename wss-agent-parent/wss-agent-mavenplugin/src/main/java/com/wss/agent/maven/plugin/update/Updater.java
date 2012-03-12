package com.wss.agent.maven.plugin.update;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import com.wss.agent.api.UpdateInventoryResult;

/**
 * The interface describe the functionality to be exposed by a maven project updater to the white source service. 
 * 
 * @author tom.shapira
 */
public interface Updater {
	
	/**
	 * The method perform the entire update procedure. 
	 * 
	 * @return Update procedure result.
	 * 
	 * @throws MojoExecutionException In case of errors during the update.
	 */
	UpdateInventoryResult update() throws MojoExecutionException;
	
	/**
	 * The method set the log to be used during the update process.
	 * 
	 * @param log
	 */
	void setLog(Log log);
}
