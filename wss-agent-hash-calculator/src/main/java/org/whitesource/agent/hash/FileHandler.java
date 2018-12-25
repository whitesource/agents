package org.whitesource.agent.hash;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author philip.abed
 */
public class FileHandler {
    private static String EMPTY_STRING = "";
    private static String UNDERSCORE = "_";
    /* --- public static methods --- */
    private static final String WHITESOURCE_PLATFORM_DEPENDENT_TMP_DIR = "WhiteSource-PlatformDependentFiles";
    public static final String PLATFORM_DEPENDENT_TMP_DIRECTORY = FileHandler.createUniqueName(WHITESOURCE_PLATFORM_DEPENDENT_TMP_DIR, EMPTY_STRING);
    public static final String PATH_TO_PLATFORM_DEPENDENT_TMP_DIR = Paths.get(System.getProperty("java.io.tmpdir"), PLATFORM_DEPENDENT_TMP_DIRECTORY).toString();


    public static String createUniqueName(String name, String extension) {
        if (name == null) {
            name = EMPTY_STRING;
        }
        if (extension == null) {
            extension = EMPTY_STRING;
        }
        String creationDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uuid = UUID.randomUUID().toString();
        return name + UNDERSCORE + creationDate + UNDERSCORE + uuid + extension;
    }

    public static void deleteTempFoldersHelper(String path) {
        if (path != null) {
            File file = new File(path);
            if (file != null) {
                deleteDirectory(file);
            }
        }
    }
    public static void deleteDirectory(File directory) {
        if (directory != null) {
            try {
                FileUtils.forceDelete(directory);
            } catch (IOException e) {
                // do nothing
            }
        }
    }
}
