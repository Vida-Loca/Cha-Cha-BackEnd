package com.vidaloca.skibidi.user.login.service;

import com.vidaloca.skibidi.user.login.dto.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoginService {
    ResponseEntity<?> authenticateUser(LoginDto loginDto);
}
