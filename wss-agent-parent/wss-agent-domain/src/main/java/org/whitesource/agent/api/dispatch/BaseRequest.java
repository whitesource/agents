package org.whitesource.agent.api.dispatch;

/**
 * Base, abstract, implementation of the interface.
 * 
 * @author Edo.Shor
 *
 * @param <R> Type of expected result. 
 */
public abstract class BaseRequest<R> implements ServiceRequest<R> {

	/* --- Static members --- */
	
	private static final long serialVersionUID = 4691829529579651426L;
	
	/* --- Members --- */

	protected final RequestType type;
	
	protected String agent;
	
	protected String agentVersion;
	
	protected String orgToken;
	
	protected long timeStamp;
	
	/* --- Constructors --- */

	/**
	 * Constructor
	 * 
	 * @param type Request operation type.
	 */
	public BaseRequest(RequestType type) {
		this(type, null, null);
	}
	
	/**
	 * Constructor
	 * 
	 * @param type Request operation type.
	 * @param agent Agent type identifier.
	 */
	public BaseRequest(RequestType type, String agent) {
		this(type, agent, null);
	}

	/**
	 * Constructor
	 * 
	 * @param type Request operation type.
	 * @param agent Agent type identifier.
	 * @param agentVersion Agent version.
	 */
	public BaseRequest(RequestType type, String agent, String agentVersion) {
		this.type  = type;
		this.agent = agent;
		this.agentVersion = agentVersion;
		this.timeStamp = System.currentTimeMillis();
	}

	/* --- Interface implementation methods --- */
	
	public RequestType type() {
		return type;
	}
	
	public String orgToken() {
		return orgToken;
	}
	
	public String agent() {
		return agent;
	}

	public String agentVersion() {
		return agentVersion;
	}

	public long timeStamp() {
		return timeStamp;
	}
	
	/* --- Getters / Setters --- */

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public void setAgentVersion(String agentVersion) {
		this.agentVersion = agentVersion;
	}

	public void setOrgToken(String orgToken) {
		this.orgToken = orgToken;
	}
	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

}
