package com.igar15.filecabinet.entity.enums;

public enum Form {
    ELECTRONIC("Electronic"), PAPER("Paper");

    private String name;

    private Form(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
