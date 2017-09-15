package org.whitesource.agent.hash;

/**
 * Enum for hash algorithms.
 *
 * @author chen.luigi
 */
public enum HashAlgorithm {

    MD5("MD5"),
    SHA1("SHA1"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    /* --- Private members --- */

    private String algorithm;

    /* --- Constructor --- */

    HashAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    /* --- Public methods --- */

    public String getAlgorithm() {
        return algorithm;
    }
}