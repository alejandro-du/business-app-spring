package com.example.webapp.security;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

public class CustomServletOutputStream extends ServletOutputStream {

    private final OutputStream master;
    private final OutputStream branch;

    public CustomServletOutputStream(OutputStream master, OutputStream branch) {
        this.master = master;
        this.branch = branch;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(int b) throws IOException {
        master.write(b);
        branch.write(b);
    }

}
