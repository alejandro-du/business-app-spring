package com.example.common.domain;

public enum Role {

    ADMIN("com.example.role.admin"), DEVELOPER("com.example.role.developer"), USER("com.example.role.user");

    final String nameProperty;

    Role(String nameProperty) {
        this.nameProperty = nameProperty;
    }

    public String getNameProperty() {
        return nameProperty;
    }

}
