package org.whitesource.agent.api.model;

import java.io.Serializable;
import java.util.*;

/**
 * @author chen.luigi
 */
public class ScanSummaryInfo implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -3801913583253812987L;

    /* --- Private Members --- */

    private long totalElapsedTime;
    private Collection<StepInfo> stepsSummaryInfo;
    private List<String> packageManagers;
    private List<String> sourceControlManagers;
    private List<String> containerRegistries;
    private List<String> otherIntegrationTypes;
    private boolean isPrivileged;
    private ScanMethod scanMethod;
    private Map<String, String> scanStatistics;

    /* --- Constructors --- */

    public ScanSummaryInfo() {
        stepsSummaryInfo = new LinkedList<>();
        packageManagers = new LinkedList<>();
        containerRegistries = new LinkedList<>();
        sourceControlManagers = new LinkedList<>();
        otherIntegrationTypes = new LinkedList<>();
        scanStatistics = new HashMap<>();
    }

    public ScanSummaryInfo(long totalElapsedTime, Collection<StepInfo> stepsSummaryInfo) {
        this();
        this.totalElapsedTime = totalElapsedTime;
        this.stepsSummaryInfo = stepsSummaryInfo;
    }

    /* --- Overridden methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScanSummaryInfo)) return false;
        ScanSummaryInfo that = (ScanSummaryInfo) o;
        return totalElapsedTime == that.totalElapsedTime &&
                Objects.equals(stepsSummaryInfo, that.stepsSummaryInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalElapsedTime, stepsSummaryInfo);
    }

    /* --- Getters / Setters --- */

    public Map<String, String> getScanStatistics() {
        return scanStatistics;
    }

    public void setScanStatistics(Map<String, String> scanStatistics) {
        this.scanStatistics = scanStatistics;
    }

    public long getTotalElapsedTime() {
        return totalElapsedTime;
    }

    public void setTotalElapsedTime(long totalElapsedTime) {
        this.totalElapsedTime = totalElapsedTime;
    }

    public Collection<StepInfo> getStepsSummaryInfo() {
        return stepsSummaryInfo;
    }

    public void setStepsSummaryInfo(Collection<StepInfo> stepsSummaryInfo) {
        this.stepsSummaryInfo = stepsSummaryInfo;
    }

    public List<String> getPackageManagers() {
        return packageManagers;
    }

    public void setPackageManagers(List<String> packageManagers) {
        this.packageManagers = packageManagers;
    }

    public List<String> getSourceControlManagers() {
        return sourceControlManagers;
    }

    public void setSourceControlManagers(List<String> sourceControlManagers) {
        this.sourceControlManagers = sourceControlManagers;
    }

    public List<String> getContainerRegistries() {
        return containerRegistries;
    }

    public void setContainerRegistries(List<String> containerRegistries) {
        this.containerRegistries = containerRegistries;
    }

    public List<String> getOtherIntegrationTypes() {
        return otherIntegrationTypes;
    }

    public void setOtherIntegrationTypes(List<String> otherIntegrationTypes) {
        this.otherIntegrationTypes = otherIntegrationTypes;
    }

    public boolean isPrivileged() {
        return isPrivileged;
    }

    public void setPrivileged(boolean privileged) {
        isPrivileged = privileged;
    }

    public ScanMethod getScanMethod() {
        return scanMethod;
    }

    public void setScanMethod(ScanMethod scanMethod) {
        this.scanMethod = scanMethod;
    }
}
