package com.vidaloca.skibidi.user.login.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class JwtLoginSucessResponse {
    private boolean success;
    private String token;
}
