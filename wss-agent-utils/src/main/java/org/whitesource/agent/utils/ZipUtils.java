package org.whitesource.agent.utils;

/**
 * Copyright (C) 2014 WhiteSource Ltd.
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

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// import sun.misc.BASE64Decoder;
// import sun.misc.BASE64Encoder;

/**
 * Utility class for various zip operations.
 *
 * @author tom.shapira
 * @author eugen.horovitz
 */
public class ZipUtils {

    /* --- Static members --- */

    //private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

    private static final String JAVA_TEMP_DIR = System.getProperty("java.io.tmpdir");
    private static final String ZIP_UTILS = System.getProperty("WSZipUtils");
    private static final String UTF_8 = "UTF-8";
    private static final int BYTES_BUFFER_SIZE = 32 * 1024;
    private static final int STRING_MAX_SIZE = BYTES_BUFFER_SIZE;
    private static final String TMP_IN_ = "tmp_in_";
    private static final String TMP_OUT_ = "tmp_out_";
    private static final String ZIP_UTILS_SUFFIX = ".json";

    private static final int N_THREADS = 2;

    /* --- Static methods --- */

    /**
     * The method compresses the string using gzip.
     *
     * @param text The string to compress.
     * @return The compressed string.
     * @throws java.io.IOException Does some thing in old style.
     * @deprecated use {@link #compressString(String)} instead.
     */
    @Deprecated
    public static String compress(String text) throws IOException {
        String result;
        if (text != null && text.length() > 0) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(text.length());
                 GZIPOutputStream gzipos = new GZIPOutputStream(baos);) {
                gzipos.write(text.getBytes(UTF_8));
                gzipos.close();
                baos.close();
                // result = (new BASE64Encoder()).encode(baos.toByteArray());
                result = Base64.encodeBase64String(baos.toByteArray());
                /* TODO
                Replace result raw to this one : result = Base64.encodeBase64String(baos.toByteArray());
                See :
                Should not be using classes that are in sun.* packages - those classes are not part of the public API
                Java and can change in any new Java version
                http://stackoverflow.com/questions/29692146/java-lang-noclassdeffounderror-sun-misc-base64encoder
                http://www.oracle.com/technetwork/java/faq-sun-packages-142232.html
                */
            }
        } else {
            result = text;
        }
        return result;
    }

    /**
     * The method decompresses the string using gzip.
     *
     * @param text The string to decompress.
     * @return The decompressed string.
     * @throws java.io.IOException Does some thing in old style.
     * @deprecated use {@link #decompressString(String)} instead.
     */
    public static String decompress(String text) throws IOException {
        if (text == null || text.length() == 0) {
            return text;
        }

        // byte[] bytes = new BASE64Decoder().decodeBuffer(text);
        byte[] bytes = Base64.decodeBase64(text);

        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
             BufferedReader bf = new BufferedReader(new InputStreamReader(gis, UTF_8));) {
            StringBuilder outStr = new StringBuilder();
            String line;
            while ((line = bf.readLine()) != null) {
                outStr.append(line);
            }
            return outStr.toString();
        }
    }

    /**
     * The method decompresses the big strings using gzip - low memory via the File system
     *
     * @param text The string to decompress.
     * @return The decompressed temp file path that should be deleted on a later stage.
     * @throws java.io.IOException
     */
    public static Path decompressChunks(String text) throws IOException {
        File tempFileOut = File.createTempFile(TMP_OUT_, ZIP_UTILS_SUFFIX, new File(JAVA_TEMP_DIR));
        if (text == null || text.length() == 0) {
            return null;
        }

        // todo make it in chunks

        byte[] bytes = Base64.decodeBase64(text);
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            decompressChunks(inputStream, tempFileOut.toPath());
            return tempFileOut.toPath();
        }
    }

    /**
     * The method decompresses the big strings using gzip - low memory via the File system
     *
     * @param inputStream
     * @param tempFileOut
     * @throws IOException
     */
    public static void decompressChunks(InputStream inputStream, Path tempFileOut) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(tempFileOut)) {
            try (GZIPInputStream chunkZipper = new GZIPInputStream(inputStream);
                 InputStream in = new BufferedInputStream(chunkZipper);) {
                byte[] buffer = new byte[BYTES_BUFFER_SIZE];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    byte[] writtenBytes = new byte[len];
                    getBytes(buffer, 0, len, writtenBytes, 0);
                    String val = new String(writtenBytes, StandardCharsets.UTF_8);
                    writer.write(val);
                }
            }
        }
    }

    public static String compressString(String text) throws IOException {
        try (ByteArrayOutputStream exportByteArrayOutputStream = new ByteArrayOutputStream()) {
            fillExportStreamCompress(text, exportByteArrayOutputStream);
            return getStringFromEncode(exportByteArrayOutputStream.toByteArray());
        }
    }

    /**
     * The method compresses the big strings using gzip - low memory via Streams
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void compressString(InputStream inputStream, OutputStream outputStream) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            fillExportStreamCompress(inputStream, byteArrayOutputStream);
            // to do -> make it in chunks
            String result = getStringFromEncode(byteArrayOutputStream.toByteArray());
            try (PrintWriter printWriter = new PrintWriter(outputStream)) {
                printWriter.write(result);
            }
        }
    }


    public static String decompressString(String text) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        fillExportStreamDecompress(text, stringBuilder);
        return stringBuilder.toString();
    }

    /**
     * The method decompresses the big strings using gzip - low memory via Streams
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void decompressString(InputStream inputStream, OutputStream outputStream) throws IOException {
        try (InputStream bufferedInputStream = new BufferedInputStream(inputStream);
             PrintWriter printWriter = new PrintWriter(outputStream);) {
            byte[] buffer = new byte[BYTES_BUFFER_SIZE];
            int len;
            while ((len = bufferedInputStream.read(buffer)) > 0) {
                String val = new String(buffer, StandardCharsets.UTF_8);
                String str = decompressString(val);
                printWriter.write(str);
            }
        }
    }

    /**
     * The method compresses the big strings using gzip - low memory via the File system
     *
     * @param text The string to compress.
     * @return The compressed string.
     * @throws java.io.IOException
     */
    public static String compressChunks(String text) throws IOException {
        Path tempFolder = Paths.get(JAVA_TEMP_DIR, ZIP_UTILS);
        File tempFileIn = File.createTempFile(TMP_IN_, ZIP_UTILS_SUFFIX, tempFolder.toFile());
        File tempFileOut = File.createTempFile(TMP_OUT_, ZIP_UTILS_SUFFIX, tempFolder.toFile());

        writeChunkBytes(text, tempFileIn);
        String result;
        if (text != null && text.length() > 0) {
            try (InputStream in = new FileInputStream(tempFileIn);
                 FileOutputStream fileOutputStream = new FileOutputStream(tempFileOut);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                 OutputStream out = new GZIPOutputStream(bufferedOutputStream);) {

                byte[] bytes = new byte[BYTES_BUFFER_SIZE];
                int len;
                while ((len = in.read(bytes)) > 0) {
                    out.write(bytes, 0, len);
                }

                in.close();
                out.flush();
                out.close();

                // result = new BASE64Encoder().encode(Files.readAllBytes(tempFileOut.toPath()));
                result = Base64.encodeBase64String(Files.readAllBytes(tempFileOut.toPath()));
            }
        } else {
            result = text;
        }

        Files.deleteIfExists(tempFileIn.toPath());
        Files.deleteIfExists(tempFileOut.toPath());

        return result;
    }

    /* --- Compress Helpers --- */

    private static String getStringFromEncode(byte[] bytes) {
        // return new BASE64Encoder().encode(bytes);
        return Base64.encodeBase64String(bytes);
    }

    private static void fillExportStreamCompress(InputStream inputStream, OutputStream outputStream) {
        try {
            try (PipedInputStream pipedInputStream = new PipedInputStream()) {
                try (PipedOutputStream pipedOutputStream = new PipedOutputStream()) {
                    pipedInputStream.connect(pipedOutputStream);

                    Runnable producer = new Runnable() {
                        @Override
                        public void run() {
                            produceCompressDataFromStream(inputStream, pipedOutputStream);
                        }
                    };
                    Runnable consumer = new Runnable() {
                        @Override
                        public void run() {
                            consumeCompressData(pipedInputStream, outputStream);
                        }
                    };

                    transferData(producer, consumer);
                }
            }
        } catch (IOException e) {
            // logger.error("Failed to produce data :", e);
        }
    }

    private static void fillExportStreamCompress(String text, OutputStream exportByteArrayOutputStream) {
        try {
            try (PipedInputStream pipedInputStream = new PipedInputStream()) {
                try (PipedOutputStream pipedOutputStream = new PipedOutputStream()) {
                    pipedInputStream.connect(pipedOutputStream);

                    Runnable producer = new Runnable() {
                        @Override
                        public void run() {
                            produceCompressDataFromText(text, pipedOutputStream);
                        }
                    };
                    Runnable consumer = new Runnable() {
                        @Override
                        public void run() {
                            consumeCompressData(pipedInputStream, exportByteArrayOutputStream);
                        }
                    };

                    transferData(producer, consumer);
                }
            }
        } catch (IOException e) {
            // logger.error("Failed to produce data :", e);
        }
    }

    private static void transferData(Runnable producer, Runnable consumer) {
        ExecutorService threadPool = Executors.newFixedThreadPool(N_THREADS);
        try {
            threadPool.submit(producer);
            threadPool.submit(consumer).get();
        } catch (InterruptedException e) {
            // logger.error("Task failed : ", e);
        } catch (ExecutionException e) {
            // logger.error("Task failed : ", e);
        } finally {
            threadPool.shutdown();
        }
    }

    private static void consumeCompressData(PipedInputStream pipedInputStream, OutputStream exportByteArrayOutputStream) {
        try (OutputStream out = new GZIPOutputStream(new BufferedOutputStream(exportByteArrayOutputStream))) {
            try {
                byte[] bytes = new byte[BYTES_BUFFER_SIZE];
                int len;
                while ((len = pipedInputStream.read(bytes)) > 0) {
                    out.write(bytes, 0, len);
                }
                out.flush();
            } catch (IOException e) {
                // logger.error("Failed to consume data to compress:", e);
            }
        } catch (IOException e) {
            // logger.error("Failed to consume data to compress:", e);
        }
    }

    private static void produceCompressDataFromStream(InputStream inputStream, PipedOutputStream pipedOutputStream) {
        try (BufferedInputStream in = new BufferedInputStream(inputStream)) {
            byte[] buffer = new byte[BYTES_BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > 0) {
                pipedOutputStream.write(buffer, 0, len);
            }
            pipedOutputStream.close();
        } catch (IOException e) {
            // logger.error("Failed to produce data to compress : ", e);
        }
    }

    private static void produceCompressDataFromText(String text, PipedOutputStream pipedOutputStream) {
        int start_String = 0;
        int chunk = text.length();
        if (text.length() > STRING_MAX_SIZE) {
            chunk = text.length() / STRING_MAX_SIZE;
        }
        try {
            while (start_String < text.length()) {
                int end = start_String + chunk;
                if (end > text.length()) {
                    end = text.length();
                }
                byte[] bytes = text.substring(start_String, end).getBytes(StandardCharsets.UTF_8);

                pipedOutputStream.write(bytes);
                start_String = end;
            }
            pipedOutputStream.close();
        } catch (IOException e) {
            // logger.error("Failed to produce data to compress : ", e);
        }
    }

    private static void fillExportStreamDecompress(String text, StringBuilder stringBuilder) {
        try {
            try (PipedInputStream pipedInputStream = new PipedInputStream()) {
                try (PipedOutputStream pipedOutputStream = new PipedOutputStream()) {
                    pipedInputStream.connect(pipedOutputStream);

                    Runnable producer = new Runnable() {
                        @Override
                        public void run() {
                            produceDecompressData(text, pipedOutputStream);
                        }
                    };
                    Runnable consumer = new Runnable() {
                        @Override
                        public void run() {
                            consumeDecompressData(pipedInputStream, stringBuilder);
                        }
                    };

                    transferData(producer, consumer);
                }
            }
        } catch (IOException e) {
            // logger.error("Failed to decompress : ", e);
        }
    }

    private static void consumeDataDecompressStream(PipedInputStream pipedInputStream, OutputStream outputStream) {
        try (GZIPInputStream chunkZipper = new GZIPInputStream(pipedInputStream);
             InputStream in = new BufferedInputStream(chunkZipper);) {

            byte[] buffer = new byte[BYTES_BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            pipedInputStream.close();
        } catch (IOException e) {
            // logger.error("Failed to decompress : ", e);
        }
    }

    private static void consumeDecompressData(PipedInputStream pipedInputStream, StringBuilder stringBuilder) {
        try (GZIPInputStream chunkZipper = new GZIPInputStream(pipedInputStream);
             InputStream in = new BufferedInputStream(chunkZipper);) {

            byte[] buffer = new byte[BYTES_BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > 0) {
                if (len < buffer.length) {
                    byte[] writtenBytes = new byte[len];
                    getBytes(buffer, 0, len, writtenBytes, 0);
                    stringBuilder.append(new String(writtenBytes, StandardCharsets.UTF_8));
                } else {
                    stringBuilder.append(new String(buffer, StandardCharsets.UTF_8));
                }
            }
            pipedInputStream.close();
        } catch (IOException e) {
            // logger.error("Failed to decompress : ", e);
        }
    }

    public static void getBytes(byte[] source, int srcBegin, int srcEnd, byte[] destination,
                                int dstBegin) {
        System.arraycopy(source, srcBegin, destination, dstBegin, srcEnd - srcBegin);
    }

    private static void produceDecompressData(String text, PipedOutputStream pipedOutputStream) {
        try {
            byte[] bytes = getStringFromDecode(text);
            pipedOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] getStringFromDecode(String text) throws IOException {
        return Base64.decodeBase64(text);
    }

    /**
     * Writes a string piece by piece to file
     *
     * @param text       to input
     * @param tempFileIn input
     * @throws IOException exception when writing
     */
    private static void writeChunkBytes(String text, File tempFileIn) throws IOException {
        try (FileOutputStream writer = new FileOutputStream(tempFileIn)) {
            int chunk = text.length();
            if (text.length() > STRING_MAX_SIZE) {
                chunk = text.length() / STRING_MAX_SIZE;
            }

            int startString = 0;
            while (startString < text.length()) {
                int endString = startString + chunk;
                if (endString > text.length()) {
                    endString = text.length();
                }
                byte[] bytes = text.substring(startString, endString).getBytes(StandardCharsets.UTF_8);
                writer.write(bytes);
                startString = endString;
            }
        }
    }

    /* --- Constructors --- */

    /**
     * Private default constructor
     */
    private ZipUtils() {
        // avoid instantiation
    }
}