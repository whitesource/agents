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
package org.whitesource.agent.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Info object describing a managed resource.
 *
 * @author Edo.Shor
 *
 * @since 1.2.0
 */
public class ResourceInfo implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 1707994312298243732L;

    /* --- Members --- */

    private String displayName;

    private String link;

    private Collection<String> licenses;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public ResourceInfo() {
        licenses = new ArrayList<String>();
    }

    /**
     * Constructor
     *
     * @param displayName
     */
    public ResourceInfo(String displayName) {
        this();
        this.displayName = displayName;
    }

    /* --- Overridden methods --- */

    @Override
    public String toString() {
        return displayName;
    }

    /* --- Getters / Setters --- */

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Collection<String> getLicenses() {
        return licenses;
    }

    public void setLicenses(Collection<String> licenses) {
        this.licenses = licenses;
    }
}
