package com.vidaloca.skibidi.friendship.exception;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(Long friendshipId) {
        super("Friendship with id: " + friendshipId + " not found");
    }
    public InvitationNotFoundException(Long userId, Long userId2){
        super("Friendship of users with ids: " + userId + " and " + userId2 + " not found");
    }
}
