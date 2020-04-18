package com.vidaloca.skibidi.event.access.exception;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(Long invitationId){
        super("Invitation with id: "+invitationId+" not found");
    }
}
