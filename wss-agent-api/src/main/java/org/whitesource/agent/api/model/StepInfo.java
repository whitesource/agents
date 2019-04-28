package org.whitesource.agent.api.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * @author chen.luigi
 */
public class StepInfo implements Serializable {

    /* --- Static Members --- */

    private static final long serialVersionUID = 7972930644057281908L;

    /* --- Private Members --- */

    private String stepName;
    private long totalElapsedTime;
    private boolean isSubStep;
    private StepCompletionStatus stepCompletionStatus;
    private Collection<StepInfo> subStepsInfo;

    /* --- Constructors --- */

    public StepInfo() {
        subStepsInfo = new LinkedList<>();
    }

    public StepInfo(String stepName, long totalElapsedTime, boolean isSubStep, StepCompletionStatus stepCompletionStatus, Collection<StepInfo> subStepsInfo) {
        this();
        this.stepName = stepName;
        this.totalElapsedTime = totalElapsedTime;
        this.isSubStep = isSubStep;
        this.stepCompletionStatus = stepCompletionStatus;
        this.subStepsInfo = subStepsInfo;
    }

    /* --- Overridden Methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StepInfo)) return false;
        StepInfo stepInfo = (StepInfo) o;
        return totalElapsedTime == stepInfo.totalElapsedTime &&
                isSubStep == stepInfo.isSubStep &&
                Objects.equals(stepName, stepInfo.stepName) &&
                stepCompletionStatus == stepInfo.stepCompletionStatus &&
                Objects.equals(subStepsInfo, stepInfo.subStepsInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepName, totalElapsedTime, isSubStep, stepCompletionStatus, subStepsInfo);
    }

    /* --- Getters / Setters --- */

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public long getTotalElapsedTime() {
        return totalElapsedTime;
    }

    public void setTotalElapsedTime(long totalElapsedTime) {
        this.totalElapsedTime = totalElapsedTime;
    }

    public boolean isSubStep() {
        return isSubStep;
    }

    public void setSubStep(boolean subStep) {
        isSubStep = subStep;
    }

    public StepCompletionStatus getStepCompletionStatus() {
        return stepCompletionStatus;
    }

    public void setStepCompletionStatus(StepCompletionStatus stepCompletionStatus) {
        this.stepCompletionStatus = stepCompletionStatus;
    }

    public Collection<StepInfo> getSubStepsInfo() {
        return subStepsInfo;
    }

    public void setSubStepsInfo(Collection<StepInfo> subStepsInfo) {
        this.subStepsInfo = subStepsInfo;
    }
}
