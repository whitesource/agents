package org.whitesource.agent.api.dispatch;

public class UploadScaLogsRequest extends BaseRequest<UploadScaLogsResult> {

    private static final long serialVersionUID = -6634396300199014567L;

    public UploadScaLogsRequest() {
        super(RequestType.UPLOAD_SCA_LOGS);
    }

    public UploadScaLogsRequest(String orgToken, String userKey, String logData) {
        this();
        this.orgToken = orgToken;
        this.userKey = userKey;
        this.logData = logData;
    }
}
