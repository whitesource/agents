package com.wss.agent.request;

/**
 * This class represents the properties needed for executing the update operation.
 * 
 * @author tom.shapira
 *
 */
public class PropertiesResult {
	
	/* --- Members --- */
	
	String property;
	
	/* --- Constructors --- */
	
	public PropertiesResult(String property) {
		this.property = property;
	}

	/* --- Getters / Setters --- */
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
