package com.vidaloca.skibidi.user.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {
    private String password;
    private String matchingPassword;
}
