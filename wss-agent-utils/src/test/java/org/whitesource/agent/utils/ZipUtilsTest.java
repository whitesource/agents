package org.whitesource.agent.utils;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.model.AgentProjectInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ZipUtilsTest {

    private static final String JAVA_TEMP_DIR = System.getProperty("java.io.tmpdir");

    @Ignore
    @Test
    public void shouldCompressAndDecompress() throws IOException {
        String input = "1234567890qwertyuiop";

        String output = ZipUtils.compressString(input);
        Assert.assertEquals(output, ZipUtils.compress(input));

        String input2 = ZipUtils.decompressString(output);
        Assert.assertEquals(input2, ZipUtils.decompress(output));

        Assert.assertEquals(input, input2);
    }

    @Ignore
    @Test
    public void shouldCompressAndDecompressFile() throws IOException {
        String currentDir = System.getProperty("user.dir").toString();
        File input = Paths.get(currentDir, "\\src\\test\\resources\\plain_request.txt").toFile();

        String originalInput = new String(Files.readAllBytes(input.toPath()));

        File tempFileZip = File.createTempFile("zip", "stringzip", new File(JAVA_TEMP_DIR));
        File tempFileUnzipZip = File.createTempFile("unzip", "unString", new File(JAVA_TEMP_DIR));

        try (FileInputStream fileInputStream = new FileInputStream(input);
             FileOutputStream outputStream = new FileOutputStream(tempFileZip);) {
            ZipUtils.compressString(fileInputStream, outputStream);
        }

        try (FileInputStream fileInputStream = new FileInputStream(tempFileZip);
             FileOutputStream outputStream = new FileOutputStream(tempFileUnzipZip);) {
            ZipUtils.decompressString(fileInputStream, outputStream);
        }

        String compressDecompressOutput = new String(Files.readAllBytes(input.toPath()));
        Assert.assertEquals(originalInput, compressDecompressOutput);
    }

    @Ignore
    @Test
    public void shouldDecompressChunks() {
        String currentDir = System.getProperty("user.dir").toString();
        File input = Paths.get(currentDir, "\\src\\test\\resources\\zipped_projects.txt").toFile();

        List<AgentProjectInfo> projects;
        try {
            String zippedProjects = String.join("", Files.readAllLines(input.toPath()));

            Path decompressedPath = ZipUtils.decompressChunks(zippedProjects);
            try (FileInputStream fileInputStream = new FileInputStream(decompressedPath.toString());
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
                projects = JsonUtils.readProjects(bufferedInputStream); //? why this works ?
            }

            String projectsUnzippedChunks = ZipUtils.decompressString(zippedProjects);
            String projectsUnzippedChunks2 = String.join("", Files.readAllLines(decompressedPath));
            Assert.assertEquals(projectsUnzippedChunks, projectsUnzippedChunks2);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertNull(e);
        }
    }
}
