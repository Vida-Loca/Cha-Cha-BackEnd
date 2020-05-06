package com.vidaloca.skibidi.event.exception.model;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event with id: " + id + " not found.");
    }
}
