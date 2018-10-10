package com.example.issues.issues;

public enum Status {

    OPEN("com.example.issues.statusOpen"), CLOSED("com.example.issues.statusClosed");

    final String nameProperty;

    Status(String nameProperty) {
        this.nameProperty = nameProperty;
    }

    public String getNameProperty() {
        return nameProperty;
    }

}
