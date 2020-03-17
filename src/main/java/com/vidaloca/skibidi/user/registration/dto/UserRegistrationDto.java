package com.vidaloca.skibidi.user.registration.dto;

import com.vidaloca.skibidi.user.registration.validation.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UserRegistrationDto {
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
    @ValidEmail
    private String email;

    private String picUrl;

    private LocalDateTime joined;

    public UserRegistrationDto(){}

    public UserRegistrationDto(@NotNull @NotEmpty String username, @NotNull @NotEmpty String name, @NotNull @NotEmpty String surname,
                               @NotNull @NotEmpty String password, String matchingPassword, @NotNull @NotEmpty String email) {
        this.username = username;
        this.name= name;
        this.surname = surname;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.email = email;
    }
    public UserRegistrationDto(@NotNull @NotEmpty String username, @NotNull @NotEmpty String name, @NotNull @NotEmpty String surname,
                               @NotNull @NotEmpty String password, String matchingPassword, @NotNull @NotEmpty String email, String picUrl) {
        this.username = username;
        this.name= name;
        this.surname = surname;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.email = email;
        this.picUrl = picUrl;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public LocalDateTime getJoined() {
        return joined;
    }

    public void setJoined(LocalDateTime joined) {
        this.joined = joined;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", matchingPassword='" + matchingPassword + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}