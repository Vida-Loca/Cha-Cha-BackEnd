package com.vidaloca.skibidi.friendship.exception;

public class UserNotAllowedException extends RuntimeException {
    public UserNotAllowedException(Long userId, String text){
        super("User with id: " + userId + " is not allowed to " + text + "invitation");
    }
}
