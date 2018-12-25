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

import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.whitesource.agent.api.model.ChecksumType;
import org.whitesource.agent.parser.JavaScriptParser;
import org.whitesource.agent.parser.ParseResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    private static final String SHA1 = "34b52cd823327013a78bd9c9f10d08e908d4d76d";
    private static final String NO_HEADER_SHA1 = "05a11a55eef5239b3ebb983f6234365014d937c0";
    private static final String NO_COMMENTS_SUPER_HASH = "a8d1acba4fe94512f71d8dd4666653a231abf6e1";

    private static final String JQUERY_JUSTIFIED_GALLERY_URL = "https://cdnjs.cloudflare.com/ajax/libs/justifiedGallery/3.7.0/js/jquery.justifiedGallery.js";

    /* --- Test methods --- */

    @Test
    public void testParseJavaScript() throws IOException {
//        String fileContent = IOUtils.toString(new URL("https://cdnjs.cloudflare.com/ajax/libs/hopscotch/0.2.0/js/hopscotch.js"), UTF_8);
        String fileContent = IOUtils.toString(new URL(JQUERY_JUSTIFIED_GALLERY_URL), StandardCharsets.UTF_8);
        ParseResult parseResult = new JavaScriptParser().parse(fileContent);
        String headerlessContent = parseResult.getContentWithoutHeaderComments();
        assertTrue(headerlessContent.startsWith("(function (factory) {"));
        String commentlessContent = parseResult.getContentWithoutComments();
        assertTrue(commentlessContent.startsWith("(function(factory) {"));
    }

//    @Ignore
    @Test
    public void testCalculateJavaScriptHash() throws IOException, WssHashException {
        HashCalculator hashCalculator = new HashCalculator();
        byte[] fileBytes = IOUtils.toByteArray(new URL(JQUERY_JUSTIFIED_GALLERY_URL));
        String normalSha1 = hashCalculator.calculateByteArraySHA1(fileBytes);
        Map<ChecksumType, String> javascriptChecksums = hashCalculator.calculateJavaScriptHashes(fileBytes);

        Assert.assertEquals(SHA1, normalSha1);
        Assert.assertEquals(NO_HEADER_SHA1, javascriptChecksums.get(ChecksumType.SHA1_NO_HEADER));
        Assert.assertEquals(NO_COMMENTS_SUPER_HASH, javascriptChecksums.get(ChecksumType.SHA1_NO_COMMENTS_SUPER_HASH));
    }

}