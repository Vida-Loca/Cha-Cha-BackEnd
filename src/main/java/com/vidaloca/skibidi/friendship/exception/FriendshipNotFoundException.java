package com.vidaloca.skibidi.friendship.exception;

public class FriendshipNotFoundException extends RuntimeException {
    public FriendshipNotFoundException(Long friendshipId) {
        super("Friendship with id: " + friendshipId + " not found");
    }
    public FriendshipNotFoundException(Long userId, Long userId2){
        super("Friendship of users with ids: " + userId + " and " + userId2 + " not found");
    }
}
