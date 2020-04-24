package com.vidaloca.skibidi.user.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import com.vidaloca.skibidi.user.account.dto.PasswordDto;
import com.vidaloca.skibidi.user.account.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserAccountControllerTest {

    @Mock
    UserAccountService service;
    @Mock
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    UserAccountController controller;

    CurrentUser currentUser;
    MockMvc mockMvc;
    HttpServletRequest request;

    @BeforeEach
    void setUp() {
        currentUser = new CurrentUser(jwtAuthenticationFilter, jwtTokenProvider);
        request = mock(HttpServletRequest.class);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getCurrentUser() throws Exception {
        mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUserEvents() throws Exception {
        mockMvc.perform(get("/user/event")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void isAdmin() throws Exception {
        mockMvc.perform(get("/user/isAdmin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void changePhoto() throws Exception {
        mockMvc.perform(put("/user/changePhoto")
                .contentType(MediaType.APPLICATION_JSON)
                .param("url", "url"))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword() throws Exception {
        mockMvc.perform(post("/user/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "mail"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void confirmResetPassword() throws Exception {
        mockMvc.perform(get("/user/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "1")
                .param("token", "token"))
                .andExpect(status().isOk());
    }

    @Test
    void testResetPassword() throws Exception {
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setPassword("pass");
        passwordDto.setPassword("pass");

        mockMvc.perform(put("/user/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(passwordDto)))
                .andExpect(status().isOk());
    }

    private String asJson(Object o) throws JsonProcessingException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);
        String jsonString = mapper.writeValueAsString(o);
        mapper.setDateFormat(df);
        return jsonString;
    }
}