package com.example.webapp.security;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Collectors;

public class CustomServletRequestWrapper extends HttpServletRequestWrapper {

    private String body;
    private final HttpServletRequest request;

    public CustomServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = request.getReader().lines().collect(Collectors.joining());
        this.request = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CustomServletInputStream(request.getInputStream());
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new StringReader(body));
    }

    public String getBody() {
        return body;
    }

}
