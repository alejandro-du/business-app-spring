package com.example.webapp.security;

import com.sun.xml.internal.stream.writers.UTF8OutputStreamWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class CustomServletResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream masterOutputStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream branchOutputStream = new ByteArrayOutputStream();

    private final UTF8OutputStreamWriter master = new UTF8OutputStreamWriter(masterOutputStream);
    private final UTF8OutputStreamWriter branch = new UTF8OutputStreamWriter(branchOutputStream);

    private final PrintWriter printWriter = new PrintWriter(master);

    public CustomServletResponseWrapper(HttpServletResponse response) throws UnsupportedEncodingException {
        super(response);
    }

    @Override
    public PrintWriter getWriter() {
        return printWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new CustomServletOutputStream(master, branch);
    }

    public String getMasterOutput() {
        return masterOutputStream.toString();
    }

    public String getBranchOutput() throws IOException {
        branch.flush();
        return branchOutputStream.toString();
    }

}
