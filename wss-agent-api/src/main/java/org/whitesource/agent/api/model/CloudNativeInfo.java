package org.whitesource.agent.api.model;

import java.io.Serializable;
import java.util.List;

public class CloudNativeInfo implements Serializable {
    private String packageName;
    private String sourcePackageName;
    private String sourcePackageVersion;
    private String distribution;
    private String distributionVersion;
    private List<VulnerabilityMetadata> vulnerabilitiesMetadata;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public CloudNativeInfo() {}


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSourcePackageName() {
        return sourcePackageName;
    }

    public void setSourcePackageName(String sourcePackageName) {
        this.sourcePackageName = sourcePackageName;
    }

    public String getSourcePackageVersion() {
        return sourcePackageVersion;
    }

    public void setSourcePackageVersion(String sourcePackageVersion) {
        this.sourcePackageVersion = sourcePackageVersion;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getDistributionVersion() {
        return distributionVersion;
    }

    public void setDistributionVersion(String distributionVersion) {
        this.distributionVersion = distributionVersion;
    }

    public List<VulnerabilityMetadata> getVulnerabilitiesMetadata() {
        return vulnerabilitiesMetadata;
    }

    public void setVulnerabilitiesMetadata(List<VulnerabilityMetadata> vulnerabilitiesMetadata) {
        this.vulnerabilitiesMetadata = vulnerabilitiesMetadata;
    }
}
