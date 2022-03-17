package org.whitesource.agent.api.model;

import java.io.Serializable;

/**
 * @author chen.luigi
 */
public class ReferencesEntity implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 6521642112001095631L;

    /* --- Private Members --- */

    private String value;
    private String source;
    private String url;
    private boolean signature;
    private boolean advisory;
    private boolean patch;


    /* --- Constructors --- */

    public ReferencesEntity(String value, String source, String url, boolean signature, boolean advisory, boolean patch) {
        this.value = value;
        this.source = source;
        this.url = url;
        this.signature = signature;
        this.advisory = advisory;
        this.patch = patch;
    }

    /* --- Getters / Setters --- */

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSignature() {
        return signature;
    }

    public void setSignature(boolean signature) {
        this.signature = signature;
    }

    public boolean isAdvisory() {
        return advisory;
    }

    public void setAdvisory(boolean advisory) {
        this.advisory = advisory;
    }

    public boolean isPatch() {
        return patch;
    }

    public void setPatch(boolean patch) {
        this.patch = patch;
    }
}
