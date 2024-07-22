package org.whitesource.agent.api.dispatch;

import java.util.Map;

public class SendMetricsRequest extends BaseRequest<SendMetricsResult> {

    private static final long serialVersionUID = -6575498573910995957L;

    public SendMetricsRequest() {
        super(RequestType.SEND_METRICS);
    }

    public SendMetricsRequest(String orgToken) {
        this();
        this.orgToken = orgToken;
    }

    public SendMetricsRequest(String orgToken, String userKey, String productName, Map<String, String> metrics) {
        this(orgToken);
        this.userKey = userKey;
        this.product = productName;
        this.extraProperties = metrics;
    }
}
