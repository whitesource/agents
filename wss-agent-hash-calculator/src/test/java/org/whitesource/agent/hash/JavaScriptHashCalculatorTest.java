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

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.whitesource.agent.api.model.ChecksumType;
import org.whitesource.agent.parser.JavaScriptParser;
import org.whitesource.agent.parser.ParseResult;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link HashCalculator}.
 *
 * @author asaf.savich
 */
public class JavaScriptHashCalculatorTest {

    /* --- Static members --- */

    private static final String SHA1 = "3ac87eac2231436c81aaf3b2a7f03efcedcd8f54";
    private static final String NO_HEADER_SHA1 = "1bcc423f0dcc0c8af51f0c6aa2d825d72148292b";
    private static final String NO_COMMENTS_SUPER_HASH = "8a2c586e09f02890f7dbacb945a55b1ae1f03d91";

    private static final String JQUERY_JUSTIFIED_GALLERY_URL = "https://cdnjs.cloudflare.com/ajax/libs/justifiedGallery/3.6.3/js/jquery.justifiedGallery.js";
    private static final String UTF_8 = "utf-8";

    /* --- Test methods --- */

    @Test
    public void testParseJavaScript() throws IOException {
//        String fileContent = IOUtils.toString(new URL("https://cdnjs.cloudflare.com/ajax/libs/hopscotch/0.2.0/js/hopscotch.js"), UTF_8);
        String fileContent = IOUtils.toString(new URL(JQUERY_JUSTIFIED_GALLERY_URL), UTF_8);
        ParseResult parseResult = new JavaScriptParser().parse(fileContent);
        String headerlessContent = parseResult.getContentWithoutHeaderComments();
        assertTrue(headerlessContent.startsWith("(function($) {"));
        String commentlessContent = parseResult.getContentWithoutComments();
        assertTrue(commentlessContent.startsWith("(function($) {"));
    }

//    @Ignore
    @Test
    public void testCalculateJavaScriptHash() throws IOException {
        HashCalculator hashCalculator = new HashCalculator();
        try {
            String fileContent = IOUtils.toString(new URL(JQUERY_JUSTIFIED_GALLERY_URL), UTF_8);
            try {
                byte[] fileBytes = fileContent.getBytes();
                String normalSha1 = hashCalculator.calculateByteArraySHA1(fileBytes);
                Map<ChecksumType, String> javascriptChecksums = hashCalculator.calculateJavaScriptHashes(fileBytes);

                assertEquals(SHA1, normalSha1);
                assertEquals(NO_HEADER_SHA1, javascriptChecksums.get(ChecksumType.SHA1_NO_HEADER));
                assertEquals(NO_COMMENTS_SUPER_HASH, javascriptChecksums.get(ChecksumType.SHA1_NO_COMMENTS_SUPER_HASH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
