package com.vidaloca.skibidi.event.type;

public enum EventType {
    PUBLIC("Public"), //Public to see,anyone can add, not invite and request needed
    NORMAL("Normal"), //Public to see, anyone can invite and request, anyone can accept request, and see invitation
    PRIVATE("Private"), //Only for Friends to see, anyone can request, only admin can accept request and send invitation
    SECRET("Secret"); //Not visible for all, only admin can accept request and invite

    private String description;

    EventType(String description){
        this.description = description;
    }

    public boolean isPermissionNeeded(){
        return this != PUBLIC;
    }
    public boolean canUserInvite(){
        return this == NORMAL || this == PUBLIC;
    }
    public boolean canUserSendRequest(){
        return this !=SECRET && this !=PUBLIC;
    }
    public boolean canUserAcceptRequest(){
        return this == NORMAL;
    }
    public boolean isVisible() { return this != SECRET;}
}
