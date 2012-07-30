package org.whitesource.api.client;

/**
 * WhiteSource service API client constants.
 * 
 * @author tom.shapira
 */
public final class ClientConstants {
	
	/* --- Service info --- */

    public static final String SERVICE_URL_KEYWORD = "wss.url";

	public static final String DEFAULT_SERVICE_URL = "http://saas.whitesourcesoftware.com/agent";
	
	public static final String APPLICATION_JSON = "application/json";
	
	/* --- Constructors --- */
	
	/**
	 * Private default constructor
	 */
	private ClientConstants() {
		// avoid instantiation
	}

}