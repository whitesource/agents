package org.whitesource.agent.api.dispatch;

import java.io.Serializable;

/**
 * The interface describe the functionality to be exposed by any request to the white source service.
 * 
 * @author Edo.Shor
 *
 * @param <R> Result type
 */
public interface ServiceRequest<R> extends Serializable {

	/**
	 * @return Request type.
	 */
	RequestType getType();
	
	/**
	 * @return White Source service token for the organization.
	 */
	String getOrgToken();

	/**
	 * @return Time stamp when the request created (client side)
	 */
	long getTimeStamp() ;
	
}
