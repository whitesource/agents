package org.whitesource.agent.jenkins.plugin;

/**
 * Constants used by plugins. 
 * 
 * @author tom.shapira
 *
 */
public final class Constants {
	
	/* --- Maven plugin--- */
	
	public static final String PLUGIN_KEY = "org.whitesource:whitesource-maven-plugin";
	
	public static final String PROJECT_TOKEN = "projectToken";
	
	public static final String AGENT_TYPE = "jenkins";
	
	public static final String AGENT_VERSION = "1.0";
	
	public static final String SHA1 = "SHA-1";
	
	/* --- Error messages --- */
	
	public static final String ERROR_NOT_PROJECT_FOLDER = "Not a project folder";
	
	public static final String ERROR_NO_PROJECT = "No project found";
	
	public static final String ERROR_MISSING_TOKEN = "Token property missing in plugin configuration";
	
	public static final String ERROR_JSON_PARSING = "Unexpected error";
	
	public static final String ERROR_CONNECTION = "No connection / Problem with server. Please contact White Source for assistance";
	
	public static final String ERROR_HTTP = "HTTP request ended unsuccessfuly";
	
	public static final String ERROR_SHA1 = "Error calculating SHA-1";

	/* --- Maven log messages --- */
	
	public static final String INFO_DOMAIN = "Inventory update results for organization ";
	
	public static final String INFO_PROJECTS_UPDATED = "The following projects have been updated: ";
	
	public static final String INFO_PROJECTS_CREATED = "The following projects have been created: ";
	
	public static final String INFO_NO_PROJECTS_UPDATED = "No projects were updated";
	
	public static final String INFO_EMAIL_MESSAGE = "Each project admin will receive an email once the update process has been completed";
	
	public static final String INFO_NO_RESULTS = "No results found";
	
	public static final String INFO_NO_DEPENDENCIES = "No dependencies were found";
	
	public static final String INFO_LICENSE_ANALYSIS = "White Source License Report:";
	
	public static final String INFO_CONTACT = "For more information visit www.whitesourcesoftware.com";
	
	public static final String INFO_NEWER_VERSIONS_FORMAT = "Found {0} libraries that have newer versions.";

	public static final String INFO_REPORT_DIRECTORY = "White Source report directory: ";
	
	public static final String INFO_SKIP_UPDATE = "Skipping White Source update";
	
	public static final String INFO_SKIP_LICENSES = "Skipping White Source license report";
	
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