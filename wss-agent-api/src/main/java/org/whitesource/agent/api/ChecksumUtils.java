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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class to calculate SHA-1 hash codes for files.
 *
 * @author Edo.Shor
 */
public final class ChecksumUtils {
    
    /* --- Static members --- */

    private static final int BUFFER_SIZE = 32 * 1024;

    private static final int PARTIAL_SHA1_LINES = 100;

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

}
