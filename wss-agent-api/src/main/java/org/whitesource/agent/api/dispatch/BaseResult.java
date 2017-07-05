package org.whitesource.agent.api.dispatch;

import java.io.Serializable;

/**
 * @author tom.shapira
 */
public class BaseResult implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 6143216164199328453L;

    /* --- Members --- */

    /**
     * Name of organization in.
     */
    private String organization;

    private String requestToken;

    /* --- Constructors --- */

    public BaseResult() {
    }

    /* --- Getters / Setters --- */

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}
