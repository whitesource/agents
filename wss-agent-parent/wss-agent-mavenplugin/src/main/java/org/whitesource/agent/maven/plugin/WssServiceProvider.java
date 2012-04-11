package org.whitesource.agent.maven.plugin;

import org.whitesource.agent.api.dispatch.RequestFactory;
import org.whitesource.api.client.WssServiceClient;
import org.whitesource.api.client.WssServiceClientImpl;

/**
 * WhiteSource service provider. Singleton.
 * 
 * @author Edo.Shor
 */
public class WssServiceProvider {

	/* --- Static members --- */
	
	private static final String ALTERNATE_SERVICE_LOCATION = "wssUrl";
	
	private static final WssServiceProvider INSTANCE = new WssServiceProvider();
	
	/* --- Members --- */
	
	private WssServiceClient _service;
	
	private RequestFactory _requestFactory;

	/* --- Constructors --- */
	
	/**
	 * Default constructor.
	 */
	private WssServiceProvider() {
		_requestFactory = new RequestFactory(Constants.AGENT_TYPE, Constants.AGENT_VERSION);
		
		String url = System.getProperty(ALTERNATE_SERVICE_LOCATION, null);
		if (url == null) {
			_service = new WssServiceClientImpl();	
		} else {
			_service = new WssServiceClientImpl(url);
		}
	}
	
	/* --- Static methods --- */
	
	public static WssServiceProvider instance() {
		return INSTANCE;
	}
	
	/* --- Public methods --- */
	
	public WssServiceClient provider() {
		return _service;
	}
	
	public RequestFactory requestFactory() {
		return _requestFactory;
	}
	
	public void shutdown() {
		_service.shutdown();
	}
	
}
