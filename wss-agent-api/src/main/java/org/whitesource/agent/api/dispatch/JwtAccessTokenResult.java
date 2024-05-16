package org.whitesource.agent.api.dispatch;

public class JwtAccessTokenResult extends BaseResult {

    private static final long serialVersionUID = 4617440013556093604L;

    private String jwtAccessToken;

    public JwtAccessTokenResult() {
        super();
    }

    public JwtAccessTokenResult(String jwtAccessToken) {
        super();
        this.jwtAccessToken = jwtAccessToken;
    }

    public String getJwtAccessToken() {
        return jwtAccessToken;
    }

    public void setJwtAccessToken(String jwtAccessToken) {
        this.jwtAccessToken = jwtAccessToken;
    }
}
