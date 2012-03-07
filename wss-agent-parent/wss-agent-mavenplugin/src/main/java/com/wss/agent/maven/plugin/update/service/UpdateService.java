package com.wss.agent.maven.plugin.update.service;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import com.wss.agent.request.PropertiesRequest;
import com.wss.agent.request.PropertiesResult;
import com.wss.agent.request.UpdateInventoryRequest;
import com.wss.agent.request.UpdateInventoryResult;

public interface UpdateService {

	PropertiesResult getProperties(PropertiesRequest request) throws MojoExecutionException;
	
	UpdateInventoryResult updateInventory(UpdateInventoryRequest request) throws MojoExecutionException;
	
	void setLog(Log log);
}
