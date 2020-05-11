package com.vidaloca.skibidi.user.registration.exception;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException(){
        super("Token is not valid");
    }
}
