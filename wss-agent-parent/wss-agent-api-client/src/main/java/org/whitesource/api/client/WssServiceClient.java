package org.whitesource.api.client;

import org.whitesource.agent.api.dispatch.PropertiesRequest;
import org.whitesource.agent.api.dispatch.PropertiesResult;
import org.whitesource.agent.api.dispatch.ReportRequest;
import org.whitesource.agent.api.dispatch.ReportResult;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.dispatch.UpdateInventoryResult;

/**
 * The interface describes the functionality to be exposed by a client implementation to the WhiteSource agent service.
 * 
 * @author Edo.Shor
 * @author Tom.Shapira
 */
public interface WssServiceClient {

	/**
	 * The method calls the WhiteSource service for properties.
	 * 
	 * @param request Properties request.
	 * 
	 * @return Properties result.
	 * 
	 * @throws WssServiceException In case an error occurred during the call to White Source server.
	 */
	PropertiesResult getProperties(PropertiesRequest request) throws WssServiceException;

	/**
	 * The method calls the WhiteSource service for inventory update.
	 * 
	 * @param request Inventory update request.
	 * 
	 * @return Inventory update result.
	 * 
	 * @throws WssServiceException In case an error occurred during the call to White Source server.
	 */
	UpdateInventoryResult updateInventory(UpdateInventoryRequest request) throws WssServiceException;

	/**
	 * The method calls the WhiteSource service for dependency analysis report.
	 * 
	 * @param request Report request.
	 * 
	 * @return Report result.
	 * 
	 * @throws WssServiceException In case an error occurred during the call to White Source server.
	 */
	ReportResult getReport(ReportRequest request) throws WssServiceException;
	
	/**
	 * The method close all connections and release resources.
	 */
	void shutdown();
	
	/**
	 * The method configure the client to use a proxy specified by the given parameters.  
	 * 
	 * @param host Proxy host address.
	 * @param port Proxy port.
	 */
	void setProxy(String host, int port);

}
