package com.igar15.filecabinet.advice;

import com.igar15.filecabinet.util.StringUpperCaseEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class AllControllerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @InitBinder
    public void initBinderDecimalNumber(WebDataBinder webDataBinder) {
        StringUpperCaseEditor stringUpperCaseEditor = new StringUpperCaseEditor();
        webDataBinder.registerCustomEditor(String.class, "decimalNumber", stringUpperCaseEditor);
        webDataBinder.registerCustomEditor(String.class, "name", stringUpperCaseEditor);
    }
}
