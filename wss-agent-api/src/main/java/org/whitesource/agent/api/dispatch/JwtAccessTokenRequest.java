package org.whitesource.agent.api.dispatch;

public class JwtAccessTokenRequest extends BaseRequest<JwtAccessTokenResult> {

    private static final long serialVersionUID = -2280907846002128613L;

    public JwtAccessTokenRequest() {
        super(RequestType.JWT_ACCESS_TOKEN);
    }

    public JwtAccessTokenRequest(String orgToken) {
        this();
        this.orgToken = orgToken;
    }

    public JwtAccessTokenRequest(String orgToken, String userKey) {
        this(orgToken);
        this.userKey = userKey;
    }
}
