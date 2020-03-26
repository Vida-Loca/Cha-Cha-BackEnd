package com.vidaloca.skibidi.friendship.status;

public enum Status {
    ACCEPTED("Accepted"),
    PROCESSING("Processing"),
    REJECTED("Rejected"),
    CANCELLED("Canceled"),
    REMOVED("Removed");

    private String description;

    Status(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

}
