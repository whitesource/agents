package com.wss.agent.maven.plugin.update;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import com.wss.agent.request.UpdateInventoryResult;

/**
 * This class sends the update request to White Source.
 * 
 * @author tom.shapira
 *
 */
public interface Updater {
	
	/* --- Public methods --- */
	
//	void getProperties(String token) throws MojoExecutionException;
	
//	void update(UpdateInventoryRequest agentRequest) throws MojoExecutionException;
	
	UpdateInventoryResult update() throws MojoExecutionException;
	
	void setLog(Log log);
}
