package org.whitesource.agent.api;

/**
 * A class to hold constant values used in WhiteSource agents.
 * 
 * @author tom.shapira
 */
public final class APIConstants {
	
	public static final String API_VERSION = "1.0";
	
	/* --- Parameters --- */
	
	public static final String PARAM_TOKEN = "token";
	public static final String PARAM_TIME_STAMP = "timeStamp";
	public static final String PARAM_PROPERTIES = "properties";
	public static final String PARAM_DIFF = "diff";
	public static final String PARAM_DEPENDENCIES = "dependencies";
	public static final String PARAM_AGENT = "agent";
	public static final String PARAM_AGENT_VERSION = "agentVersion";
	public static final String PARAM_REQUEST_TYPE = "type";
	
	/* --- Messages --- */
	
	public static final String TOKEN_INVALID = "Invalid token";
	public static final String TIME_STAMP_INVALID = "Invalid request time";
	public static final String DIFF_INVALID = "Invalid diff";
	public static final String UPDATE_SUCCESS = "update success";
	public static final String JSON_ERROR = "Problem parsing json";
	
	/* --- Miscellaneous --- */
	
	public static final int HASH_CODE_SEED = 133;
	public static final int HASH_CODE_FACTOR = 23;
	
	/* --- Constructors --- */
	
	/**
	 * Private default constructor
	 */
	private APIConstants() {
		// avoid instantiation
	}

}