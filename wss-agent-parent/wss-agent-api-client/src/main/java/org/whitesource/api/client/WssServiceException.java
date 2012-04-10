package org.whitesource.api.client;


/**
 * Exception for errors encountered while interacting with the WhiteSource service. 
 * 
 * @author Edo.Shor
 */
public class WssServiceException extends Exception {

	/* --- Static members --- */

	private static final long serialVersionUID = -6703395740692354154L;
	
	/* --- Constructors --- */

	public WssServiceException(){
		super();
	}
	
	public WssServiceException(String message){
		super(message);
	}
	
	public WssServiceException(Throwable cause){
		super(cause);
	}
	
	public WssServiceException(String message, Throwable cause){
		super(message, cause);
	}
	
}
