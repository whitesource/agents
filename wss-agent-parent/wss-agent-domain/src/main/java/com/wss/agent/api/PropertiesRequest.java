package com.wss.agent.api;

public class PropertiesRequest extends BaseRequest<PropertiesResult> {

	public PropertiesRequest() {
		this(null);
	}
	
	public PropertiesRequest(String orgToken) {
		super(RequestType.PROPERTIES, orgToken);
	}
	
}
