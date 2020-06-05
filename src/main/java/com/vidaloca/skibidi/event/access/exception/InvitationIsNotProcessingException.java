package com.vidaloca.skibidi.event.access.exception;

public class InvitationIsNotProcessingException extends RuntimeException {
    public InvitationIsNotProcessingException(Long id){
        super("Invitation with id: " + id + " is not processing");
    }
}
