package org.whitesource.agent.api.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * @author chen.luigi
 */
public class ScanSummaryInfo implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -3801913583253812987L;

    /* --- Private Members --- */

    private long totalElapsedTime;
    private Collection<StepInfo> stepsSummaryInfo;

    /* --- Constructors --- */

    public ScanSummaryInfo() {
        stepsSummaryInfo = new LinkedList<>();
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
}
