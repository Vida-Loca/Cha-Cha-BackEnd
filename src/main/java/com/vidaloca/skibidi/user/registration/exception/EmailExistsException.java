package com.vidaloca.skibidi.user.registration.exception;


@SuppressWarnings("serial")
public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String email) {
        super("Account with email: " + email + " already exists");    }

}