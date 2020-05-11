package com.vidaloca.skibidi.user.registration.exception;

public class UsernameExistsException extends RuntimeException{
        public UsernameExistsException( String username) {
            super("Account with username: " + username + " already exists");
        }
}
