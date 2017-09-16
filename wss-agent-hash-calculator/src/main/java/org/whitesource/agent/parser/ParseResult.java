package org.whitesource.agent.parser;

/**
 * @author tom.shapira
 */
public class ParseResult {

    /* --- Members --- */

    /**
     * Doesn't resemble the file content in terms of spaces, tabs, indentation, but has the same content without comments.
     */
    private String contentWithoutComments;

    /**
     * Resembles the file content exactly but without comments in the header of the file.
     */
    private String contentWithoutHeaderComments;

    /* --- Getters / Setters --- */

    public String getContentWithoutComments() {
        return contentWithoutComments;
    }

    public void setContentWithoutComments(String contentWithoutComments) {
        this.contentWithoutComments = contentWithoutComments;
    }

    public String getContentWithoutHeaderComments() {
        return contentWithoutHeaderComments;
    }

    public void setContentWithoutHeaderComments(String contentWithoutHeaderComments) {
        this.contentWithoutHeaderComments = contentWithoutHeaderComments;
    }
}
