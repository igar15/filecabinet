package com.igar15.filecabinet.entity.enums;

public enum Status {
    ORIGINAL("Original"), DUPLICATE("Duplicate"), ACC_COPY("Acc. copy"), UNACC_COPY("Unacc. copy");

    private String name;

    private Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
