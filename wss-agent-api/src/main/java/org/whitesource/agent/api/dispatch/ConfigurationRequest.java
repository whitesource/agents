package org.whitesource.agent.api.dispatch;

/**
 * Created by anna.rozin
 */
public class ConfigurationRequest extends BaseRequest<ConfigurationResult> {


    /* --- Static members --- */

    private static final long serialVersionUID = 1565062936737064054L;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public ConfigurationRequest() {
        super(RequestType.GET_CONFIGURATION);
    }

    public ConfigurationRequest(String orgToken, String product, String productVersion, String userKey, String requesterEmail, String productToken) {
        this();
        this.orgToken = orgToken;
        this.product = product;
        this.productVersion = productVersion;
        this.userKey = userKey;
        this.requesterEmail = requesterEmail;
        this.productToken = productToken;
    }
}
