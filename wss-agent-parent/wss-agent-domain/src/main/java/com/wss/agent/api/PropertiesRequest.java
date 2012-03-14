package com.wss.agent.api;

/**
 * Request for service properties.
 * 
 * @author Edo.Shor
 */
public class PropertiesRequest extends BaseRequest<PropertiesResult> {

	/* --- Static members --- */
	
	private static final long serialVersionUID = 1390477474899005404L;
	
	/* --- Constructors --- */
	
	/**
	 * Default constructor
	 */
	public PropertiesRequest() {
		this(null);
	}
	
	/**
	 * Constructor
	 * 
	 * @param orgToken
	 */
	public PropertiesRequest(String orgToken) {
		super(RequestType.PROPERTIES, orgToken);
	}
	
}
