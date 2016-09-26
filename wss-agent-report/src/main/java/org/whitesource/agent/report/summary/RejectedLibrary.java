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
package org.whitesource.agent.report.summary;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * POJO for holding the minimum resource information.
 *
 * @author tom.shapira
 * @since 2.3.2
 */
public class RejectedLibrary implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -3757254562564574416L;

    /* --- Members --- */

    private String name;
    private String sha1;
    private String link;
    private Set<String> projects;

    /* --- Constructors --- */

    public RejectedLibrary() {
        projects = new HashSet<String>();
    }

    public RejectedLibrary(String name, String sha1, String link) {
        this();
        this.name = name;
        this.sha1 = sha1;
        this.link = link;
    }

    /* --- Overridden methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RejectedLibrary)) return false;

        RejectedLibrary that = (RejectedLibrary) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return sha1 != null ? sha1.equals(that.sha1) : that.sha1 == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (sha1 != null ? sha1.hashCode() : 0);
        return result;
    }

    /* --- Getters / Setters --- */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<String> getProjects() {
        return projects;
    }

    public void setProjects(Set<String> projects) {
        this.projects = projects;
    }
}
