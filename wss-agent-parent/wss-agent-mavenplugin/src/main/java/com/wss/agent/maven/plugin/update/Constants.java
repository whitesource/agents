package com.wss.agent.maven.plugin.update;

/**
 * Constants used by {@link UpdateMojo} 
 * 
 * @author tom.shapira
 *
 */
public final class Constants {
	
	/* --- Maven plugin--- */
	
	public static final String PLUGIN_KEY = "com.wss:whitesource-maven-plugin";
	
	public static final String PROJECT_TOKEN = "projectToken";
	
	public static final String AGENT_VERSION = "1.0";
	
	/* --- Servlet info --- */

	// TODO change
	public static final String SERVICE_ENDPOINT_URL = "http://localhost:8888/agent/update";
	
	public static final String APPLICATION_JSON = "application/json";
	
	/* --- Stand alone pom --- */
	
	public static final String STANDALONE_POM_GROUP_ID = "org.apache.maven";
	
	public static final String STANDALONE_POM_ARTIFACT_ID = "standalone-pom";
	
	public static final String STANDALONE_POM_VERSION = "1";
	
	/* --- Error messages --- */
	
	public static final String ERROR_NOT_PROJECT_FOLDER = "Not a project folder";
	
	public static final String ERROR_NO_PROJECT = "No project found";
	
	public static final String ERROR_MISSING_TOKEN = "Token property missing in plugin configuration";
	
	public static final String ERROR_JSON_PARSING = "Unexpected error";
	
	public static final String ERROR_CONNECTION = "No connection / Problem with server. Please contact White Source for assistance";
	
	public static final String ERROR_HTTP = "HTTP request ended unsuccessfuly";

	/* --- Maven log messages --- */
	
	public static final String SKIP_UPDATE = "Skipping White Source update";
	
	public static final String DEBUG_REQUEST_BUILT = "Request created successfully";
	
	public static final String DEBUG_FOUND_PROJECT = "Found project ";
	
	public static final String DEBUG_PROPERTIES_ACQUIRE = "Acquiring properties";
	
	public static final String DEBUG_PROPERTIES_RECEIVED = "Received properties";
	
	public static final String DEBUG_UPDATE_SEND = "Sending update request";
	
	public static final String DEBUG_UPDATE_SUCCESS = "Update successful";
	

	/* --- Constructors --- */
	
	/**
	 * Private default constructor
	 */
	private Constants() {
		// avoid instantiation
	}

}
