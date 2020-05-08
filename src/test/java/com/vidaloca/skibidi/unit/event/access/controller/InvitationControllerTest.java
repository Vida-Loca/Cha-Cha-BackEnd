package com.vidaloca.skibidi.unit.event.access.controller;

import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import com.vidaloca.skibidi.event.access.controller.InvitationController;
import com.vidaloca.skibidi.event.access.service.InvitationService;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
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

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InvitationControllerTest {

    @Mock
    InvitationService service;
    @Mock
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    InvitationController controller;

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
    void getAllInvitations() throws Exception {
        mockMvc.perform(get("/event/{eventId}/invitations", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllUserInvitations() throws Exception {
        mockMvc.perform(get("/user/event_invitations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void inviteUser() throws Exception {
        mockMvc.perform(post("/event/{eventId}/invite", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void acceptInvitation() throws Exception {
        mockMvc.perform(put("/event/invite/{invitationId}/accept", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void rejectInvitation() throws Exception {
        mockMvc.perform(put("/event/invite/{invitationId}/reject", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}