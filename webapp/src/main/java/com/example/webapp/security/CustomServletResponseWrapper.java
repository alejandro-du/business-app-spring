package com.example.webapp.security;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class CustomServletResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream master = new ByteArrayOutputStream();
    private final ByteArrayOutputStream branch = new ByteArrayOutputStream();
    private final PrintWriter printWriter = new PrintWriter(master);

    public CustomServletResponseWrapper(HttpServletResponse response) {
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

    public String getMasterOutput() throws UnsupportedEncodingException {
        return master.toString("UTF8");
    }

    public String getBranchOutput() throws IOException {
        branch.flush();
        return branch.toString("UTF8");
    }

}
