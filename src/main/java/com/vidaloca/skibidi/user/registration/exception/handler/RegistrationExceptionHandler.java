package com.vidaloca.skibidi.user.registration.exception.handler;

import com.vidaloca.skibidi.user.exception.EmailNotFoundException;
import com.vidaloca.skibidi.user.registration.exception.TokenNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RegistrationExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getUsernameNotFoundException(UsernameNotFoundException ex){
        return ex.getMessage();
    }
    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getEmailNotFoundException(EmailNotFoundException ex){
        return ex.getMessage();
    }
    @ExceptionHandler(TokenNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getTokenNotValidException(TokenNotValidException ex){
        return ex.getMessage();
    }
}
