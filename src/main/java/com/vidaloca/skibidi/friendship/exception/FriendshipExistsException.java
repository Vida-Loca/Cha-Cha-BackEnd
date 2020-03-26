package com.vidaloca.skibidi.friendship.exception;

import com.vidaloca.skibidi.friendship.model.Friendship;
import com.vidaloca.skibidi.friendship.status.InvitationStatus;

public class FriendshipExistsException extends RuntimeException{
    public FriendshipExistsException(Long invitorId, Long invitedId, InvitationStatus invitationStatus){
        super("Friendship of users with ids: "+invitorId + "and " +invitedId + "already exists. Status of friendship"
                + invitationStatus.getDescription());
    }
}
