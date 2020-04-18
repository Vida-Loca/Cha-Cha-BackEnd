package com.vidaloca.skibidi.event.access.exception;

public class UserCantRequestToEventException extends RuntimeException {
    public UserCantRequestToEventException(Long userId){
        super("User with id: "+ userId+" can't request to that event");
    }
}
