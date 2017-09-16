/**
 * Copyright (C) 2012 White Source Ltd.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whitesource.agent.hash;

import com.sun.jna.Platform;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * @author Edo.Shor
 */
public class ChecksumUtilsTest {

    /* --- Static members --- */

    private static final String EMPTY_FILE_SHA1 = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
    private static final String NON_EMPTY_FILE_WINDOWS_SHA1 = "7a3a53d8ddca62e9df1e04b56087bbbf852fe0d3";
    private static final String NON_EMPTY_FILE_UNIX_SHA1 = "e6f0001627a15d3a2b7bd8e1040649a828a13b9d";
    private static final String EMPTY_FILE_MD5 = "d41d8cd98f00b204e9800998ecf8427e";
    private static final String NON_EMPTY_FILE_WINDOWS_MD5 = "a14be48c41c3dc60bacc578941c42ad2";
    private static final String NON_EMPTY_FILE_UNIX_MD5 = "b5c9feed84dbcbeb9d600959f2c87d07";

    private static final String EMPTY_FILE_TXT = "/empty-file.txt";
    private static final String NON_EMPTY_FILE_TXT = "/non-empty-file.txt";
    private static final String UTF_8 = "utf-8";

    /* --- Test methods --- */

    @Test
    public void testEmptyFileSHA1() throws IOException {
        String filePath = getClass().getResource(EMPTY_FILE_TXT).getFile();
        File file = new File(URLDecoder.decode(filePath, UTF_8));
        String sha1 = ChecksumUtils.calculateSHA1(file);
        Assert.assertEquals(EMPTY_FILE_SHA1, sha1);
    }

    @Test
    public void testSHA1() throws IOException {
        String filePath = getClass().getResource(NON_EMPTY_FILE_TXT).getFile();
        File file = new File(URLDecoder.decode(filePath, UTF_8));
        String sha1 = ChecksumUtils.calculateSHA1(file);
        if (Platform.isWindows()) {
            Assert.assertEquals(NON_EMPTY_FILE_WINDOWS_SHA1, sha1);
        } else {
            Assert.assertEquals(NON_EMPTY_FILE_UNIX_SHA1, sha1);
        }
    }

    @Test
    public void testEmptyFileMD5() throws IOException {
        String filePath = getClass().getResource(EMPTY_FILE_TXT).getFile();
        File file = new File(URLDecoder.decode(filePath, UTF_8));
        String md5 = ChecksumUtils.calculateHash(file, HashAlgorithm.MD5);
        Assert.assertEquals(EMPTY_FILE_MD5, md5);
    }

    @Test
    public void testMD5() throws IOException {
        String filePath = getClass().getResource(NON_EMPTY_FILE_TXT).getFile();
        File file = new File(URLDecoder.decode(filePath, UTF_8));
        String md5 = ChecksumUtils.calculateHash(file, HashAlgorithm.MD5);
        if (Platform.isWindows()) {
            Assert.assertEquals(NON_EMPTY_FILE_WINDOWS_MD5, md5);
        } else {
            Assert.assertEquals(NON_EMPTY_FILE_UNIX_MD5, md5);
        }
    }

    @Test
    public void testSuperHash() throws IOException {
//        File originFile = new File(URLDecoder.decode(getClass().getResource("/superHash/origin.txt").getFile(), "utf-8"));
//        ChecksumUtils.HashCalculationResult originFileHashResult = ChecksumUtils.calculateSuperHash(originFile);
//
//        File whitespaceChangesFile = new File(URLDecoder.decode(getClass().getResource("/superHash/whitespace-changes.txt").getFile(), "utf-8"));
//        ChecksumUtils.HashCalculationResult whitespaceFileHashResult = ChecksumUtils.calculateSuperHash(whitespaceChangesFile);
//
//        File msbTestFile = new File(URLDecoder.decode(getClass().getResource("/superHash/msb-test.txt").getFile(), "utf-8"));
//        ChecksumUtils.HashCalculationResult msbTestFileHashResult = ChecksumUtils.calculateSuperHash(msbTestFile);
//
//        File lsbTestFile = new File(URLDecoder.decode(getClass().getResource("/superHash/lsb-test.txt").getFile(), "utf-8"));
//        ChecksumUtils.HashCalculationResult lsbTestFileHashResult = ChecksumUtils.calculateSuperHash(lsbTestFile);
//
//        assertEquals("Files must match despite whitespace changes", originFileHashResult.getFullHash(), whitespaceFileHashResult.getFullHash());
//        assertEquals("File msb hashes must match", originFileHashResult.getMostSigBitsHash(), msbTestFileHashResult.getMostSigBitsHash());
//        assertEquals("File lsb hashes must match", originFileHashResult.getLeastSigBitsHash(), lsbTestFileHashResult.getLeastSigBitsHash());

//        DirectoryScanner scanner = new DirectoryScanner();
//        scanner.setBasedir("C:\\Users\\User\\Downloads\\CdnJS");
//        scanner.scan();
//        String[] fileNames = scanner.getIncludedFiles();
//        for (String fileName : fileNames) {
//            File file = new File(scanner.getBasedir(), fileName);
//            ChecksumUtils.HashCalculationResult result = ChecksumUtils.calculateSuperHash(file);
//            if (result != null) {
//                System.out.println(fileName + " : " + result.getFullHash() + " | " + result.getMostSigBitsHash() + " | " + result.getLeastSigBitsHash());
//            }
//        }
    }

}
