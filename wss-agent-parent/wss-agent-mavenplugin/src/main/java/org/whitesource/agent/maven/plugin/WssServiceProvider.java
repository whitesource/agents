/**
 * Copyright (C) 2012 White Source Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whitesource.agent.maven.plugin;

import org.whitesource.agent.api.dispatch.RequestFactory;
import org.whitesource.api.client.WssServiceClient;
import org.whitesource.api.client.WssServiceClientImpl;

/**
 * WhiteSource service provider. Singleton.
 * 
 * @author Edo.Shor
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
