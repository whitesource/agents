package org.whitesource.agent.api.dispatch;

public class AsyncCheckPolicyComplianceStatusRequest extends BaseRequest<AsyncCheckPolicyComplianceStatusResult> {

    /* --- Static Members --- */

    private static final long serialVersionUID = -3011749510680068374L;

    /* --- Members --- */

    protected String identifier;

    /**
     * Default constructor
     */
    public AsyncCheckPolicyComplianceStatusRequest(String identifier) {
        super(RequestType.ASYNC_CHECK_POLICY_COMPLIANCE_STATUS);
        this.identifier = identifier;
    }

    public AsyncCheckPolicyComplianceStatusRequest() {
        super(RequestType.ASYNC_CHECK_POLICY_COMPLIANCE_STATUS);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
