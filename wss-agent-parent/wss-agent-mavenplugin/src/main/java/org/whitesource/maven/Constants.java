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
package org.whitesource.maven;

/**
 * Constants used by plugin.
 * 
 * @author tom.shapira
 */
public final class Constants {
	
	/* --- Configuration --- */

	public static final String AGENT_TYPE = "maven-plugin";
	public static final String AGENT_VERSION = "1.0";

    public static final String PLUGIN_PREFIX = "org.whitesource.";
    public static final String ORG_TOKEN = PLUGIN_PREFIX + "orgToken";
	public static final String PROJECT_TOKEN = PLUGIN_PREFIX + "projectToken";
    public static final String MODULE_TOKENS = PLUGIN_PREFIX + "moduleTokens";
    public static final String IGNORE = PLUGIN_PREFIX + "ignore";
    public static final String INCLUDES = PLUGIN_PREFIX + "includes";
    public static final String EXCLUDES = PLUGIN_PREFIX + "excludes";
    public static final String IGNORE_POM_MODULES = PLUGIN_PREFIX + "ignorePomModules";

	/* --- Errors --- */

    public static final String ERROR_SERVICE_CONNECTION = "Error communicating with service: ";
	public static final String ERROR_SHA1 = "Error calculating SHA-1";

	/* --- Constructors --- */
	
	/**
	 * Private default constructor
	 */
	private Constants() {
		// avoid instantiation
	}

}