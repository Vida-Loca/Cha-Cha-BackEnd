package com.vidaloca.skibidi.event.type;

public enum EventType {
    PUBLIC("Public"),
    NORMAL("Normal"),
    PRIVATE("Private"),
    SECRET("Secret");

    private String description;

    EventType(String description){
        this.description = description;
    }

    private boolean isPermissionNeeded(){
        return this != PUBLIC;
    }
    private boolean canUserInvite(){
        return this != SECRET && this !=PRIVATE;
    }
    private boolean canUserSendRequest(){
        return this !=SECRET && this !=PUBLIC;
    }
}
