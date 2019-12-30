package com.vidaloca.skibidi.registration.utills;

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
