package org.whitesource.agent.api.model;

import java.util.Objects;

/**
 * @author chen.luigi
 */
public class FilesMathchingStepInfo extends StepInfo {

    /* --- Private Members --- */

    private int totalSourceBinariesFound;

    /* --- Constructors --- */

    public FilesMathchingStepInfo(int totalSourceBinariesFound) {
        super();
        this.totalSourceBinariesFound = totalSourceBinariesFound;
    }

    /* --- Overridden methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilesMathchingStepInfo)) return false;
        if (!super.equals(o)) return false;
        FilesMathchingStepInfo that = (FilesMathchingStepInfo) o;
        return totalSourceBinariesFound == that.totalSourceBinariesFound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalSourceBinariesFound);
    }

    /* --- Getters / Setters --- */

    public int getTotalSourceBinariesFound() {
        return totalSourceBinariesFound;
    }

    public void setTotalSourceBinariesFound(int totalSourceBinariesFound) {
        this.totalSourceBinariesFound = totalSourceBinariesFound;
    }
}
