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
package org.whitesource.agent.hash;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whitesource.agent.api.model.DependencyInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Utility class to calculate SHA-1 hash codes for files.
 *
 * @author Edo.Shor
 */
public final class ChecksumUtils {
    
    /* --- Static members --- */

    private static final Logger logger = LoggerFactory.getLogger(ChecksumUtils.class);

    private static final int PARTIAL_SHA1_LINES = 100;
    private static final String EMPTY_FILE_SHA1 = "da39a3ee5e6b4b0d3255bfef95601890afd80709";

    /* --- Constructors --- */

    /**
     * Private default constructor
     */
    private ChecksumUtils() {
        // avoid instantiation
    }

    /* --- Static methods --- */

    /**
     * Calculates the given file's algorithm hash code.
     *
     * @param resourceFile File to calculate
     *
     * @return Calculated checksum for the given file.
     *
     * @throws IOException on file reading errors.
     * @throws IllegalStateException when no algorithm for checksum can be found.
     */
    public static String calculateSHA1(File resourceFile) throws IOException {
        return calculateHash(resourceFile, HashAlgorithm.SHA1);
    }

    public static String calculateHash(File resourceFile, HashAlgorithm algorithm) throws IOException {
        return new HashCalculator().calculateHash(resourceFile, algorithm);
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
                dependencyInfo.setHeaderSha1(DigestUtils.sha1Hex(header.toString()));
                dependencyInfo.setFooterSha1(DigestUtils.sha1Hex(footer.toString()));
            }
        } catch (FileNotFoundException e) {
            // ignore
        } catch (IOException e) {
            // ignore
        }
    }

    public static void calculateSuperHash(DependencyInfo dependencyInfo, File dependencyFile) {
        StringBuilder superHash = new StringBuilder("");
        HashCalculator superHashCalculator = new HashCalculator();
        if (!dependencyFile.getName().toLowerCase().matches(FileExtensions.BINARY_FILE_EXTENSION_REGEX)) {
            try {
                HashCalculationResult superHashResult = superHashCalculator.calculateSuperHash(dependencyFile);
                if (superHashResult != null) {
                    dependencyInfo.setFullHash(superHashResult.getFullHash());
                    dependencyInfo.setMostSigBitsHash(superHashResult.getMostSigBitsHash());
                    dependencyInfo.setLeastSigBitsHash(superHashResult.getLeastSigBitsHash());
                    superHash.append(superHashResult.getFullHash());
                }
            } catch (IOException e) {
                logger.warn("Error calculating fullHash for {}, Error - ", dependencyFile.getName(), e.getMessage());
            }
        }
    }

    public static String calculateOtherPlatformSha1(File file) {
        String otherPlatformSha1 = null;
        if (file.length() <= org.whitesource.agent.hash.FileUtils.MAX_FILE_SIZE) {
            File otherPlatformFile = null;
            try {
                otherPlatformFile = org.whitesource.agent.hash.FileUtils.createOtherPlatformFile(file);
                if (otherPlatformFile != null) {
                    otherPlatformSha1 = ChecksumUtils.calculateSHA1(otherPlatformFile);
                    if (EMPTY_FILE_SHA1.equals(otherPlatformSha1)) {
                        otherPlatformSha1 = null;
                    }
                }
            } catch (Exception e) {
                logger.warn("Unable to create other platform file for {}: {}", file.getPath(), e.getMessage());
            } finally {
                FileHandler.deleteTempFoldersHelper(FileHandler.PATH_TO_PLATFORM_DEPENDENT_TMP_DIR);
            }
        } else {
            logger.debug("File {} size is too big for scanning other platform sha1, skipping it.", file.getName());
        }
        return otherPlatformSha1;
    }

    public static void calculateOtherPlatformSha1(DependencyInfo dependency, File file) {
        String otherPlatformSha1 = calculateOtherPlatformSha1(file);
        dependency.setOtherPlatformSha1(otherPlatformSha1);
    }

    /* --- Private static methods --- */

    private static void deleteFile(File file) {
        if (file != null) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                // do nothing
            }
        }
    }

}