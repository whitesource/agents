/**
 * Copyright (C) 2016 White Source Ltd.
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

import org.whitesource.agent.api.model.ResourceInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Result for {@link GetDependencyDataRequest}.
 *
 * @author anna.rozin
 *
 * @since 2.2.8
 */
public class GetDependencyDataResult implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -6064404450850954766L;

    /* --- Members --- */

    /**
     * Name of organization in.
     */
    private String organization;

    /**
     * Resources Info Collection.
     */

    private Collection<ResourceInfo> resources;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public GetDependencyDataResult() {
        resources = new ArrayList<ResourceInfo>();
    }

    /**
     * Constructor
     *
     * @param organization Name of the domain.
     */
    public GetDependencyDataResult(String organization) {
        this();
        this.organization = organization;
    }

    /* --- Getters / Setters --- */

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Collection<ResourceInfo> getResources() {
        return resources;
    }

    public void setResources(Collection<ResourceInfo> resources) {
        this.resources = resources;
    }
}
