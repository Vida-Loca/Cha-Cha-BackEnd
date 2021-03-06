package com.vidaloca.skibidi.user.exception.handler;

import com.vidaloca.skibidi.friendship.exception.InvitationExistsException;
import com.vidaloca.skibidi.friendship.exception.InvitationNotFoundException;
import com.vidaloca.skibidi.friendship.exception.UserNotAllowedException;
import com.vidaloca.skibidi.user.exception.EmailNotFoundException;
import com.vidaloca.skibidi.user.exception.PasswordsNotMatchesException;
import com.vidaloca.skibidi.user.exception.TokenInvalidException;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotAllowedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getUsernameNotFoundException(UserNotAllowedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getUserNotFoundException(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getEmailNotFoundException(EmailNotFoundException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(PasswordsNotMatchesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getPasswordNotMatchesException(PasswordsNotMatchesException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(TokenInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getTokenInvalidException(TokenInvalidException ex){
        return ex.getMessage();
    }

}
