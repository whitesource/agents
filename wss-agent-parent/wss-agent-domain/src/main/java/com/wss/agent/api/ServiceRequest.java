package com.wss.agent.api;

/**
 * The interface describe the functionality to be exposed by any request to the white source service.
 * 
 * @author Edo.Shor
 *
 * @param <R> Result type
 */
public interface ServiceRequest<R> {

	/**
	 * @return Request type.
	 */
	RequestType getType();
	
	/**
	 * @return White Source service token for the organization.
	 */
	public String getOrgToken();

	/**
	 * @return Time stamp when the request created (client side)
	 */
	public long getTimeStamp() ;
	
}
