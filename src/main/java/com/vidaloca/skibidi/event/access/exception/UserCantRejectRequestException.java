package com.vidaloca.skibidi.event.access.exception;

public class UserCantRejectRequestException extends RuntimeException {
    public UserCantRejectRequestException(Long userId) {
        super("User with id: " + userId + " can't reject requests in that event");
    }
}
