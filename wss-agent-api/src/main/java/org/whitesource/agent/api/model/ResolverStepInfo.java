package org.whitesource.agent.api.model;

import java.util.Collection;
import java.util.Objects;

/**
 * @author chen.luigi
 */
public class ResolverStepInfo extends StepInfo {

    /* --- Private Members --- */

    private int totalUniqueDependenciesFound;
    private int totalDuplicateDependenciesFound;

    /* --- Constructors --- */

    public ResolverStepInfo(int totalUniqueDependenciesFound, int totalDuplicateDependenciesFound) {
        this.totalUniqueDependenciesFound = totalUniqueDependenciesFound;
        this.totalDuplicateDependenciesFound = totalDuplicateDependenciesFound;
    }

    public ResolverStepInfo(String stepName, long totalElapsedTime, boolean isSubStep, StepCompletionStatus stepCompletionStatus, Collection<StepInfo> subStepsInfo, int totalUniqueDependenciesFound, int totalDuplicateDependenciesFound) {
        super(stepName, totalElapsedTime, isSubStep, stepCompletionStatus, subStepsInfo);
        this.totalUniqueDependenciesFound = totalUniqueDependenciesFound;
        this.totalDuplicateDependenciesFound = totalDuplicateDependenciesFound;
    }

    /* --- Overridden methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResolverStepInfo)) return false;
        if (!super.equals(o)) return false;
        ResolverStepInfo that = (ResolverStepInfo) o;
        return totalUniqueDependenciesFound == that.totalUniqueDependenciesFound &&
                totalDuplicateDependenciesFound == that.totalDuplicateDependenciesFound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalUniqueDependenciesFound, totalDuplicateDependenciesFound);
    }

    /* --- Getters / Setters --- */

    public int getTotalUniqueDependenciesFound() {
        return totalUniqueDependenciesFound;
    }

    public void setTotalUniqueDependenciesFound(int totalUniqueDependenciesFound) {
        this.totalUniqueDependenciesFound = totalUniqueDependenciesFound;
    }

    public int getTotalDuplicateDependenciesFound() {
        return totalDuplicateDependenciesFound;
    }

    public void setTotalDuplicateDependenciesFound(int totalDuplicateDependenciesFound) {
        this.totalDuplicateDependenciesFound = totalDuplicateDependenciesFound;
    }
}
