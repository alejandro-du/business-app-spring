package com.example.webapp.security;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.Writer;

public class CustomServletOutputStream extends ServletOutputStream {

    private final Writer master;
    private final Writer branch;

    public CustomServletOutputStream(Writer master, Writer branch) {
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
