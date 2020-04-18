package com.vidaloca.skibidi.event.access.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(Long reqId){
        super("Request with id: "+reqId+" not found");
    }
}
