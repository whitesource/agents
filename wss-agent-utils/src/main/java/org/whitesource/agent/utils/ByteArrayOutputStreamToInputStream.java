package org.whitesource.agent.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayOutputStreamToInputStream extends InputStream {
    private final ByteArrayOutputStream outputStream;
    private int currentPosition;

    public ByteArrayOutputStreamToInputStream(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
        this.currentPosition = 0;
    }

    @Override
    public int read() throws IOException {
        synchronized (outputStream) {
            byte[] buffer = outputStream.toByteArray();
            if (currentPosition < buffer.length) {
                return buffer[currentPosition++];
            } else {
                return -1; // Indicates end of stream
            }
        }
    }

    @Override
    public void close() throws IOException {
        // No need to close the underlying ByteArrayOutputStream
        // Closing it may have unintended side effects
    }
}
