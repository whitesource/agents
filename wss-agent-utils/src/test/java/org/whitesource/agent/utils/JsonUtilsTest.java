package org.whitesource.agent.utils;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class JsonUtilsTest {

    @Ignore
    @Test
    public void shouldDeserialize (){
        String currentDir = System.getProperty("user.dir").toString();
        File input = Paths.get(currentDir, "\\src\\test\\resources\\plain_request.txt").toFile();
        try {
            try (FileInputStream fileInputStream = new FileInputStream(input)) {
                UpdateInventoryRequest updateRequest = JsonUtils.readUpdateInventoryRequest(fileInputStream);
                Assert.assertNotNull(updateRequest);
            }
        } catch (IOException e) {
            Assert.assertNull(e);
        }
    }
}
