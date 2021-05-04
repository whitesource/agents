package org.whitesource.agent.api.dispatch;

import org.whitesource.agent.api.model.VulnerabilityInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Result for {@link CheckVulnerabilitiesRequest}.
 *
 * @author ruslan.gogerman
 *
 * @since 2.5.5
 */
public class CheckVulnerabilitiesResult implements Serializable {

    /* --- Static Members --- */

    private static final long serialVersionUID = - 6580191828869055056L;

    /* --- Members --- */

    private String depSha1;
    private Map<String, Collection<VulnerabilityInfo>> sha1ToVulnerabilitiesMap;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public CheckVulnerabilitiesResult() {
        sha1ToVulnerabilitiesMap = new HashMap<>();
    }

    public CheckVulnerabilitiesResult(Map<String, Collection<VulnerabilityInfo>> sha1ToVulnerabilitiesMap) {
        this.sha1ToVulnerabilitiesMap = sha1ToVulnerabilitiesMap;
    }

    /* --- Getters / Setters --- */

    public Map<String, Collection<VulnerabilityInfo>> getSha1ToVulnerabilitiesMap() {
        return sha1ToVulnerabilitiesMap;
    }

    public void setSha1ToVulnerabilitiesMap(Map<String, Collection<VulnerabilityInfo>> sha1ToVulnerabilitiesMap) {
        this.sha1ToVulnerabilitiesMap = sha1ToVulnerabilitiesMap;
    }

    public String getDepSha1() {
        return depSha1;
    }

    public void setDepSha1(String depSha1) {
        this.depSha1 = depSha1;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


}
