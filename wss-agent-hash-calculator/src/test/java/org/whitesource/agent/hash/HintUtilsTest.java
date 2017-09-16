/**
 * Copyright (C) 2017 White Source Ltd.
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
import org.junit.Ignore;
import org.junit.Test;
import org.whitesource.agent.api.model.DependencyHintsInfo;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Test class for {@link HintUtils}.
 *
 * @author chen.luigi
 */
public class HintUtilsTest {

    /* --- Static members --- */

    private static final String DLL_FILE = "/pecoff4j/Signature.dll";
    private static final String EXE_FILE = "/pecoff4j/java-rmi.exe";
    private static final String MSI_FILE = "/pecoff4j/3ba03ffe.msi";
    private static final String JAR_FILE = "/pecoff4j/aopalliance-1.0.jar";
    private static final String UTF_8 = "utf-8";

    /* --- Test methods --- */

    @Test
    public void testGetHintsDll() throws IOException {
        File file = getResourceFile(DLL_FILE);
        DependencyHintsInfo dependencyHintsInfo = HintUtils.getHints(file.getPath());
        Assert.assertNotNull(dependencyHintsInfo);
        Assert.assertEquals("Microsoft Corporation", dependencyHintsInfo.getCompanyName());
        Assert.assertEquals("6.1.40302.0", dependencyHintsInfo.getFileVersion());
        Assert.assertEquals("© Microsoft Corporation.  All rights reserved.", dependencyHintsInfo.getCopyright());
        Assert.assertEquals("EntityFramework.SqlServer.dll", dependencyHintsInfo.getOriginalFilename());
        Assert.assertEquals("Microsoft Entity Framework", dependencyHintsInfo.getProductName());
        Assert.assertEquals("6.1.3-40302", dependencyHintsInfo.getProductVersion());

        if (Platform.isWindows()) {
            Assert.assertEquals("Microsoft Code Signing PCA", dependencyHintsInfo.getIssuerCommonName());
            Assert.assertEquals("Microsoft Corporation", dependencyHintsInfo.getSubjectCommonName());
        } else {
            Assert.assertNull(dependencyHintsInfo.getIssuerCommonName());
            Assert.assertNull(dependencyHintsInfo.getSubjectCommonName());
        }
    }

    @Test
    public void testGetHintsExe() throws IOException {
        File file = getResourceFile(EXE_FILE);
        DependencyHintsInfo dependencyHintsInfo = HintUtils.getHints(file.getPath());
        Assert.assertNotNull(dependencyHintsInfo);
        Assert.assertNull(dependencyHintsInfo.getCompanyName());
        Assert.assertEquals("8.0.1520.6", dependencyHintsInfo.getFileVersion());
        Assert.assertEquals("Copyright © 2017", dependencyHintsInfo.getCopyright());
        Assert.assertEquals("java-rmi.exe", dependencyHintsInfo.getOriginalFilename());
        Assert.assertEquals("OpenJDK Platform 8", dependencyHintsInfo.getProductName());
        Assert.assertEquals("8.0.1520.6", dependencyHintsInfo.getProductVersion());
    }

    @Ignore
    @Test
    public void testGetHintsMsi() throws IOException {
        // TODO test when implemented
        File file = getResourceFile(MSI_FILE);
        DependencyHintsInfo dependencyHintsInfo = HintUtils.getHints(file.getPath());
        Assert.assertNotNull(dependencyHintsInfo);
    }

    @Test
    public void testGetHintsJar() throws IOException {
        File file = getResourceFile(JAR_FILE);
        DependencyHintsInfo dependencyHintsInfo = HintUtils.getHints(file.getPath());
        Assert.assertNull(dependencyHintsInfo);
    }

    /* --- Private methods --- */

    private File getResourceFile(String filename) throws UnsupportedEncodingException {
        return new File(URLDecoder.decode(getClass().getResource(filename).getFile(), UTF_8));
    }
}