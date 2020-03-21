package com.vidaloca.skibidi.user.exception;


import com.vidaloca.skibidi.user.account.dto.PasswordDto;

public class PasswordsNotMatchesException extends Throwable{
    public PasswordsNotMatchesException(){
        super("Passwords not matches");
    }
}
