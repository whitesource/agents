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
package org.whitesource.agent.api;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.whitesource.agent.api.model.DependencyInfo;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Utility class to calculate SHA-1 hash codes for files.
 *
 * @author Edo.Shor
 */
public final class ChecksumUtils {
    
    /* --- Static members --- */

    private static final int BUFFER_SIZE = 32 * 1024;

    private static final int PARTIAL_SHA1_LINES = 100;

    public static final int FILE_MIN_SIZE= 1024 * 2;

    public static final int FILE_SMALL_SIZE = 1024 * 3;

    public static final double FILE_SMALL_BUCKET_SIZE = 1024 * 1.25;

    /* --- Constructors --- */

    /**
     * Private default constructor
     */
    private ChecksumUtils() {
        // avoid instantiation
    }

    /* --- Static methods --- */

    /**
     * Calculates the given file's SHA-1 hash code.
     *
     * @param resourceFile File to calculate
     *
     * @return Calculated SHA-1 for the given file.
     *
     * @throws IOException on file reading errors.
     * @throws IllegalStateException when no algorithm for SHA-1 can be found.
     */
    public static String calculateSHA1(File resourceFile) throws IOException {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        FileInputStream fis = new FileInputStream(resourceFile);
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            int len = fis.read(buffer, 0, BUFFER_SIZE);
            while (len >= 0) {
                messageDigest.update(buffer, 0, len);
                len = fis.read(buffer, 0, BUFFER_SIZE);
            }
        } finally {
            try{
                fis.close();
            } catch (IOException e) {
                // ignore
            }
        }
        return toHex(messageDigest.digest());
        // Base64.encodeBase64("Test".getBytes())
    }

    /**
     * Calculates the given file's SHA-1 hash code.
     *
     * @param byteArray to calculate
     *
     * @return Calculated SHA-1 for the given byteArray.
     *
     * @throws IllegalStateException when no algorithm for SHA-1 can be found.
     */
    public static String calculateByteArraySHA1(byte[] byteArray) throws IOException {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
                messageDigest.update( byteArray, 0, byteArray.length);
        return toHex(messageDigest.digest());
    }

    public static void calculateHeaderAndFooterSha1(File file, DependencyInfo dependencyInfo) {
        try {
            int lines = FileUtils.readLines(file).size();
            if (lines > PARTIAL_SHA1_LINES) {
                // get lines to read
                int i = (int) (Math.log((double) lines / PARTIAL_SHA1_LINES) / Math.log(2));
                int linesToRead = (int) (50 * Math.pow(2, i));

                StringBuilder header = new StringBuilder();
                StringBuilder footer = new StringBuilder();
                int lineIndex = 0;

                for (String line : FileUtils.readLines(file)) {
                    // read header
                    if (lineIndex < linesToRead) {
                        header.append(line);
                    }

                    // read footer
                    if (lineIndex >= lines - linesToRead) {
                        footer.append(line);
                    }
                    lineIndex++;
                }

                // calculate sha1s
                dependencyInfo.setHeaderSha1(DigestUtils.shaHex(header.toString()));
                dependencyInfo.setFooterSha1(DigestUtils.shaHex(footer.toString()));
            }
        } catch (FileNotFoundException e) {
            // ignore
        } catch (IOException e) {
            // ignore
        }
    }
    public static DependencyInfo getFileHash(File file, DependencyInfo dependencyInfo) throws IOException {
        // Ignore files smaller than 2kb
        if (file.length() <= FILE_MIN_SIZE) {
            return null;
        }
        // Get file bytes
        byte[] fileToByte = FileUtils.readFileToByteArray(file);

        //Remove white spaces
        byte[] bytesWithoutSpaces = stripWhiteSpaces(fileToByte);

        long fileSize = bytesWithoutSpaces.length;
        // Ignore files smaller than 2kb
        if (fileSize <= FILE_MIN_SIZE) {
            return null;
        }
        // Handle 2kb->3kb files
        else if (fileSize <= FILE_SMALL_SIZE) {
            return hashBuckets(bytesWithoutSpaces , FILE_SMALL_BUCKET_SIZE, dependencyInfo);
        }

        else if (file != null && fileSize > FILE_SMALL_SIZE) {
            int baseLowNumber = 1;
            int digits = (int) Math.log10(fileSize);
            int i = 0;
            while (i < digits) {
                baseLowNumber  = baseLowNumber * 10;
                i++;
            }
            double highNumber = Math.ceil((fileSize + 1) / (float) baseLowNumber) * baseLowNumber;
            double lowNumber = highNumber - baseLowNumber;
            double bucketSize = (highNumber + lowNumber) / 4;
            return hashBuckets(bytesWithoutSpaces, bucketSize, dependencyInfo);
        }
        return null;
    }

    private static DependencyInfo hashBuckets(byte[] fileWithoutSpaces, double bucketSize, DependencyInfo dependencyInfo)
            throws IOException {
        // int(bucket_size) will round down the bucket_size: IE: 1.2 -> 1.0
        int bucketIntSize = (int) bucketSize ;

        // Get bytes and calculate sha1
        byte [] mostSifBytes = Arrays.copyOfRange(fileWithoutSpaces, 0, bucketIntSize);
        int length = fileWithoutSpaces.length;
        byte [] listSigBytes = Arrays.copyOfRange(fileWithoutSpaces, length - bucketIntSize, length);
        dependencyInfo.setMostSigBitSha1(calculateByteArraySHA1(mostSifBytes));
        dependencyInfo.setLeastSigBitSha1(calculateByteArraySHA1(listSigBytes));
        return dependencyInfo;
    }

    /* --- Private static methods --- */

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            int b = aByte & 0xFF;
            if (b < 0x10) {
                sb.append('0');
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
    private static byte[] stripWhiteSpaces(byte[] data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (byte b : data) {
                if(b!=(byte)0x0d && b!=(byte)0x0a && b!=(byte)0x09 && b!=(byte)0x20 ){
                    bos.write(b);
                }
            }
        return bos.toByteArray();
    }

}
