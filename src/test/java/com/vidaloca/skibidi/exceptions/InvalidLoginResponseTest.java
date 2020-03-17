package com.vidaloca.skibidi.exceptions;

import com.vidaloca.skibidi.user.login.exception.InvalidLoginResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class InvalidLoginResponseTest {


    @Test
    void getUsername() {
        InvalidLoginResponse ilr = new InvalidLoginResponse();
        ilr.setUsername("username");
        ilr.setPassword("password");

        Assert.assertEquals("username", ilr.getUsername());
    }

    @Test
    void setUsername() {
        InvalidLoginResponse ilr = new InvalidLoginResponse();
        ilr.setUsername("username");
        ilr.setPassword("password");

        Assert.assertEquals("username", ilr.getUsername());
    }

    @Test
    void getPassword() {
        InvalidLoginResponse ilr = new InvalidLoginResponse();
        ilr.setUsername("username");
        ilr.setPassword("password");

        Assert.assertEquals("password", ilr.getPassword());
    }

    @Test
    void setPassword() {
        InvalidLoginResponse ilr = new InvalidLoginResponse();
        ilr.setUsername("username");
        ilr.setPassword("password");

        Assert.assertEquals("password", ilr.getPassword());
    }
}