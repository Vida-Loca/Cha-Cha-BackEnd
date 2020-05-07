package com.vidaloca.skibidi.user.account.controller;

import com.vidaloca.skibidi.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserAccountControllerIT extends BaseIT {

    @Test
    @Transactional
    void getCurrentUser() throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "testowy1",
                        "password"
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        mockMvc.perform(get("/user")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("test1")))
                .andExpect(jsonPath("$.surname", is("Test1")))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllUserEvents() throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "testowy1",
                        "password"
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        mockMvc.perform(get("/user/event")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @Transactional
    void isAdmin() {
    }

    @Test
    @Transactional
    void changePhoto() {
    }

    @Test
    @Transactional
    void changeNames() {
    }

    @Test
    @Transactional
    void resetPassword() {
    }

    @Test
    @Transactional
    void confirmResetPassword() {
    }

    @Test
    @Transactional
    void testResetPassword() {
    }
}