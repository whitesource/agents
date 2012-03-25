package org.whitesource.agent.api.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.whitesource.agent.api.JsonParsingException;
import org.whitesource.agent.api.JsonUtils;


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
	
	/* --- Static methods--- */
	
	public static ReportResult fromJSON(String json) throws JsonParsingException {
		return JsonUtils.fromJson(json, ReportResult.class);
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