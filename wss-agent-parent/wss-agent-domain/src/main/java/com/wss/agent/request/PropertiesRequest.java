package com.wss.agent.request;

public class PropertiesRequest {

	/* --- Members --- */
	
	String token;

	/* --- Constructors --- */
	
	public PropertiesRequest(String token) {
		this.token = token;
	}
	
	/* --- Getters / Setters --- */
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
