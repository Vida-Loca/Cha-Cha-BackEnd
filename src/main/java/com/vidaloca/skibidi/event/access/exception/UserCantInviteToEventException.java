package com.vidaloca.skibidi.event.access.exception;

public class UserCantInviteToEventException extends RuntimeException {
    public UserCantInviteToEventException(Long userId){
        super("User with id: "+ userId+" is not allowed to invite users to that event");
    }
}
