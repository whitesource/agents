package org.whitesource.agent.api.dispatch;

import java.io.Serializable;

/**
 * @author tom.shapira
 */
public class BaseResult implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 6143216164199328453L;

    /* --- Members --- */

    private String requestToken;

    /* --- Constructors --- */

    public BaseResult() {
    }

    /* --- Getters / Setters --- */

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}
