package com.vidaloca.skibidi.event.exception.handler;

import com.vidaloca.skibidi.event.exception.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getEventNotFoundException(EventNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserIsNotInEventException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getUserIsNotInEventException(UserIsNotInEventException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(UserActuallyInEventException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getUserActuallyInEventException(UserActuallyInEventException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(UserIsNotAdminException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String getUserIsNotAdminException(UserIsNotAdminException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(LastAdminException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String getLastAdminException(LastAdminException ex){
        return ex.getMessage();
    }
}
