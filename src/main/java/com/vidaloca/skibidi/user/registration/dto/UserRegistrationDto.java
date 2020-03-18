package com.vidaloca.skibidi.user.registration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    private String name;
    private String surname;
    private String username;
    private String password;
    private String matchingPassword;
    private String email;
    private String picUrl;

    public UserRegistrationDto(String name, String surname, String username, String password, String matchingPassword, String email) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.email = email;
    }
}