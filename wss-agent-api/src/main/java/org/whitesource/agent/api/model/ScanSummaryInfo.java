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
    private List<DependencyType> packageManagers;
    private List<ScmTypes> sourceControlManagers;
    private List<ContainerRegistryTypes> containersRegistry;
    private List<OtherIntegrationsTypes> otherIntegrationsTypes;


    /* --- Constructors --- */

    public ScanSummaryInfo() {
        stepsSummaryInfo = new LinkedList<>();
        packageManagers = new LinkedList<>();
        containersRegistry = new LinkedList<>();
        sourceControlManagers = new LinkedList<>();
        otherIntegrationsTypes = new LinkedList<>();
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

    public List<DependencyType> getPackageManagers() {
        return packageManagers;
    }

    public void setPackageManagers(List<DependencyType> packageManagers) {
        this.packageManagers = packageManagers;
    }

    public List<ContainerRegistryTypes> getContainersRegistry() {
        return containersRegistry;
    }

    public void setContainersRegistry(List<ContainerRegistryTypes> containersRegistry) {
        this.containersRegistry = containersRegistry;
    }

    public List<ScmTypes> getSourceControlManagers() {
        return sourceControlManagers;
    }

    public void setSourceControlManagers(List<ScmTypes> sourceControlManagers) {
        this.sourceControlManagers = sourceControlManagers;
    }

    public List<OtherIntegrationsTypes> getOtherIntegrationsTypes() {
        return otherIntegrationsTypes;
    }

    public void setOtherIntegrationsTypes(List<OtherIntegrationsTypes> otherIntegrationsTypes) {
        this.otherIntegrationsTypes = otherIntegrationsTypes;
    }
}
