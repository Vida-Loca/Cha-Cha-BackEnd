package com.vidaloca.skibidi.event.exception.model;

public class UserIsNotInEventException extends RuntimeException {
    public UserIsNotInEventException(Long userId, Long eventId) {
        super("User with id: " + userId + " is not in event with id: " + eventId + " and cannot make this action.");
    }
}
