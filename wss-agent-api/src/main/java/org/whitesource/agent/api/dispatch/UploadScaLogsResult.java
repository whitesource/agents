package org.whitesource.agent.api.dispatch;

public class UploadScaLogsResult extends BaseResult {

    private static final long serialVersionUID = -8474225446757628208L;
    private boolean success;
    private String message;

    public UploadScaLogsResult() {
        super();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
