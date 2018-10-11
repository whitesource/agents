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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.whitesource.agent.api.model.ChecksumType;
import org.whitesource.agent.api.model.DependencyType;
import org.whitesource.agent.parser.JavaScriptParser;
import org.whitesource.agent.parser.ParseResult;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * Utility class to calculate SHA-1 hash codes for files.
 *
 * @author anna.rozin
 * @author tom.shapira
 */
public class HashCalculator {

    /* --- Static members --- */

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HashCalculator.class);

    private static final int BUFFER_SIZE = 32 * 1024;

    private static final int FILE_MIN_SIZE_THRESHOLD = 512;
    private static final int FILE_PARTIAL_HASH_MIN_SIZE = 1024 * 2;
    private static final int FILE_SMALL_SIZE = 1024 * 3;
    private static final int FILE_MAX_SIZE_THRESHOLD = Integer.MAX_VALUE;

    private static final double FILE_SMALL_BUCKET_SIZE = 1024 * 1.25;
    
    private static final char ZERO = '0';

    private static final byte CARRIAGE_RETURN = (byte) 0x0d;
    private static final byte NEW_LINE = (byte) 0x0a;
    private static final byte HORIZONTAL_TAB = (byte) 0x09;
    private static final byte SPACE = (byte) 0x20;

    private static final Collection<Byte> WHITESPACES = Arrays.asList(CARRIAGE_RETURN, NEW_LINE, HORIZONTAL_TAB, SPACE);
    private static final String UTF_8 = "utf-8";

    private static final String UNDERSCORE = "_";

    /* --- Static methods --- */

    /**
     * Calculates 3 hashes for the given file:
     *
     * 1. Hash of the file without new lines and whitespaces
     * 2. Hash of the most significant bits of the file without new lines and whitespaces
     * 3. Hash of the least significant bits of the file without new lines and whitespaces
     * 
     * @param file input
     * @return HashCalculationResult with all three hashes
     * @throws IOException exception1
     */
    public HashCalculationResult calculateSuperHash(File file) throws IOException {
        // Ignore files smaller than 0.5kb
        long fileSize = file.length();
        if (fileSize <= FILE_MIN_SIZE_THRESHOLD) {
            logger.debug("Ignored file " + file.getName() + " (" + FileUtils.byteCountToDisplaySize(fileSize) + "): minimum file size is 512B");
            return null;
        }
        if (fileSize >= FILE_MAX_SIZE_THRESHOLD){
            logger.debug("Ignore file {}, ({}): maximum file size is 2GB", file.getName(), FileUtils.byteCountToDisplaySize(fileSize));
            return null;
        }

        HashCalculationResult result = null;
        try {
            result = calculateSuperHash(FileUtils.readFileToByteArray(file));
        } catch (OutOfMemoryError e) {
            logger.debug(MessageFormat.format("Failed calculating SHA-1 for file {0}: size too big {1}",
                    file.getAbsolutePath(), FileUtils.byteCountToDisplaySize(fileSize)));
        }
        return result;
    }

    /**
     * Calculates 3 hashes for the given bytes:
     *
     * 1. Hash of the file without new lines and whitespaces
     * 2. Hash of the most significant bits of the file without new lines and whitespaces
     * 3. Hash of the least significant bits of the file without new lines and whitespaces
     *
     * @param bytes to calculate
     * @return HashCalculationResult with all three hashes
     * @throws IOException exception2
     */
    public HashCalculationResult calculateSuperHash(byte[] bytes) throws IOException {
        HashCalculationResult result = null;

        // Remove white spaces
        byte[] bytesWithoutSpaces = stripWhiteSpaces(bytes);

        long fileSize = bytesWithoutSpaces.length;
        if (fileSize < FILE_MIN_SIZE_THRESHOLD) {
            // Ignore files smaller 1/2 kb
            logger.debug("Ignoring file with size " + FileUtils.byteCountToDisplaySize(fileSize) + ": minimum file size is 512B");
        } else if (fileSize <= FILE_PARTIAL_HASH_MIN_SIZE) {
            // Don't calculate msb and lsb hashes for files smaller than 2kb
            String fullFileHash = calculateByteArrayHash(bytesWithoutSpaces, HashAlgorithm.SHA1);
            result = new HashCalculationResult(fullFileHash);
        } else if (fileSize <= FILE_SMALL_SIZE) {
            // Handle 2kb->3kb files
            result = hashBuckets(bytesWithoutSpaces, FILE_SMALL_BUCKET_SIZE);
        } else {
            int baseLowNumber = 1;
            int digits = (int) Math.log10(fileSize);
            int i = 0;
            while (i < digits) {
                baseLowNumber = baseLowNumber * 10;
                i++;
            }
            double highNumber = Math.ceil((fileSize + 1) / (float) baseLowNumber) * baseLowNumber;
            double lowNumber = highNumber - baseLowNumber;
            double bucketSize = (highNumber + lowNumber) / 4;
            result = hashBuckets(bytesWithoutSpaces, bucketSize);
        }
        return result;
    }

    /**
     * Calculates the given file's SHA-1 hash code.
     *
     * @param resourceFile File to calculate
     * @return Calculated SHA-1 for the given file.
     * @throws IOException           on file reading errors.
     * @throws IllegalStateException when no algorithm for SHA-1 can be found.
     */
    public String calculateSHA1(File resourceFile) throws IOException {
        return calculateHash(resourceFile, HashAlgorithm.SHA1);
    }

    public String calculateHash(File resourceFile, HashAlgorithm algorithm) throws IOException {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(algorithm.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        try(InputStream inputStream = new FileInputStream(resourceFile);
            BOMInputStream fis = new BOMInputStream(inputStream)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = fis.read(buffer, 0, BUFFER_SIZE);
            while (len >= 0) {
                messageDigest.update(buffer, 0, len);
                len = fis.read(buffer, 0, BUFFER_SIZE);
            }
        }
        return toHex(messageDigest.digest());
    }

    /**
     * Calculates the given file's SHA-1 hash code.
     *
     * @param byteArray to calculate
     * @return Calculated SHA-1 for the given byteArray.
     * @throws IOException when no algorithm for SHA-1 can be found.
     */
    public String calculateByteArraySHA1(byte[] byteArray) throws IOException {
        return calculateByteArrayHash(byteArray, HashAlgorithm.SHA1);
    }

    public String calculateByteArrayHash(byte[] byteArray, HashAlgorithm algorithm) throws IOException {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(algorithm.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        messageDigest.update(byteArray, 0, byteArray.length);
        return toHex(messageDigest.digest());
    }

    /**
     * Removes all JavaScript comments from the file and calculates SHA-1 checksum.
     *
     * @param file to calculate
     * @return Calculated SHA-1 checksums for the given file.
     */
    public Map<ChecksumType, String> calculateJavaScriptHashes(File file) throws WssHashException {
        Map<ChecksumType, String> checksums = new EnumMap<>(ChecksumType.class);
        try {
            long fileLength = file.length();
            if (fileLength >= FILE_MAX_SIZE_THRESHOLD){
                logger.debug("Ignore file {}, ({}): maximum file size  is 2GB", file.getName(), FileUtils.byteCountToDisplaySize(fileLength));
                return checksums;
            }
            checksums = calculateJavaScriptHashes(FileUtils.readFileToByteArray(file));
        } catch (Exception e) {
            throw new WssHashException("Error calculating JavaScript hash: "+ e.getMessage());
        }
        return checksums;
    }

    /**
     * Removes all JavaScript header comments from the file and calculates SHA-1 checksum.
     *
     * @param byteArray to calculate
     * @return Calculated SHA-1 for the given file.
     */
    public Map<ChecksumType, String> calculateJavaScriptHashes(byte[] byteArray) throws WssHashException {
        Map<ChecksumType, String> checksums = new EnumMap<>(ChecksumType.class);
        try {
            String fileContent = IOUtils.toString(byteArray, UTF_8);
            ParseResult parseResult = new JavaScriptParser().parse(fileContent);
            if (parseResult != null) {
                // no comments
                String contentWithoutComments = parseResult.getContentWithoutComments();
                if (StringUtils.isNotBlank(contentWithoutComments)) {
                    HashCalculationResult noCommentsSha1 = calculateSuperHash(contentWithoutComments.getBytes());
                    if (noCommentsSha1 != null) {
                        checksums.put(ChecksumType.SHA1_NO_COMMENTS_SUPER_HASH, noCommentsSha1.getFullHash());
                    }
                }

                // no headers
                String headerlessContent = parseResult.getContentWithoutHeaderComments();
                if (StringUtils.isNotBlank(headerlessContent)) {
                    String headerlessChecksum = calculateByteArrayHash(headerlessContent.getBytes(), HashAlgorithm.SHA1);
                    checksums.put(ChecksumType.SHA1_NO_HEADER, headerlessChecksum);
                }
            }
        } catch (Exception e) {
            throw new WssHashException("Error calculating JavaScript hash: "+ e.getMessage());
        }
        return checksums;
    }

    /**
     * Calculates SHA-1 for library by name, version and dependencyType
     *
     * @param name of library
     * @param version of library
     * @param dependencyType of library
     * @return Calculated SHA-1 for library by name, version and dependencyType
     * @throws IOException when failed to calculate sha-1
     */
    public String calculateSha1ByNameVersionAndType(String name, String version, DependencyType dependencyType) throws IOException {
        String sha1ToCalc = name + UNDERSCORE + version + UNDERSCORE + dependencyType.toString();
        return calculateByteArraySHA1(sha1ToCalc.getBytes(StandardCharsets.UTF_8));
    }

    /* --- Private static methods --- */

    private HashCalculationResult hashBuckets(byte[] fileWithoutSpaces, double bucketSize) throws IOException {
        // int(bucket_size) will round down the bucket_size: IE: 1.2 -> 1.0
        int bucketIntSize = (int) bucketSize;

        // Get bytes and calculate sha1
        byte[] mostSigBytes = Arrays.copyOfRange(fileWithoutSpaces, 0, bucketIntSize);
        int length = fileWithoutSpaces.length;
        byte[] leastSigBytes = Arrays.copyOfRange(fileWithoutSpaces, length - bucketIntSize, length);
        String fullFileHash = calculateByteArraySHA1(fileWithoutSpaces);
        String mostSigBitsHash = calculateByteArraySHA1(mostSigBytes);
        String leastSigBitsHash = calculateByteArraySHA1(leastSigBytes);
        return new HashCalculationResult(fullFileHash, mostSigBitsHash, leastSigBitsHash);
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            int b = aByte & 0xFF;
            if (b < 0x10) {
                sb.append(ZERO);
            }
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    /**
     * Removes all whitespaces from the text - the same way that Shir is doing for source files.
     *
     * @param data - byte array
     * @return file as string
     */
    private byte[] stripWhiteSpaces(byte[] data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (byte b : data) {
            if (!WHITESPACES.contains(b)) {
                bos.write(b);
            }
        }
        return bos.toByteArray();
    }
}