package com.vidaloca.skibidi.friendship.exception;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(Long friendshipId) {
        super("Friendship with id: " + friendshipId + " not found");
    }
}
