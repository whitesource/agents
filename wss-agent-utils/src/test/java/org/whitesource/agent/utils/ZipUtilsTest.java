package org.whitesource.agent.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ZipUtilsTest {

    @Test
    public void shouldCompressAndDecompress() throws IOException {
        String input = "1234567890qwertyuiop";

        String output = ZipUtils.compressString(input);
        Assert.assertEquals(output, ZipUtils.compress(input));

        String input2 = ZipUtils.decompressString(output);
        Assert.assertEquals(input2, ZipUtils.decompress(output));

        Assert.assertEquals(input, input2);
    }
}
