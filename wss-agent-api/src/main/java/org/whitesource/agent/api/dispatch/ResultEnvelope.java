/**
 * Copyright (C) 2012 White Source Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whitesource.agent.api.dispatch;

import org.whitesource.agent.api.APIConstants;

import java.io.Serializable;

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

	/** Identifier of the request for support purposes */
	private String requestToken;

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

	/**
	 * Constructor
	 *
	 * @param status
	 * @param message
	 * @param data
	 * @param requestToken
	 */
	public ResultEnvelope(int status, String message, String data, String requestToken) {
		this.status = status;
		this.message = message;
		this.data = data;
		this.requestToken = requestToken;
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
		.append("\n").append("requestToken=").append(requestToken)
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

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}
}