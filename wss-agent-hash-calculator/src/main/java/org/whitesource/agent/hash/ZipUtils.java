/**
 * Copyright (C) 2014 WhiteSource Ltd.
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

import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Utility class for various zip operations.
 *
 * @author tom.shapira
 */
public class ZipUtils {

    /* --- Static members --- */

    public static final String UTF_8 = "UTF-8";

    /* --- Static methods --- */

    /**
     * The method compresses the string using gzip.
     *
     * @param text The string to compress.
     *
     * @return The compressed string.
     *
     * @throws java.io.IOException
     */
    public static String compress(String text) throws IOException {
        String result;
        if (text != null && text.length() > 0) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(text.length());
            GZIPOutputStream gzipos = null;
            try {
                gzipos = new GZIPOutputStream(baos);
                gzipos.write(text.getBytes(UTF_8));
                gzipos.close();
                baos.close();
                result = (new BASE64Encoder()).encode(baos.toByteArray());
                /* TODO
                Replace result raw to this one : result = Base64.encodeBase64String(baos.toByteArray());
                See :
                Should not be using classes that are in sun.* packages - those classes are not part of the public API
                Java and can change in any new Java version
                http://stackoverflow.com/questions/29692146/java-lang-noclassdeffounderror-sun-misc-base64encoder
                http://www.oracle.com/technetwork/java/faq-sun-packages-142232.html
                */
            } catch (IOException e) {
                result = text;
            } finally {
                baos.close();
                if (gzipos != null) {
                    gzipos.close();
                }
            }
        } else {
            result = text;
        }
        return result;
    }

    /* --- Constructors --- */

    /**
     * Private default constructor
     */
    private ZipUtils() {
        // avoid instantiation
    }
}
