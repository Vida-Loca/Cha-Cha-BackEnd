package com.vidaloca.skibidi.integration.event.access.controller;

import com.vidaloca.skibidi.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InvitationControllerIT extends BaseIT {

    @Test
    @Transactional
    void getAllInvitations() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/invitations", 14)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllInvitationsReturnsNull() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(get("/event/{eventId}/invitations", 14)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist())
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllUserInvitations() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(get("/user/event_invitations", 14)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @Transactional
    void inviteUser() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/event/{eventId}/invite", 10)
                .header("Authorization", "Bearer " + token)
                .param("userId", "16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andDo(print());
    }

    @Test
    @Transactional
    void inviteUserActuallyInEvent() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/event/{eventId}/invite", 10)
                .header("Authorization", "Bearer " + token)
                .param("userId", "14"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("User: testowy2 is actually in that event."))
                .andDo(print());
    }

    @Test
    @Transactional
    void inviteUserNotAllowed() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(post("/event/{eventId}/invite", 14)
                .header("Authorization", "Bearer " + token)
                .param("userId", "16"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$").value("User with id: 14 is not allowed to invite users to that event"))
                .andDo(print());
    }

    @Test
    @Transactional
    void acceptInvitation() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/event/invite/{invitationId}/accept", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.accessStatus").value("ACCEPTED"))
                .andDo(print());
    }

    @Test
    @Transactional
    void acceptInvitationNotFound() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/event/invite/{invitationId}/accept", 99)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    void acceptInvitationNotProcessing() throws Exception {
        String token = authenticateUser("testowy4", "password");

        mockMvc.perform(put("/event/invite/{invitationId}/accept", 12)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadGateway())
                .andDo(print());
    }

    @Test
    @Transactional
    void rejectInvitation() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/event/invite/{invitationId}/reject", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.accessStatus").value("REJECTED"))
                .andDo(print());
    }

    @Test
    @Transactional
    void rejectInvitationNotFound() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/event/invite/{invitationId}/reject", 99)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    void rejectInvitationNotProcessing() throws Exception {
        String token = authenticateUser("testowy4", "password");

        mockMvc.perform(put("/event/invite/{invitationId}/reject", 12)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadGateway())
                .andDo(print());
    }
}