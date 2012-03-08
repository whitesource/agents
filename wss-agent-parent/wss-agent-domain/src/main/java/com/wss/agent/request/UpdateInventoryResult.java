package com.wss.agent.request;

import com.wss.agent.exception.JsonParsingException;
import com.wss.agent.utils.JsonUtils;

public class UpdateInventoryResult {
	
	/* --- Members --- */
	
	private String result;
	
	/* --- Constructors --- */
	
	/**
	 * Default constructor (for JSON parsing)
	 * 
	 */
	public UpdateInventoryResult() {
	}
	
	/**
	 * Constructor
	 * 
	 * @param properties
	 */
	public UpdateInventoryResult(String result) {
		this.result = result;
	}

	/* --- Static methods --- */
	
	public static UpdateInventoryResult fromJSON(String json) throws JsonParsingException {
		return JsonUtils.parseUpdateInventoryResultJson(json);
	}
	
	/* --- Getters / Setters --- */ 
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}