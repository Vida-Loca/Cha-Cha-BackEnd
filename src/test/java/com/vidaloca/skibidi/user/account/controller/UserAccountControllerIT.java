package com.vidaloca.skibidi.user.account.controller;

import com.vidaloca.skibidi.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserAccountControllerIT extends BaseIT {

    @Test
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
    void getAllUserEvents() {
    }

    @Test
    void isAdmin() {
    }

    @Test
    void changePhoto() {
    }

    @Test
    void changeNames() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void confirmResetPassword() {
    }

    @Test
    void testResetPassword() {
    }
}