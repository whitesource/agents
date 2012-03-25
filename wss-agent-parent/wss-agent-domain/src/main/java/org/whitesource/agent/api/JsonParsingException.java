package org.whitesource.agent.api;

/**
 * Exception thrown when an error occurs during JSON parsing.
 * 
 * @author tom.shapira
 *
 */
public class JsonParsingException extends Exception {

	/* --- Static members --- */

	private static final long serialVersionUID = -9160357951934576459L;

	/* --- Constructors --- */
	
	public JsonParsingException(String message){ 
		super(message); 
	} 

	public JsonParsingException(String message, Throwable t){ 
		super(message, t); 
	}
	
	public JsonParsingException(Throwable t){ 
		super(t); 
	}
}
