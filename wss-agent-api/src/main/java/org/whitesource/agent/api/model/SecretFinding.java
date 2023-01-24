package org.whitesource.agent.api.model;


public class SecretFinding {

    private int layerNumber;
    private String ruleId;
    private String category;
    private String severity;
    private String description;
    private int startLine;
    private int endLine;
    private String filePath;
    private Code code;
    private String match;

}