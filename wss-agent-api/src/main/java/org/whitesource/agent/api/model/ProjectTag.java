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
import java.util.Objects;

public class ProjectTag implements Serializable {
    private static final long serialVersionUID = 1835100904098326933L;

    /* --- Members --- */

    private String tagKey;

    private String tagValue;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public ProjectTag() {

    }

    /**
     * Constructor
     *
     * @param tagKey
     * @param tagValue
     */
    public ProjectTag(String tagKey, String tagValue) {
        this.tagKey = tagKey;
        this.tagValue = tagValue;
    }

    /* --- Overridden methods --- */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ProjectTag@").append(Integer.toHexString(hashCode()))
                .append("[")
                .append("tagKey= ").append(tagKey).append(",")
                .append("tagValue= ").append(tagValue)
                .append(" ]");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectTag)) return false;
        ProjectTag that = (ProjectTag) o;
        return Objects.equals(tagKey, that.tagKey) &&
                Objects.equals(tagValue, that.tagValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagKey, tagValue);
    }

    public String getTagKey() {
        return tagKey;
    }

    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }
}
