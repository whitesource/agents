package org.whitesource.agent.api.dispatch;

public enum AppFlags {
    UPLOAD_SCA_LOGS("UPLOAD_SCA_LOGS"),
    ASYNC_CHECK_POLICIES_ENABLED("ASYNC_CHECK_POLICIES_ENABLED");


    private final String value;

    AppFlags(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
