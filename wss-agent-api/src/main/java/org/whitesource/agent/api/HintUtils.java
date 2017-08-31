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

import org.boris.pecoff4j.PE;
import org.boris.pecoff4j.ResourceDirectory;
import org.boris.pecoff4j.ResourceEntry;
import org.boris.pecoff4j.constant.ResourceType;
import org.boris.pecoff4j.io.PEParser;
import org.boris.pecoff4j.io.ResourceParser;
import org.boris.pecoff4j.resources.StringFileInfo;
import org.boris.pecoff4j.resources.StringTable;
import org.boris.pecoff4j.resources.VersionInfo;
import org.boris.pecoff4j.util.ResourceHelper;
import org.whitesource.agent.api.model.DependencyHintsInfo;

/**
 * Utility class for populating hint data.
 *
 * @author chen.luigi
 */
public final class HintUtils {

    /* --- Static members --- */

    // hints for .NET (dll, exe)
    public static final String COMPANY_NAME = "CompanyName";
    public static final String FILE_VERSION = "FileVersion";
    public static final String COPYRIGHT = "LegalCopyright";
    public static final String ORIGINAL_FILE_NAME = "OriginalFilename";
    public static final String PRODUCT_NAME = "ProductName";
    public static final String PRODUCT_VERSION = "ProductVersion";

    /* --- Constructors --- */

    /**
     * Private default constructor
     */
    private HintUtils() {
        // avoid instantiation
    }

    /* --- Static methods --- */

    public static DependencyHintsInfo getPortableExecutableHints(String filename) {
        DependencyHintsInfo hints = null;
        try {
            // parse PE (Portable Executable) file
            PE pe = PEParser.parse(filename);
            ResourceDirectory rd = pe.getImageData().getResourceTable();
            ResourceEntry[] entries = ResourceHelper.findResources(rd, ResourceType.VERSION_INFO);
            byte[] data = entries[0].getData();
            VersionInfo version = ResourceParser.readVersionInfo(data);
            StringFileInfo stringFileInfo = version.getStringFileInfo();
            StringTable table = stringFileInfo.getTable(0);

            // collect hints
            hints = new DependencyHintsInfo();
            for (int i = 0; i < table.getCount(); i++) {
                String value = table.getString(i).getValue();
                switch (table.getString(i).getKey()) {
                    case COMPANY_NAME:
                        hints.setCompanyName(value);
                        break;
                    case FILE_VERSION:
                        hints.setFileVersion(value);
                        break;
                    case COPYRIGHT:
                        hints.setCopyright(value);
                        break;
                    case ORIGINAL_FILE_NAME:
                        hints.setOriginalFilename(value);
                        break;
                    case PRODUCT_NAME:
                        hints.setProductName(value);
                        break;
                    case PRODUCT_VERSION:
                        hints.setProductVersion(value);
                        break;
                    default: break;
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return hints;
    }
}