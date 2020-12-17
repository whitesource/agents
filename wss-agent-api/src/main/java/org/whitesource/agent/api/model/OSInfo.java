package org.whitesource.agent.api.model;


public class OSInfo {

    /*
     * examples:
     *  ID=ubuntu + VERSION=20.04
     *  ID=fedora + VERSION=33
     */
    private String id;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
