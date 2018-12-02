package org.whitesource.agent.via.api;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by anna.rozin
 */
public class VulnerableElementInfo implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 7249655682041302822L;

    /* --- Members --- */

    private Integer startLine;
    private Integer endLine;

    /* --- Constructor --- */

    public VulnerableElementInfo(){
    }

    public VulnerableElementInfo(Integer startLine, Integer endLine){
        this.startLine = startLine;
        this.endLine = endLine;
    }

    /* --- Overridden methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VulnerableElementInfo that = (VulnerableElementInfo) o;
        return startLine == that.startLine &&
                endLine == that.endLine;
    }

    @Override
    public int hashCode() {

        return Objects.hash(startLine, endLine);
    }
    /* --- Getters / Setters --- */

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getEndLine() {
        return endLine;
    }

    public void setEndLine(Integer endLine) {
        this.endLine = endLine;
    }
}
