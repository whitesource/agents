package com.wss.agent.request;

/**
 * Wrapper for any response form the White Source service. 
 * 
 * @author Edo.Shor
 */
public class ResultEnvelope {
	
	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_BAD_REQUEST = 1;
	public static final int STATUS_SERVER_ERROR = 2;
	
	public static final String MESSAGE_OK = "ok";
	public static final String MESSAGE_ILLEGAL_ARGUMENTS = "Illegal arguments";
	public static final String MESSAGE_SERVER_ERROR = "Server error";
	
	/* --- Members --- */
	
	private final String envelopeVersion = "1.0";
	
	/** Status code of the operation. */
	private int status;
	
	/** Human readable message. */
	private String message;
	
	/** Data associated with the result */
	private String data;
	
	/* --- constructors --- */

	/**
	 * Constructor
	 * 
	 * @param status
	 * @param message
	 * @param data
	 */
	public ResultEnvelope(int status, String message, String data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	/* --- Overridden methods --- */
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("ResultEnvelope@").append(hashCode()).append("[")
		.append("\n").append("envelopeVersion=").append(envelopeVersion).append(",")
		.append("\n").append("status=").append(status).append(",")
		.append("\n").append("message=").append(message).append(",")
		.append("\n").append("data=").append(data)
		.append("\n]");
		
		return sb.toString();
	}
	
	/* --- Getters / Setters --- */

	public String getEnvelopeVersion() {
		return envelopeVersion;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
