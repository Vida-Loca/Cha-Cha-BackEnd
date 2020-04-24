package com.vidaloca.skibidi.friendship.controller;

import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import com.vidaloca.skibidi.friendship.service.FriendshipService;
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
class FriendshipControllerTest {
    @Mock
    FriendshipService service;
    @Mock
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    FriendshipController controller;

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
    void getAllUsersContains() throws Exception {
        mockMvc.perform(get("/users_contains")
                .contentType(MediaType.APPLICATION_JSON)
                .param("regex", "l"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllCurrentUserFriends() throws Exception {
        mockMvc.perform(get("/user/friends")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllUserFriends() throws Exception {
        mockMvc.perform(get("/user/{userId}/friends", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllUserFriendsEvents() throws Exception {
        mockMvc.perform(get("/user/friends/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllInvitations() throws Exception {
        mockMvc.perform(get("/user/invitations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void inviteUserToFriends() throws Exception {
        mockMvc.perform(post("/user/invite")
                .contentType(MediaType.APPLICATION_JSON)
                .param("invitedId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void cancelInvitation() throws Exception {
        mockMvc.perform(put("/user/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .param("invitationId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void acceptInvitation() throws Exception {
        mockMvc.perform(put("/user/accept")
                .contentType(MediaType.APPLICATION_JSON)
                .param("invitationId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void rejectInvitation() throws Exception {
        mockMvc.perform(put("/user/reject")
                .contentType(MediaType.APPLICATION_JSON)
                .param("invitationId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void removeFriend() throws Exception {
        mockMvc.perform(put("/user/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userToRemoveId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void blockUser() throws Exception {
        mockMvc.perform(put("/user/block")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userToBlockId", "1"))
                .andExpect(status().isOk());
    }
}