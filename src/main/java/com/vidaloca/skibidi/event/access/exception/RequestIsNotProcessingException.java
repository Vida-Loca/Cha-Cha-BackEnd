package com.vidaloca.skibidi.event.access.exception;

public class RequestIsNotProcessingException extends RuntimeException {
    public RequestIsNotProcessingException(Long id){
        super("Request with id: " + id + " is not processing");
    }
}