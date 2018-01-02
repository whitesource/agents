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

import com.sun.jna.Platform;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import static org.junit.Assert.assertNull;

/**
 * Test class for {@link HashCalculator}.
 *
 * @author anna.rozin
 * @author tom.shapira
 */
public class HashCalculatorTest {

    /* --- Static members --- */

    private static String OS = System.getProperty("os.name").toLowerCase();

    private static final String EMPTY_FILE_SHA1 = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
    private static final String WINDOWS_NON_EMPTY_FILE_SHA1 = "7a3a53d8ddca62e9df1e04b56087bbbf852fe0d3";
    private static final String UNIX_NON_EMPTY_FILE_SHA1 = "e6f0001627a15d3a2b7bd8e1040649a828a13b9d";

    /* --- Test methods --- */

    @Test
    public void testBomFiles() throws IOException {
        HashCalculator hashCalculator = new HashCalculator();

        String filePath = getClass().getResource("/utf-bom/jquery-1.8.1-utf8.txt").getFile();
        File file = new File(filePath);

        String filePathBom = getClass().getResource("/utf-bom/jquery-1.8.1-utf8bom.txt").getFile();
        File fileBom = new File(filePathBom);

        String sha1 = hashCalculator.calculateSHA1(file);
        String sha1Bom = hashCalculator.calculateSHA1(fileBom);

        Assert.assertTrue(sha1.equals(sha1Bom));
    }

    @Test
    public void testSHA1() throws IOException {
        HashCalculator hashCalculator = new HashCalculator();
        
        String filePath = getClass().getResource("/empty-file.txt").getFile();
        File file = new File(URLDecoder.decode(filePath, "utf-8"));
        String sha1 = hashCalculator.calculateSHA1(file);
        Assert.assertEquals(EMPTY_FILE_SHA1, sha1);

        filePath = getClass().getResource("/non-empty-file.txt").getFile();
        file = new File(URLDecoder.decode(filePath, "utf-8"));
        sha1 = hashCalculator.calculateSHA1(file);
        if (Platform.isWindows()) {
            Assert.assertEquals(WINDOWS_NON_EMPTY_FILE_SHA1, sha1);
        } else {
            Assert.assertEquals(UNIX_NON_EMPTY_FILE_SHA1, sha1);
        }
    }

    @Test
    public void testSuperHash() throws IOException {
        HashCalculator hashCalculator = new HashCalculator();
        File originFile = new File(URLDecoder.decode(getClass().getResource("/superHash/origin.txt").getFile(), "utf-8"));
        HashCalculationResult originFileHashResult = hashCalculator.calculateSuperHash(originFile);

        File whitespaceChangesFile = new File(URLDecoder.decode(getClass().getResource("/superHash/whitespace-changes.txt").getFile(), "utf-8"));
        HashCalculationResult whitespaceFileHashResult = hashCalculator.calculateSuperHash(whitespaceChangesFile);

        File msbTestFile = new File(URLDecoder.decode(getClass().getResource("/superHash/msb-test.txt").getFile(), "utf-8"));
        HashCalculationResult msbTestFileHashResult = hashCalculator.calculateSuperHash(msbTestFile);

        File lsbTestFile = new File(URLDecoder.decode(getClass().getResource("/superHash/lsb-test.txt").getFile(), "utf-8"));
        HashCalculationResult lsbTestFileHashResult = hashCalculator.calculateSuperHash(lsbTestFile);

        Assert.assertEquals("Files must match despite whitespace changes", originFileHashResult.getFullHash(), whitespaceFileHashResult.getFullHash());
        Assert.assertEquals("File msb hashes must match", originFileHashResult.getMostSigBitsHash(), msbTestFileHashResult.getMostSigBitsHash());
        Assert.assertEquals("File lsb hashes must match", originFileHashResult.getLeastSigBitsHash(), lsbTestFileHashResult.getLeastSigBitsHash());

        File smallFile = new File(URLDecoder.decode(getClass().getResource("/superHash/small-file.txt").getFile(), "utf-8"));
        HashCalculationResult smallFileHashResult = hashCalculator.calculateSuperHash(smallFile);
        assertNull(smallFileHashResult);
    }

    /* --- Private methods --- */

    private boolean isWindows() {
        return OS.indexOf("win") >= 0;
    }

//    @Test
//    public void testLargeFile() throws IOException {
//        File largeFile = new File("C:\\WhiteSource\\env\\jboss\\maven\\index\\central\\_4z.fdt");
//        HashCalculationResult result = new HashCalculator().calculateSuperHash(largeFile);
//        assertNull(result);
//    }

}
