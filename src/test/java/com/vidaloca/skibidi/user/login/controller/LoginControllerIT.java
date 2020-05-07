package com.vidaloca.skibidi.user.login.controller;

import com.vidaloca.skibidi.BaseIT;
import com.vidaloca.skibidi.user.login.dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerIT extends BaseIT {

    @Test
    @Transactional
    void authenticateUser() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testowy1");
        loginDto.setPassword("password");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(loginDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void authenticateUserWrongPassword() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testowy1");
        loginDto.setPassword("wrong");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(loginDto)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @Transactional
    void authenticateUserWrongUsername() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("wrong");
        loginDto.setPassword("password");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(loginDto)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}