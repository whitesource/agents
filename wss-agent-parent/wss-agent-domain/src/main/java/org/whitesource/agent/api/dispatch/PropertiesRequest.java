package org.whitesource.agent.api.dispatch;

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
	 * Constructor
	 * 
	 * @param orgToken WhiteSource organization token.
	 */
	public PropertiesRequest(String orgToken) {
		super(RequestType.PROPERTIES);
		this.orgToken = orgToken;
	}
	
}
