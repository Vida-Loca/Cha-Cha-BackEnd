package com.vidaloca.skibidi.event.access.exception.handler;

import com.vidaloca.skibidi.event.access.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccessExceptionHandler {

    @ExceptionHandler(InvitationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getInvitationNotFoundException(InvitationNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(RequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getRequestNotFoundException(RequestNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserCantInviteToEventException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String getUserCantInviteToEventException(UserCantInviteToEventException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserCantRequestToEventException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String getUserCantRequestToEventException(UserCantRequestToEventException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(UserCantAcceptRequestException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String getUserCantAcceptRequestException(UserCantAcceptRequestException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(IsNotProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String getIsNotProcessingException(IsNotProcessingException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserCantRejectRequestException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String getUserCantRejectRequestException(UserCantRejectRequestException ex) {
        return ex.getMessage();
    }

}

