package com.igar15.filecabinet.util;

import java.beans.PropertyEditorSupport;

public class StringUpperCaseEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        return getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text.toUpperCase());
    }
}
