package com.vidaloca.skibidi.friendship.exception;

import com.vidaloca.skibidi.friendship.status.Status;

public class FriendshipExistsException extends RuntimeException{
    public FriendshipExistsException(Long invitorId, Long invitedId, Status status){
        super("Friendship of users with ids: "+invitorId + "and " +invitedId + "already exists. Status of friendship"
                + status.getDescription());
    }
}
