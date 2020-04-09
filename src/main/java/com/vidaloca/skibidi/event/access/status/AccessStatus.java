package com.vidaloca.skibidi.event.access.status;

public enum AccessStatus {
    ACCEPTED("Accepted"),
    PROCESSING("Processing"),
    REJECTED("Rejected");

    private String description;

    AccessStatus(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return description;
    }
}
