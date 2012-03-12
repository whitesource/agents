package com.wss.agent.constants;

/**
 * 
 * @author tom.shapira
 */
public final class AgentConstants {
	
	public static final String API_VERSION = "1.0";
	
	/* --- Parameters --- */
	
	public static final String PARAM_TOKEN = "token";
	
	public static final String PARAM_TIME_STAMP = "timeStamp";
	
	public static final String PARAM_PROPERTIES = "properties";
	
	public static final String PARAM_DIFF = "diff";
	
	public static final String PARAM_AGENT_VERSION = "agentVersion";
	
	public static final String PARAM_REQUEST_TYPE = "type";
	
	/* --- Messages --- */
	
	public static final String TOKEN_INVALID = "Invalid token";
	
	public static final String TIME_STAMP_INVALID = "Invalid request time";
	
	public static final String DIFF_INVALID = "Invalid diff";
	
	public static final String UPDATE_SUCCESS = "update success";
	
	public static final String JSON_ERROR = "Problem parsing json";
	
	/* --- Constructors --- */
	
	/**
	 * Private default constructor
	 */
	private AgentConstants() {
		// avoid instantiation
	}

}
