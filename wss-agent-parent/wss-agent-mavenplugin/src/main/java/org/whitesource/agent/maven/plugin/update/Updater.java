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
package org.whitesource.agent.maven.plugin.update;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.whitesource.agent.api.dispatch.UpdateInventoryResult;


/**
 * The interface describe the functionality to be exposed by a maven project updater to the white source service. 
 * 
 * @author tom.shapira
 */
public interface Updater {
	
	/**
	 * The method perform the entire update procedure. 
	 * 
	 * @return Update procedure result.
	 * 
	 * @throws MojoExecutionException In case of errors during the update.
	 */
	UpdateInventoryResult update() throws MojoExecutionException;
	
	/**
	 * The method set the log to be used during the update process.
	 * 
	 * @param log
	 */
	void setLog(Log log);
}
