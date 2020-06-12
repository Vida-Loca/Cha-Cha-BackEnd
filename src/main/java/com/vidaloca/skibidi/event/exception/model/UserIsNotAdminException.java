package com.vidaloca.skibidi.event.exception.model;

public class UserIsNotAdminException extends RuntimeException {
    public UserIsNotAdminException(Long id) {
        super("User with id: " + id + " is not admin of that event.");
    }
}
