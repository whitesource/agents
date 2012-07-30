package org.whitesource.api.client;

import org.whitesource.agent.api.dispatch.PropertiesResult;
import org.whitesource.agent.api.dispatch.ReportResult;
import org.whitesource.agent.api.dispatch.RequestFactory;
import org.whitesource.agent.api.dispatch.UpdateInventoryResult;
import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.DependencyInfo;

import java.util.Collection;

/**
 * A facade to the communication layer with the White Source service.
 *
 * @author Edo.Shor
 */
public class WhitesourceService {

    /* --- Members --- */

    private WssServiceClient client;

    private RequestFactory requestFactory;

    /* --- Constructors --- */

    public WhitesourceService() {
        this("generic", "1.0");
    }

    public WhitesourceService(final String agent, final String agentVersion) {
        this(agent, agentVersion, null);
    }

    public WhitesourceService(final String agent, final String agentVersion, String serviceUrl) {
        requestFactory = new RequestFactory(agent, agentVersion);

        if (serviceUrl == null) {
            serviceUrl = System.getProperty(ClientConstants.SERVICE_URL_KEYWORD, ClientConstants.DEFAULT_SERVICE_URL);
        }
        client = new WssServiceClientImpl(serviceUrl);
    }

    /* --- Public methods --- */

    /**
     * The method update the White Source organization account with the given OSS information.
     *
     * @param orgToken Orgnization token uniquely identifying the account at white source..
     * @param projectInfos OSS usage information to send to white source.
     *
     * @return Result of updating white source.
     *
     * @throws WssServiceException In case of errors while updating white source.
     */
    public UpdateInventoryResult update(String orgToken, Collection<AgentProjectInfo> projectInfos)
            throws WssServiceException {
        return client.updateInventory(requestFactory.newUpdateInventoryRequest(orgToken, projectInfos));
    }

    /**
     * The method get the properties defined in the white source account.
     *
     * @param orgToken Orgnization token uniquely identifying the account at white source.
     *
     * @return Whtie source account properties.
     *
     * @throws WssServiceException In case of errors while getting the properties form white source.
     */
    public PropertiesResult getProperties(String orgToken)
            throws WssServiceException {
        return client.getProperties(requestFactory.newPropertiesRequest(orgToken));
    }

    /**
     * The method get a license report for the given dependencies.
     *
     * @param dependencies Collection of dependencies to get a report for.
     *
     * @return Report for the given dependencies.
     *
     * @throws WssServiceException In case of errors while getting the report from white source.
     */
    public ReportResult getReport(Collection<DependencyInfo> dependencies)
            throws WssServiceException {
        return  client.getReport(requestFactory.newReportRequest(dependencies));
    }

    /**
     * The method close the underlying client to the White Source service.
     *
     * @see org.whitesource.api.client.WssServiceClient#shutdown()
     */
    public void shutdown() {
        client.shutdown();
    }

    /* --- Getters / Setters --- */

    public WssServiceClient getClient() {
        return client;
    }

    public void setClient(WssServiceClient client) {
        this.client = client;
    }

    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

    public void setRequestFactory(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

}
