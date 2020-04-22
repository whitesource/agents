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
package org.whitesource.agent.client;


/**
 * Exception for errors encountered while interacting with the WhiteSource service. 
 * 
 * @author Edo.Shor
 */
public class WssServiceException extends Exception {

	/* --- Static members --- */

	private static final long serialVersionUID = -6703395740692354154L;

	/* --- Members --- */

	private String requestToken;
	private int statusCode;

	/* --- Constructors --- */

	public WssServiceException(){
		super();
	}
	
	public WssServiceException(String message){
		super(message);
	}

	public WssServiceException(String message, String requestToken) {
		super(message);
		this.requestToken = requestToken;
	}

	public WssServiceException(Throwable cause){
		super(cause);
	}
	
	public WssServiceException(String message, Throwable cause){
		super(message, cause);
	}

	public WssServiceException(String message, Throwable cause, int statusCode) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	/* --- Getters / Setters --- */

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
