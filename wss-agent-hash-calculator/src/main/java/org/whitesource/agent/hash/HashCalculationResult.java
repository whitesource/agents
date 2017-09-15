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
package org.whitesource.agent.hash;

/**
 * This object holds the various hashes calculated for a single file.
 *
 * @author tom.shapira
 */
public class HashCalculationResult {

    /* --- Members --- */

    private String fullHash;
    private String mostSigBitsHash;
    private String leastSigBitsHash;

    /* --- Constructors --- */

    public HashCalculationResult(String fullHash) {
        this.fullHash = fullHash;
    }

    public HashCalculationResult(String fullHash, String mostSigBitsHash, String leastSigBitsHash) {
        this.fullHash = fullHash;
        this.mostSigBitsHash = mostSigBitsHash;
        this.leastSigBitsHash = leastSigBitsHash;
    }

    /* --- Getters / Setters --- */

    public String getFullHash() {
        return fullHash;
    }

    public void setFullHash(String fullHash) {
        this.fullHash = fullHash;
    }

    public String getMostSigBitsHash() {
        return mostSigBitsHash;
    }

    public void setMostSigBitsHash(String mostSigBitsHash) {
        this.mostSigBitsHash = mostSigBitsHash;
    }

    public String getLeastSigBitsHash() {
        return leastSigBitsHash;
    }

    public void setLeastSigBitsHash(String leastSigBitsHash) {
        this.leastSigBitsHash = leastSigBitsHash;
    }
}
