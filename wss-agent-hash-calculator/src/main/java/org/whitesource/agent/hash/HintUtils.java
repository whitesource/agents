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
import org.apache.commons.lang.StringUtils;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * Utility class for populating hint data.
 *
 * @author chen.luigi
 */
public final class HintUtils {

    /* --- Static members --- */

    private static final Logger logger = Logger.getLogger(HintUtils.class.getName());

    // patterns
    private static final String PORTABLE_EXECUTABLES_PATTERN = ".*\\.dll|.*\\.exe|.*\\.msi";
    private static final String COMMA_SPLIT = ",";
    private static final String EQUAL_SPLIT = "=";
    private static final String AUTH_CODE_SIGNATURE_ISSUER_PATTERN = "(Get-AuthenticodeSignature {0}).SignerCertificate.IssuerName";
    private static final String AUTH_CODE_SIGNATURE_SUBJECT_PATTERN = "(Get-AuthenticodeSignature {0}).SignerCertificate.Subject";
    private static final String POWER_SHELL = "powershell.exe";
    private static final String POWER_SHELL_COMMAND = "-Command";

    // PE hints for .NET (dll, exe)
    private static final String COMPANY_NAME = "CompanyName";
    private static final String FILE_VERSION = "FileVersion";
    private static final String COPYRIGHT = "LegalCopyright";
    private static final String ORIGINAL_FILE_NAME = "OriginalFilename";
    private static final String PRODUCT_NAME = "ProductName";
    private static final String PRODUCT_VERSION = "ProductVersion";
    private static final String MISSING_VALUE = "N/A";

    // Signature hint for .NET (dll, exe)
    private static final String COMMON_NAME_PARAMETER = "CN";

    /* --- Constructors --- */

    /**
     * Private default constructor
     */
    private HintUtils() {
        // avoid instantiation
    }

    /* --- Static methods --- */

    public static DependencyHintsInfo getHints(String filename) {
        DependencyHintsInfo hints = null;
        if (StringUtils.isNotBlank(filename)) {
            if (filename.matches(PORTABLE_EXECUTABLES_PATTERN)) {
                hints = getPortableExecutableHints(filename);
            }
        }
        return hints;
    }

    /* --- Private methods --- */

    private static DependencyHintsInfo getPortableExecutableHints(String filename) {
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

            // collect general details
            hints = new DependencyHintsInfo();
            for (int i = 0; i < table.getCount(); i++) {
                String value = table.getString(i).getValue();
                if (StringUtils.isNotBlank(value) && !value.equals(MISSING_VALUE)) {
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
            }

            if (Platform.isWindows()) {
                hints.setIssuerCommonName(executePowerShellCommand(MessageFormat.format(AUTH_CODE_SIGNATURE_ISSUER_PATTERN, filename)));
                hints.setSubjectCommonName(executePowerShellCommand(MessageFormat.format(AUTH_CODE_SIGNATURE_SUBJECT_PATTERN, filename)));
            }

            // TODO handle msi

        } catch (Exception e) {
            // do nothing
        }
        return hints;
    }

    // Get the signature from file by execute process from power shell
    private static String executePowerShellCommand(String command) {
        try {
            String[] commandList = {POWER_SHELL, POWER_SHELL_COMMAND, command};
            ProcessBuilder pb = new ProcessBuilder(commandList);
            Process process = pb.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(COMMON_NAME_PARAMETER)) {
                    String[] values = line.split(COMMA_SPLIT);
                    return values[0].substring(values[0].indexOf(EQUAL_SPLIT) + 1);
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return null;
    }
}