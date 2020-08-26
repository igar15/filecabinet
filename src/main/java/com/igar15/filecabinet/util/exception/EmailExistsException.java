package com.igar15.filecabinet.util.exception;

public class EmailExistsException extends RuntimeException{

    public EmailExistsException(String message) {
        super(message);
    }
}
