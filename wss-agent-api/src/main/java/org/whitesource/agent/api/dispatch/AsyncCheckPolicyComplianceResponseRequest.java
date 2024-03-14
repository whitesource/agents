package org.whitesource.agent.api.dispatch;

public class AsyncCheckPolicyComplianceResponseRequest extends BaseRequest<AsyncCheckPolicyComplianceResponseResult> {

    /* --- Static Members --- */

    private static final long serialVersionUID = 7267771318691275351L;

    /* --- Members --- */

    protected String identifier;

    /**
     * Default constructor
     */
    public AsyncCheckPolicyComplianceResponseRequest(String identifier) {
        super(RequestType.ASYNC_CHECK_POLICY_COMPLIANCE_RESPONSE);
        this.identifier = identifier;
    }

    public AsyncCheckPolicyComplianceResponseRequest() {super(RequestType.ASYNC_CHECK_POLICY_COMPLIANCE_RESPONSE);}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
