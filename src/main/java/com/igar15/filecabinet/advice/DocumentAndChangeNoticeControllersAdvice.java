package com.igar15.filecabinet.advice;

import com.igar15.filecabinet.controller.ChangeNoticeController;
import com.igar15.filecabinet.controller.DocumentController;
import com.igar15.filecabinet.util.StringUpperCaseEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice(assignableTypes = {DocumentController.class, ChangeNoticeController.class})
public class DocumentAndChangeNoticeControllersAdvice {

    @InitBinder
    public void initBinderDecimalNumberAndNameFields(WebDataBinder webDataBinder) {
        StringUpperCaseEditor stringUpperCaseEditor = new StringUpperCaseEditor();
        webDataBinder.registerCustomEditor(String.class, "decimalNumber", stringUpperCaseEditor);
        webDataBinder.registerCustomEditor(String.class, "name", stringUpperCaseEditor);
    }
}
