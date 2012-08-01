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

import java.io.IOException;
import java.util.Properties;


/**
 * Result of the properties operation.
 * 
 * @author tom.shapira
 */
public class PropertiesResult {
	
	/* --- Members --- */
	
	private Properties properties;
	
	/* --- Constructors --- */

	/**
	 * Default constructor (for JSON parsing)
	 * 
	 */
	public PropertiesResult() {
		properties = new Properties();
	}
	
	/**
	 * Constructor
	 * 
	 * @param properties
	 */
	public PropertiesResult(Properties properties) {
		this.properties = properties;
	}

	/* --- Getters / Setters --- */
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
