package com.vidaloca.skibidi.user.exception;

public class TokenInvalidException extends RuntimeException{
    public TokenInvalidException(String message){
        super("Token "+message);
    }
}
