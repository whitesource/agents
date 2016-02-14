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
package org.whitesource.agent.api.dispatch;

import org.whitesource.agent.api.model.AgentProjectInfo;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Base, abstract, implementation of the interface.
 * 
 * @author Edo.Shor
 *
 * @param <R> Type of expected result. 
 */
public abstract class BaseRequest<R> implements ServiceRequest<R> {

	/* --- Static members --- */
	
	private static final long serialVersionUID = 4691829529579651426L;
	
	/* --- Members --- */

	protected final RequestType type;
	
	protected String agent;
	
	protected String agentVersion;
	
	protected String orgToken;
	
	protected String product;

    protected String productVersion;

    protected long timeStamp;

    protected String requesterEmail;

	protected Collection<AgentProjectInfo> projects;

	/* --- Constructors --- */

	/**
	 * Constructor
	 * 
	 * @param type Request operation type.
	 */
	public BaseRequest(RequestType type) {
		this(type, null, null);
	}

	/**
	 * Constructor
	 *
	 * @param type Request operation type.
	 * @param agent Agent type identifier.
	 */
	public BaseRequest(RequestType type, String agent) {
		this(type, agent, null);
	}

	/**
	 * Constructor
	 *
	 * @param type Request operation type.
	 * @param agent Agent type identifier.
	 * @param agentVersion Agent version.
	 */
	public BaseRequest(RequestType type, String agent, String agentVersion) {
		this.type  = type;
		this.agent = agent;
		this.agentVersion = agentVersion;
        this.timeStamp = System.currentTimeMillis();
		projects = new ArrayList<AgentProjectInfo>();
	}

	/* --- Interface implementation methods --- */

    @Override
	public RequestType type() {
		return type;
	}

    @Override
	public String orgToken() {
		return orgToken;
	}

    @Override
	public String agent() {
		return agent;
	}

    @Override
	public String agentVersion() {
		return agentVersion;
	}

    @Override
    public String product() {
        return product;
    }

    @Override
    public String productVersion() {
        return productVersion;
    }

    @Override
    public long timeStamp() {
        return timeStamp;
    }

    @Override
    public String requesterEmail() {
        return requesterEmail;
    }

	/* --- Getters / Setters --- */

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public void setAgentVersion(String agentVersion) {
		this.agentVersion = agentVersion;
	}

	public void setOrgToken(String orgToken) {
		this.orgToken = orgToken;
	}

    public void setProduct(String product) {
        this.product = product;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

	public Collection<AgentProjectInfo> getProjects() {
		return projects;
	}

	public void setProjects(Collection<AgentProjectInfo> projects) {
		this.projects = projects;
	}
}
