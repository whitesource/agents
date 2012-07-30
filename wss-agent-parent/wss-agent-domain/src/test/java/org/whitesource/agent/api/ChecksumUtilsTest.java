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

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import static org.junit.Assert.assertEquals;

/**
 * @author Edo.Shor
 */
public class ChecksumUtilsTest {

    private static final String EMPTY_FILE_SHA1 = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
    private static final String NON_EMPTY_FILE_SHA1 = "7a3a53d8ddca62e9df1e04b56087bbbf852fe0d3";

    @Test
    public void testSHA1() throws IOException {
        String filePath = getClass().getResource("/empty-file.txt").getFile();
        File file = new File(URLDecoder.decode(filePath, "utf-8"));
        String sha1 = ChecksumUtils.calculateSHA1(file);
        assertEquals(sha1, EMPTY_FILE_SHA1);

        filePath = getClass().getResource("/non-empty-file.txt").getFile();
        file = new File(URLDecoder.decode(filePath, "utf-8"));
        sha1 = ChecksumUtils.calculateSHA1(file);
        assertEquals(sha1, NON_EMPTY_FILE_SHA1);

    }

}
