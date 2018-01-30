package org.whitesource.agent.hash;

import java.util.*;

/**
 * Created by anna.rozin
 */
public class FileExtensions {

    /* --- Static members --- */

    private static final String FILE_EXTENSION_PREFIX_REGEX = ".*\\.";

    private static final String OR_REGEX = "|";

    public static final String GLOB_PATTERN_PREFIX = "**/*.";

    public static final String BINARY_FILE_EXTENSION_REGEX;

    public static final List<String> SOURCE_EXTENSIONS = Arrays.asList(
            "c", "cc", "cp", "cpp", "cxx", "c\\+\\+", "h", "hh", "hpp", "hxx", "h\\+\\+", "m", "mm", "pch", // C and C++
            "c#", "cs", "csharp",  // C#
            "go", "goc",  // GO
            "js", // JavaScript
            "pl", "plx", "pm", "ph", "cgi", "fcgi", "pod", "psgi", "al", "perl", "t", // PERL
            "pl6", "p6m", "p6l", "pm6", "nqp", "6pl", "6pm", "p6", // PERL6
            "php", // PHP
            "py", // Python
            "rb", // Ruby
            "swift", // Swift
            "java", // Java
            "clj", "cljx", "cljs", "cljc"); // Clojure

    private static final Collection<String> GENERIC_RESOURCE_FILE_EXTENSIONS = new ArrayList<>(
            Arrays.asList("air", "apk", "bz2", "deb", "drpm", "dmg", "egg", "exe",
                    "gem", "gzip", "jar", "msi", "nupkg", "rpm", "swc", "swf",
                    "tar.bz2", "tar.gz", "tar", "tgz", "pkg.tar.xz", "udeb", "whl", "zip", "(u)?deb", "(a)?rpm"));


    public static final List<String> ARCHIVE_EXTENSIONS = Arrays.asList("war", "ear", "zip", "whl", "tar.gz", "tgz", "tar", "car", "aar", "jar");
    private static final Collection<String> DOT_NET_FILE_EXTENSIONS = new ArrayList<>(
            Arrays.asList("dll", "exe"));
    public static final String SOURCE_FILE_PATTERN;
    public static final String BINARY_FILE_PATTERN;
    public static final String ARCHIVE_FILE_PATTERN;
    public static final String[] INCLUDES;
    public static final String[] EXCLUDES = new String[]{"**/*sources.jar", "**/*javadoc.jar", "**/tests/**"};
    public static final String[] ARCHIVE_INCLUDES;
    public static final String[] ARCHIVE_EXCLUDES = new String[]{"**/*sources.jar", "**/*javadoc.jar", "**/tests/**"};


    static {
        SOURCE_FILE_PATTERN = buildRegexString(SOURCE_EXTENSIONS);
        BINARY_FILE_PATTERN = buildRegexString(GENERIC_RESOURCE_FILE_EXTENSIONS);
        ARCHIVE_FILE_PATTERN = buildRegexString(ARCHIVE_EXTENSIONS);

        StringBuilder sb = new StringBuilder();
        sb.append(BINARY_FILE_PATTERN);
        sb.append(OR_REGEX);
        sb.append(ARCHIVE_FILE_PATTERN);
        sb.append(OR_REGEX);
        sb.append(buildRegexString(DOT_NET_FILE_EXTENSIONS));
        BINARY_FILE_EXTENSION_REGEX = sb.toString();

        List<String> allExtensions = new ArrayList<>();
        allExtensions.addAll(SOURCE_EXTENSIONS);
        allExtensions.addAll(GENERIC_RESOURCE_FILE_EXTENSIONS);
        INCLUDES = initializeGlobPattern(allExtensions);
        ARCHIVE_INCLUDES = initializeGlobPattern(ARCHIVE_EXTENSIONS);
    }

    /* --- Private methods --- */

    private static String buildRegexString(Collection<String> extensions) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = extensions.iterator();
        while (iterator.hasNext()) {
            String extension = iterator.next();
            sb.append(FILE_EXTENSION_PREFIX_REGEX);
            sb.append(extension);
            if (iterator.hasNext()) {
                sb.append(OR_REGEX);
            }
        }
        return sb.toString();
    }

    private static String[] initializeGlobPattern(List<String> extensions) {
        String[] globPatterns = new String[extensions.size()];
        for (int i = 0; i < extensions.size(); i++) {
            globPatterns[i] = GLOB_PATTERN_PREFIX + extensions.get(i);
        }
        return globPatterns;
    }

}