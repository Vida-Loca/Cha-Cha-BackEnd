package com.vidaloca.skibidi.friendship.status;

public enum InvitationStatus {
    ACCEPTED("Accepted"),
    PROCESSING("Processing"),
    REJECTED("Rejected"),
    CANCELLED("Canceled");

    private String description;

    InvitationStatus(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

}
