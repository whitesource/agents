package org.whitesource.agent.maven.plugin.service;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.whitesource.agent.api.dispatch.PropertiesRequest;
import org.whitesource.agent.api.dispatch.PropertiesResult;
import org.whitesource.agent.api.dispatch.ReportRequest;
import org.whitesource.agent.api.dispatch.ReportResult;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.dispatch.UpdateInventoryResult;

/**
 * The interface describes the functionality exposed by the wrapper to the WhiteSource agent service.
 * 
 * @author tom.shapira
 *
 */
public interface ServiceProvider {

	/**
	 * The method calls the WhiteSource service for properties.
	 * 
	 * @param request Properties request.
	 * 
	 * @return Properties result.
	 * 
	 * @throws MojoExecutionException In case an error occurred during the call to White Source server.
	 */
	PropertiesResult getProperties(PropertiesRequest request) throws MojoExecutionException;

	/**
	 * The method calls the WhiteSource service for inventory update.
	 * 
	 * @param request Inventory update request.
	 * 
	 * @return Inventory update result.
	 * 
	 * @throws MojoExecutionException In case an error occurred during the call to White Source server.
	 */
	UpdateInventoryResult updateInventory(UpdateInventoryRequest request) throws MojoExecutionException;

	/**
	 * The method calls the WhiteSource service for dependency analysis report.
	 * 
	 * @param request Report request.
	 * 
	 * @return Report result.
	 * 
	 * @throws MojoExecutionException In case an error occurred during the call to White Source server.
	 */
	ReportResult getReport(ReportRequest request) throws MojoExecutionException;

	/**
	 * Sets the log.
	 * 
	 * @param log Log.
	 */
	void setLog(Log log);
}
