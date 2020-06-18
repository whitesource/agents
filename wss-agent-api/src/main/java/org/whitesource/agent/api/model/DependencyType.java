/**
 * Copyright (C) 2017 White Source Ltd.
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

/**
 * Enum for describing the type of dependency sent to the server.
 *
 * @author tom.shapira
 */
public enum DependencyType {

    MAVEN,
    GRADLE,
    JAVA, //(For additionalSha1 purpose do not send it to application)

    NPM,
    BOWER,
    GRUNT,

    GO,
    PYTHON,

    RUBY,

    NUGET,
    PHP,

    RPM,
    DEBIAN,
    ALPINE,
    ARCH_LINUX,

    COCOAPODS,
    HEX,

    R,
    CRATE,
    CABAL,
    OPAM,
    PUB,

    DOCKER,
    DOCKER_LAYER;

    @Override
    public String toString() {
        switch(this) {
            case COCOAPODS: return "CocoaPods";
            case CRATE: return "Crate";
            case CABAL: return "Cabal";
            case OPAM: return "Opam";
            case DEBIAN: return "Debian";
            case ALPINE: return "Alpine";
            case ARCH_LINUX: return "Arch Linux";
            case PUB: return "Pub";
            case DOCKER: return "Docker";
            default: return super.toString();
        }
    }
}
