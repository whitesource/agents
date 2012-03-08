package com.wss.agent.request;

import java.util.Properties;

import com.wss.agent.exception.JsonParsingException;
import com.wss.agent.utils.JsonUtils;

/**
 * This class represents the properties needed for executing the update operation.
 * 
 * @author tom.shapira
 *
 */
public class PropertiesResult {
	
	/* --- Members --- */
	
	private Properties properties;
	
	/* --- Constructors --- */

	/**
	 * Default constructor (for JSON parsing)
	 * 
	 */
	public PropertiesResult() {
	}
	
	/**
	 * Constructor
	 * 
	 * @param properties
	 */
	public PropertiesResult(Properties properties) {
		this.properties = properties;
	}
	
	/* --- Static methods--- */
	
	public static PropertiesResult fromJSON(String json) throws JsonParsingException {
		return JsonUtils.parsePropertiesJson(json);
	}

	/* --- Getters / Setters --- */
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
