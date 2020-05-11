package com.vidaloca.skibidi.event.forum.exception.handler;
import com.vidaloca.skibidi.event.forum.exception.IsNotUsersPostException;
import com.vidaloca.skibidi.event.forum.exception.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getPostNotFoundException(PostNotFoundException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(IsNotUsersPostException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getIsNotUsersPostException(IsNotUsersPostException ex){
        return ex.getMessage();
    }
}
