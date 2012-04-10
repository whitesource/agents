package org.whitesource.agent.api.dispatch;

import java.io.IOException;
import java.io.Serializable;

import org.whitesource.agent.api.APIConstants;
import org.whitesource.agent.api.JsonUtils;


/**
 * Wrapper for any response form the White Source service. 
 * 
 * @author Edo.Shor
 */
public class ResultEnvelope implements Serializable {
	
	/* --- Static members --- */
	
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_BAD_REQUEST = 2;
	public static final int STATUS_SERVER_ERROR = 3;
	
	public static final String MESSAGE_OK = "ok";
	public static final String MESSAGE_ILLEGAL_ARGUMENTS = "Illegal arguments";
	public static final String MESSAGE_SERVER_ERROR = "Server error";
	
	private static final long serialVersionUID = -3835912575385728376L;
	
	/* --- Members --- */
	
	private String envelopeVersion = APIConstants.API_VERSION;
	
	/** Status code of the operation. */
	private int status;
	
	/** Human readable message. */
	private String message;
	
	/** Data associated with the result */
	private String data;
	
	/* --- Constructors --- */

	/**
	 * Default constructor
	 */
	public ResultEnvelope() {
		
	}
	
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
		
		sb.append("ResultEnvelope@").append(Integer.toHexString(hashCode())).append("[")
		.append("\n").append("envelopeVersion=").append(envelopeVersion).append(",")
		.append("\n").append("status=").append(status).append(",")
		.append("\n").append("message=").append(message).append(",")
		.append("\n").append("data=").append(data)
		.append("\n]");
		
		return sb.toString();
	}
	
	/* --- Static methods --- */
	
	public static ResultEnvelope fromJSON(String json) throws IOException {
		return JsonUtils.fromJson(json, ResultEnvelope.class);
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
