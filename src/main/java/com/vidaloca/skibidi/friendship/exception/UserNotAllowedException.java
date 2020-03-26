package com.vidaloca.skibidi.friendship.exception;

import com.vidaloca.skibidi.friendship.status.InvitationStatus;
import com.vidaloca.skibidi.user.model.User;

public class UserNotAllowedException extends RuntimeException {
    public UserNotAllowedException(Long userId, String text){
        super("User with id: " + userId + " is not allowed to " + text + "invitation");
    }
}
