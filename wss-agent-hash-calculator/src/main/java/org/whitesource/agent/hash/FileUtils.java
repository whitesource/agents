package org.whitesource.agent.hash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for modifying files.
 *
 * @author tom.shapira
 */
public final class FileUtils {

    /* --- Static members --- */

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

//    private static final String PLATFORM_DEPENDENT_TMP_DIRECTORY = System.getProperty("java.io.tmpdir") + File.separator + "WhiteSource-PlatformDependentFiles";

    private static final String EMPTY_STRING = "";

    public static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10mb

    private static final String CRLF = "\r\n";
    private static final String NEW_LINE = "\n";


    /* --- Constructors --- */

    /**
     * Private default constructor
     */
    private FileUtils() {
        // avoid instantiation
    }

    /* --- Public methods --- */

    public static File createOtherPlatformFile(File originalPlatform) {
        // calculate other platform sha1 for files larger than MAX_FILE_SIZE
        long length = originalPlatform.length();
        if (length < MAX_FILE_SIZE && length < Runtime.getRuntime().freeMemory()) {
            try {
                byte[] byteArray = org.apache.commons.io.FileUtils.readFileToByteArray(originalPlatform);

                String fileText = new String(byteArray);
                File otherPlatformFile = new File(FileHandler.PATH_TO_PLATFORM_DEPENDENT_TMP_DIR, originalPlatform.getName());
                if (fileText.contains(CRLF)) {
                    org.apache.commons.io.FileUtils.write(otherPlatformFile, fileText.replaceAll(CRLF, NEW_LINE));
                } else if (fileText.contains(NEW_LINE)) {
                    org.apache.commons.io.FileUtils.write(otherPlatformFile, fileText.replaceAll(NEW_LINE, CRLF));
                }

                if (otherPlatformFile.exists()) {
                    return otherPlatformFile;
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    /* --- Private methods --- */

    private static String cleanLine(String line) {
        return line.replace("/**", EMPTY_STRING).replace("/*", EMPTY_STRING)
                .replace("*", EMPTY_STRING).replace("#", EMPTY_STRING)
                .replace("/", EMPTY_STRING).replace("\\t", EMPTY_STRING)
                .replace("\\n", EMPTY_STRING).trim();
    }

    private static void deleteFile(File file) {
        if (file != null) {
            try {
                org.apache.commons.io.FileUtils.forceDelete(file);
            } catch (IOException e) {
                // do nothing
            }
        }
    }
}
