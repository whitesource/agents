/**
 * 
 */
package org.whitesource.agent.jenkins;

import hudson.Util;
import java.io.Serializable;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author c_rsharv
 *
 */
public class WhiteSourceConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String wssUrl;
	private final String orgToken;
	
	@DataBoundConstructor
	public WhiteSourceConfiguration(String wssUrl, String orgToken) {
		super();
		this.wssUrl = Util.fixEmptyAndTrim(wssUrl);
		this.orgToken = Util.fixEmptyAndTrim(orgToken);
	}
	
	public String getWssUrl() {
		return wssUrl;
	}
	
	public String getOrgToken() {
		return orgToken;
	}
	
	
}
