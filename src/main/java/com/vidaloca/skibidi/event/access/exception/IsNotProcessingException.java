package com.vidaloca.skibidi.event.access.exception;

import org.springframework.security.access.intercept.RunAsUserToken;

public class IsNotProcessingException extends RuntimeException {
    public IsNotProcessingException(String name, Long id){
        super(name+" with id: " + id + " is not processing");
    }
}
