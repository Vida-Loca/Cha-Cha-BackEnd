package com.vidaloca.skibidi.registration.dto;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserDto {
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String surname;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @NotNull
    @NotEmpty
    private String email;

    public UserDto(){}

    public UserDto(@NotNull @NotEmpty String username,@NotNull @NotEmpty String name, @NotNull @NotEmpty String surname,
                   @NotNull @NotEmpty String password, String matchingPassword, @NotNull @NotEmpty String email) {
        this.username = username;
        this.name= name;
        this.surname = name;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}