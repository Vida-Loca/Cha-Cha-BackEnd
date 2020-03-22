package com.vidaloca.skibidi.user.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String email){
        super("User with following email :" + email + " not found");
    }
}
