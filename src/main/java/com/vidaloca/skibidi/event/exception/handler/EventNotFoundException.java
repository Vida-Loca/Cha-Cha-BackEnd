package com.vidaloca.skibidi.event.exception.handler;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id){
        super("Event with id: "+ id + " not found");
    }
}
