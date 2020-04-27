package com.vidaloca.skibidi.event.access.exception;

public class UserCantAcceptRequestException extends RuntimeException {
    public UserCantAcceptRequestException(Long userId){
        super("User with id: "+ userId+" can't accept requests in that event");
    }
}
