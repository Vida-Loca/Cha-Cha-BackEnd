package com.vidaloca.skibidi.event.exception.model;

public class LastAdminException extends RuntimeException{
    public LastAdminException(){
        super("You are last admin in that event. Before leave you must grant admin to another one");
    }
}
