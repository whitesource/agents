/**
 * 
 */
package org.whitesource.agent.jenkins.plugin;

import org.whitesource.agent.api.dispatch.RequestFactory;
import org.whitesource.api.client.WssServiceClient;
import org.whitesource.api.client.WssServiceClientImpl;

/**
 * @author c_rsharv
 *
 */
public final class WssServiceProvider {
/* --- Static members --- */
	
	private static final String ALTERNATE_SERVICE_LOCATION = "wssUrl";
	
	private static final WssServiceProvider INSTANCE = new WssServiceProvider();
	
	/* --- Members --- */
	
	private WssServiceClient mService;
	
	private RequestFactory mRequestFactory;

	/* --- Constructors --- */
	
	/**
	 * Default constructor.
	 */
	private WssServiceProvider() {
		mRequestFactory = new RequestFactory(Constants.AGENT_TYPE, Constants.AGENT_VERSION);
		
		String url = System.getProperty(ALTERNATE_SERVICE_LOCATION, null);
		if (url == null) {
			mService = new WssServiceClientImpl();	
		} else {
			mService = new WssServiceClientImpl(url);
		}
	}
	
	/* --- Static methods --- */
	
	public static WssServiceProvider instance() {
		return INSTANCE;
	}
	
	/* --- Public methods --- */
	
	public WssServiceClient provider() {
		return mService;
	}
	
	public RequestFactory requestFactory() {
		return mRequestFactory;
	}
	
	public void shutdown() {
		mService.shutdown();
	}
}
