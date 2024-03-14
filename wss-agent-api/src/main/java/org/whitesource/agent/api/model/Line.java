package org.whitesource.agent.api.model;


public class Line {
    private int number;
    private String content;
    private boolean isCause;
    private String annotation;
    private boolean truncated;
    private String highlighted;
    private boolean firstCause;
    private boolean lastCause;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCause() {
        return isCause;
    }

    public void setCause(boolean cause) {
        isCause = cause;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(String highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isFirstCause() {
        return firstCause;
    }

    public void setFirstCause(boolean firstCause) {
        this.firstCause = firstCause;
    }

    public boolean isLastCause() {
        return lastCause;
    }

    public void setLastCause(boolean lastCause) {
        this.lastCause = lastCause;
    }



}
