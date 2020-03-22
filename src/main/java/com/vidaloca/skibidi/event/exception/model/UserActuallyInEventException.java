package com.vidaloca.skibidi.event.exception.model;

public class UserActuallyInEventException extends Throwable {
    public UserActuallyInEventException(String username){
        super("User: "+ username + " is actually in that event");
    }
}
