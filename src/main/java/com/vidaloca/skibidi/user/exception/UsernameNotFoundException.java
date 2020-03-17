package com.vidaloca.skibidi.user.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String username) {
        super("User with username: " + username + " is not found");
    }
}