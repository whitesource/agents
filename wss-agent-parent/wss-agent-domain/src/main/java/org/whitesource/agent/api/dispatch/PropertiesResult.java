package org.whitesource.agent.api.dispatch;

import java.util.Properties;

import org.whitesource.agent.api.JsonParsingException;
import org.whitesource.agent.api.JsonUtils;


/**
 * Result of the properties operation.
 * 
 * @author tom.shapira
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
		properties = new Properties();
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
		return JsonUtils.fromJson(json, PropertiesResult.class);
	}

	/* --- Getters / Setters --- */
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
