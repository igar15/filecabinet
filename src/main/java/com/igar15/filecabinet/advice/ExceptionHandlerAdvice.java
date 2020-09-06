package com.igar15.filecabinet.advice;

import com.igar15.filecabinet.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

//    private static Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler
    public String handlException(Exception e, Model model) {
//        log.warn("Exception {} : {}", e.getClass().getSimpleName(), e.getMessage());
        if (e.getClass().equals(NotFoundException.class)) {
            model.addAttribute("notFoundMessage", e.getMessage());
        }
        else {
            model.addAttribute("exceptionMessage", e.getMessage());
        }
        return "exceptionPage";
    }

}

