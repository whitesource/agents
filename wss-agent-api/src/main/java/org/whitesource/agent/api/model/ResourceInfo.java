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

import com.google.gson.annotations.Since;
import org.whitesource.agent.api.AgentApiVersion;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

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

    /**
     * @since 2.2.8
     */
    private String sha1;
    private Collection<VulnerabilityInfo> vulnerabilities;
    private String homepageUrl;
    private String description;
    /**
    * @since 2.9.8
    */
    @Since(AgentApiVersion.AGENT_API_VERSION_2_9_8)
    private String keyUuid;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public ResourceInfo() {
        licenses = new LinkedList<>();
        vulnerabilities = new LinkedList<>();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceInfo)) return false;

        ResourceInfo that = (ResourceInfo) o;

        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (keyUuid != null ? keyUuid.equals(this.keyUuid) : that.keyUuid != null) return false;
        return sha1 != null ? sha1.equals(that.sha1) : that.sha1 == null;

    }

    @Override
    public int hashCode() {
        int result = displayName != null ? displayName.hashCode() : 0;
        result = 31 * result + (keyUuid != null ? keyUuid.hashCode() : 0);
        result = 31 * result + (sha1 != null ? sha1.hashCode() : 0);
        return result;
    }

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

    public String getKeyUuid(){
        return keyUuid;
    }

    public void  setKeyUuid(String value){
        keyUuid = value;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public Collection<VulnerabilityInfo> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(Collection<VulnerabilityInfo> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
