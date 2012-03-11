package com.wss.agent.constants;

/**
 * 
 * @author tom.shapira
 */
public class AgentConstants {
	
	/* --- Messages --- */
	
	public static final String TOKEN_INVALID = "Invalid token";
	
	public static final String TIME_STAMP_INVALID = "Invalid request time";
	
	public static final String DIFF_INVALID = "Invalid diff";
	
	public static final String UPDATE_SUCCESS = "update success";
	
	public static final String JSON_ERROR = "Problem parsing json";
	
	/* --- Parameters --- */
	
	public static final String PARAM_TOKEN = "token";
	
	public static final String PARAM_TIME_STAMP = "timeStamp";
	
	public static final String PARAM_PROPERTIES = "properties";
	
	public static final String PARAM_DIFF = "diff";
	
	public static final String PARAM_AGENT_VERSION = "agentVersion";
	
	public static final String PARAM_REQUEST_TYPE = "type";
	
	/* --- Constructors --- */
	
	/**
	 * Private default constructor
	 */
	private AgentConstants() {
		// avoid instantiation
	}

}
