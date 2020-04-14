package com.vidaloca.skibidi.user.login.service;

import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import com.vidaloca.skibidi.user.login.dto.LoginDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginServiceImplTest {

    @Mock
    JwtTokenProvider tokenProvider;
    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        loginService = new LoginServiceImpl(tokenProvider, authenticationManager);
    }

    @Test
    void authenticateUser() {
        //given
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("username");
        loginDto.setPassword("password");
        String token = "TOKEN";
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(any(Authentication.class))).thenReturn(token);

        //when
        ResponseEntity<?> response = loginService.authenticateUser(loginDto);

        //then
        assertNotNull(response);
        assertEquals("<200 OK OK,JwtLoginSucessResponse(success=true, token=Bearer TOKEN),[]>", response.toString());
        assertEquals(200 , response.getStatusCodeValue());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, times(1)).generateToken(any(Authentication.class));
    }
}