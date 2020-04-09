package com.vidaloca.skibidi.friendship.exception;

import com.vidaloca.skibidi.friendship.status.InvitationStatus;

public class InvitationExistsException extends RuntimeException{
    public InvitationExistsException(Long invitorId, Long invitedId, InvitationStatus invitationStatus){
        super("Invitatnion of users with ids: "+invitorId + " and " +invitedId + " already exists. AccessStatus : "
                + invitationStatus.getDescription());
    }
}
