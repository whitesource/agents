package org.whitesource.agent.api.dispatch;

public enum EntitlementType {
    CORE("CORE"),
    API_1("API_1"),
    API_2("API_2"),
    ESSENTIALS("ESSENTIALS"),
    SCA("SCA"),
    SAST("SAST"),
    CN("CN"),
    LLM("LLM"),
    IMG("IMG"),
    IAC("IAC");

    private final String value;

    EntitlementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

