/**
 * Copyright (C) 2012 White Source Ltd.
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
package org.whitesource.agent.report;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Helper class for manipulating files while generating reports.
 *
 * @author Edo.Shor
 */
public final class FileUtils {

    /* --- Public methods --- */

    /**
     * Copy a classpath resource to a destination file.
     *
     * @param resource    Path to resource to copy.
     * @param destination File to copy resource to.
     * @throws IOException
     */
    public static void copyResource(String resource, File destination) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            is = FileUtils.class.getResourceAsStream("/" + resource);
            fos = new FileOutputStream(destination);

            final byte[] buffer = new byte[10 * 1024];
            int len;
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } finally {
            close(is);
            close(fos);
        }

    }

    /**
     * Closes a resource if it is not null. All possible IOExceptions are ignored
     *
     * @param resource resource to close
     */
    public static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e1) {
                // sshhhhhh....
            }
        }
    }

    /**
     * Pack a directory into a single zip file.
     * <p/>
     * <p>
     * <b>Note:</b> The implementation is based on the packZip method in jetbrains openAPI.
     * original author is Maxim Podkolzine (maxim.podkolzine@jetbrains.com)
     * </p>
     *
     * @param dir
     * @param zos
     * @throws IOException
     */
    public static void packDirectory(File dir, ZipOutputStream zos) throws IOException {
        byte[] buffer = new byte[64 * 1024];  // a reusable buffer
        try {
            traverseAndWrite(dir, zos, new StringBuilder(), true, buffer);
        } finally {
            close(zos);
        }
    }

    /**
     * Delete all files rooted in the given path.
     *
     * @param path root path to delete
     *
     * @return <code>True</code> if all files were deleted successfully.
     *
     * @throws FileNotFoundException in case path couldn't be found.
     */
    public static boolean deleteRecursive(File path) throws FileNotFoundException {
        if (!path.exists()) {
            throw new FileNotFoundException(path.getAbsolutePath());
        }

        boolean success = true;
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                success = success && FileUtils.deleteRecursive(f);
            }
        }

        return success && path.delete();
    }

    /* --- Private methods --- */

    private static void traverseAndWrite(File file, ZipOutputStream zos, StringBuilder pathBuilder, boolean isFirst, byte[] buffer)
            throws IOException {

        if (!isFirst) {
            pathBuilder.append(file.getName());
            if (!file.isFile()) {
                pathBuilder.append('/');
            }
        }

        if (file.isFile()) {
            final String path = pathBuilder.toString();
            final ZipEntry zipEntry = new ZipEntry(path);
            zipEntry.setTime(file.lastModified());
            zos.putNextEntry(zipEntry);

            InputStream input = new BufferedInputStream(new FileInputStream(file));
            try {
                int read;
                do {
                    read = input.read(buffer);
                    zos.write(buffer, 0, Math.max(read, 0));
                } while (read == buffer.length);
            } finally {
                close(input);
            }
        } else {
            final File[] files = file.listFiles();
            if (files != null) {
                int length = pathBuilder.length();
                for (File innerFile : files) {
                    traverseAndWrite(innerFile, zos, pathBuilder, false, buffer);
                    pathBuilder.setLength(length);
                }
            }
        }
    }

    /* --- Constructors --- */

    /**
     * Private constructor
     */
    private FileUtils() {
        // avoid instantiation
    }
}
