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
package org.whitesource.agent.hash;


/**
 * Exception for errors encountered while interacting with the WhiteSource service. 
 * 
 * @author Edo.Shor
 */
public class WssHashException extends Exception {

	/* --- Static members --- */

	private static final long serialVersionUID = -6703395740692354154L;

	/* --- Constructors --- */

	public WssHashException(){
		super();
	}

	public WssHashException(String message){
		super(message);
	}

	public WssHashException(Throwable cause){
		super(cause);
	}

	public WssHashException(String message, Throwable cause){
		super(message, cause);
	}
	
}
