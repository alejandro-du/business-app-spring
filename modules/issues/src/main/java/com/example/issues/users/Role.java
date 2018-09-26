package com.example.issues.users;

import org.apache.commons.lang3.StringUtils;

public enum Role {

    ADMIN, DEVELOPER, USER;

    @Override
    public String toString() {
        return StringUtils.capitalize(super.toString().toLowerCase());
    }

}
