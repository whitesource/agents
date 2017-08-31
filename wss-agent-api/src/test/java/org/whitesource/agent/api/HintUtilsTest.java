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
package org.whitesource.agent.api;

import junit.framework.Assert;
import org.junit.Test;
import org.whitesource.agent.api.model.DependencyHintsInfo;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Test class for {@link HintUtils}.
 *
 * @author chen.luigi
 */
public class HintUtilsTest {

    /* --- Static members --- */

    private static final String ZED_GRAPH_DLL = "/pecoff4j/ZedGraph.dll";

    /* --- Test methods --- */

    @Test
    public void testDependencyHintsInfo() throws IOException {
        File dllFile = new File(URLDecoder.decode(getClass().getResource(ZED_GRAPH_DLL).getFile(), "utf-8"));
        DependencyHintsInfo dependencyHintsInfo = HintUtils.getPortableExecutableHints(dllFile.getPath());
        Assert.assertNotNull(dependencyHintsInfo);
        Assert.assertEquals("John Champion, et al.", dependencyHintsInfo.getCompanyName());
        Assert.assertEquals("5.1.7.430", dependencyHintsInfo.getFileVersion());
        Assert.assertEquals("Copyright © 2003-2007 John Champion", dependencyHintsInfo.getCopyright());
        Assert.assertEquals("ZedGraph.dll", dependencyHintsInfo.getOriginalFilename());
        Assert.assertEquals("ZedGraph Library", dependencyHintsInfo.getProductName());
        Assert.assertEquals("5.1.7.430", dependencyHintsInfo.getProductVersion());
    }

}