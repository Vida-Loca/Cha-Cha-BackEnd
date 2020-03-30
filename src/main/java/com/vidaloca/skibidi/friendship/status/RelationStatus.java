package com.vidaloca.skibidi.friendship.status;

public enum  RelationStatus {
    FRIENDS("Friends"),
    REMOVED("Removed"),
    BLOCKED("Blocked");

    private String description;

    RelationStatus(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
