package org.whitesource.agent.api.model;

/**
 * @author asaf.savich
 */
public enum ChecksumType {
    SHA1,
    SHA1_SUPER_HASH,
    SHA1_SUPER_HASH_MSB,
    SHA1_SUPER_HASH_LSB,
    SHA1_NO_HEADER,
    SHA1_NO_HEADER_SUPER_HASH,
    SHA1_NO_COMMENTS,
    SHA1_NO_COMMENTS_SUPER_HASH,
    SHA1_OTHER_PLATFORM,
    SHA1_UTF8,
    MD5,
    ADDITIONAL_SHA1
}