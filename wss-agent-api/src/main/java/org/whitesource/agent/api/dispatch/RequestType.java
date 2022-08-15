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

/**
 * Enumeration of the service available methods.
 * 
 * @author Edo.Shor
 */
public enum RequestType {
    UPDATE ("UPDATE"),
    CHECK_POLICIES ("CHECK_POLICIES"),
    CHECK_POLICY_COMPLIANCE ("CHECK_POLICY_COMPLIANCE"),
    CHECK_VULNERABILITIES ("CHECK_VULNERABILITIES"),
    GET_CLOUD_NATIVE_VULNERABILITIES ("GET_CLOUD_NATIVE_VULNERABILITIES"),
    GET_DEPENDENCY_DATA ("GET_DEPENDENCY_DATA"),
    SUMMARY_SCAN ("SUMMARY_SCAN"),
    GET_CONFIGURATION ("GET_CONFIGURATION");

    private final String value;

    RequestType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

