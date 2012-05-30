/**
 * 
 */
package org.whitesource.agent.jenkins;

import hudson.Util;
import hudson.util.FormValidation;

/**
 * @author c_rsharv
 *
 */
public class WhiteSourceUtils {
	
	public static FormValidation validateWssUrl(final String wssUrl) {
        final String t = Util.fixEmptyAndTrim(wssUrl);
        if (t == null) {
            return FormValidation.error("Required.");
        }
        return FormValidation.ok();
    }
	
	public static FormValidation validateOrgToken(final String orgToken) {
        final String t = Util.fixEmptyAndTrim(orgToken);
        if (t == null) {
            return FormValidation.error("Required.");
        }
        return FormValidation.ok();
    }
}
