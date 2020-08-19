package com.igar15.filecabinet.util;

import java.beans.PropertyEditorSupport;

public class StringUpperCaseEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        return (String) getValue();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text.toUpperCase());
    }
}
