package com.vidaloca.skibidi.user.exception.handler;

import com.vidaloca.skibidi.friendship.exception.InvitationExistsException;
import com.vidaloca.skibidi.friendship.exception.InvitationNotFoundException;
import com.vidaloca.skibidi.friendship.exception.UserNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotAllowedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getUserNotAllowedException(UserNotAllowedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvitationExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getInvitationExistsException(InvitationExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvitationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getInvitationNotFoundException(InvitationNotFoundException ex) {
        return ex.getMessage();
    }

}
