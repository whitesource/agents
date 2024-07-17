package org.whitesource.agent.api.dispatch;


public class SendMetricsRequest extends BaseRequest<SendMetricsResult> {

    private static final long serialVersionUID = -6575498573910995957L;

    public SendMetricsRequest() {
        super(RequestType.SEND_METRICS);
    }

    public SendMetricsRequest(String orgToken) {
        this();
        this.orgToken = orgToken;
    }

    public SendMetricsRequest(String orgToken, String userKey) {
        this(orgToken);
        this.userKey = userKey;
    }
}
