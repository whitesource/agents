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

}
