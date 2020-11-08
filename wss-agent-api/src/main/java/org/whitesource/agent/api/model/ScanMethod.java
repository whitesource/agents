package org.whitesource.agent.api.model;


import java.io.Serializable;

public class ScanMethod implements Serializable {

    private static final long serialVersionUID = -1327471352027972845L;
    private String type;
    private String version;

    public ScanMethod(String type, String version) {
        this.type = type;
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
