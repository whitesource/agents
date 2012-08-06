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

import java.util.HashMap;
import java.util.Map;


/**
 * Result of the license distribution operation.
 * 
 * @author tom.shapira
 */
public class ReportResult {
	
	/* --- Members --- */
	
	private Map<String, Integer> licenseDistribution;
	
	private int numOfNewerVersions;
	
	/* --- Constructors --- */

	/**
	 * Default constructor (for JSON parsing)
	 * 
	 */
	public ReportResult() {
		licenseDistribution = new HashMap<String, Integer>();
	}
	
	/**
	 * Constructor
	 * 
	 * @param properties
	 */
	public ReportResult(Map<String, Integer> licenseDistribution, int numOfNewerVersions) {
		this.licenseDistribution = licenseDistribution;
		this.numOfNewerVersions = numOfNewerVersions;
	}
	
	/* --- Getters / Setters --- */
	
	public Map<String, Integer> getLicenseDistribution() {
		return licenseDistribution;
	}
	
	public void setLicenseDistribution(Map<String, Integer> licenseDistribution) {
		this.licenseDistribution = licenseDistribution;
	}

	public int getNumOfNewerVersions() {
		return numOfNewerVersions;
	}

	public void setNumOfNewerVersions(int numOfNewerVersions) {
		this.numOfNewerVersions = numOfNewerVersions;
	}
	
}