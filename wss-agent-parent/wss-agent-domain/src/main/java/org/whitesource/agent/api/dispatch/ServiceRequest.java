package org.whitesource.agent.api.dispatch;

import java.io.Serializable;

/**
 * The interface describe the functionality to be exposed by any request to the WhiteSource service.
 * 
 * @author Edo.Shor
 *
 * @param <R> Result type
 */
public interface ServiceRequest<R> extends Serializable {

	/**
	 * @return Request type.
	 */
	RequestType type();
	
	/**
	 * @return Agent type identifier.
	 */
	String agent();
	
	/**
	 * @return Agent version.
	 */
	String agentVersion();

	/**
	 * @return WhiteSource service token for the organization.
	 */
	String orgToken();
	
	/**
	 * @return Time stamp when the request created (client side)
	 */
	long timeStamp() ;
	
}
