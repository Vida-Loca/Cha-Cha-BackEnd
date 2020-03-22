package com.vidaloca.skibidi.user.account.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordDto {
    private String password;
    private String matchingPassword;
}
