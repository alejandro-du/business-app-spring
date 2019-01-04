package com.example.webapp.security;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomServletInputStream extends ServletInputStream {

    private final InputStream sourceStream;

    public CustomServletInputStream(InputStream sourceStream) {
        this.sourceStream = sourceStream;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        return sourceStream.read();
    }

}
