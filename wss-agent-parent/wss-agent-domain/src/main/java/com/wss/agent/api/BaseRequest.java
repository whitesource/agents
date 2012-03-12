package com.wss.agent.api;

public class BaseRequest<R> implements ServiceRequest<R> {

	/* --- Static members --- */
	
	private static final long serialVersionUID = 4691829529579651426L;
	
	/* --- Members --- */

	private final RequestType type;
	
	private final String orgToken;
	
	private long timeStamp;
	
	/* --- Constructors --- */

	public BaseRequest(RequestType type, String orgToken) {
		this.type = type;
		this.orgToken = orgToken;
		
		this.timeStamp = System.currentTimeMillis();
	}
	
	/* --- Getters / Setters --- */

	public RequestType getType() {
		return type;
	}
	
	public String getOrgToken() {
		return orgToken;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

}
