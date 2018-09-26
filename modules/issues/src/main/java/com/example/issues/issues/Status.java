package com.example.issues.issues;

import org.springframework.util.StringUtils;

public enum Status {

    OPEN, CLOSED;

    @Override
    public String toString() {
        return StringUtils.capitalize(super.toString().toLowerCase());
    }

}
