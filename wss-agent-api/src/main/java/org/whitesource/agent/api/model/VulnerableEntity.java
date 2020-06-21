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

    //since 2.9.9.51
    private String namespace;
    private String className;
    private String method;
    private String language;
    private String type;
    private String extraData;

    /* --- Constructors --- */

    public VulnerableEntity(String element, int startLine, int endLine) {
        this.element = element;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public VulnerableEntity(String element, int startLine, int endLine, String namespace, String className,
                            String method, String language, String type, String extraData) {
        this(element, startLine, endLine);
        this.namespace = namespace;
        this.className = className;
        this.method = method;
        this.language = language;
        this.type = type;
        this.extraData = extraData;
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

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
}
