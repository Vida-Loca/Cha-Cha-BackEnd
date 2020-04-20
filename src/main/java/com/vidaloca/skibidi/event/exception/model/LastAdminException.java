package com.vidaloca.skibidi.event.exception.model;

public class LastAdminException extends RuntimeException{
    public LastAdminException(){
        super("you are the last admin in this event. Before leaving give admin to another user");
    }
}
