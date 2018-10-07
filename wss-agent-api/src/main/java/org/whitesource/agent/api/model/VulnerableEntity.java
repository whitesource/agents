package org.whitesource.agent.api.model;

import java.io.Serializable;

/**
 * @author chen.luigi
 */
public class VulnerableEntity implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 6521642112001095631L;

    /* --- Private Members --- */

    private String element;
    private int startLine;
    private int endLine;

    /* --- Constructors --- */

    public VulnerableEntity(String element, int startLine, int endLine) {
        this.element = element;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    /* --- Getters / Setters --- */

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }
}
