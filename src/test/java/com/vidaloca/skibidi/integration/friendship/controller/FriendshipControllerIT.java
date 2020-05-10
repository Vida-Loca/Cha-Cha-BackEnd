package com.vidaloca.skibidi.integration.friendship.controller;

import com.vidaloca.skibidi.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FriendshipControllerIT extends BaseIT {

    @Test
    @Transactional
    void getAllUsersContains() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/users_contains")
                .header("Authorization", "Bearer " + token)
                .param("regex", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllCurrentUserFriends() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/user/friends")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllUserFriends() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/user/{userId}/friends", 14)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllUserFriendsEvents() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/user/friends/events")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllInvitations() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/user/invitations")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    @Transactional
    void inviteUserToFriends() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/user/invite")
                .header("Authorization", "Bearer " + token)
                .param("invitedId", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invitor.name", is("Name10")))
                .andExpect(jsonPath("$.invited.name", is("Name15")))
                .andDo(print());
    }

    @Test
    @Transactional
    void inviteUserToFriends_UserAlreadyInFriends() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/user/invite")
                .header("Authorization", "Bearer " + token)
                .param("invitedId", "14"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void inviteUserToFriends_InvitationExistProcessing() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/user/invite")
                .header("Authorization", "Bearer " + token)
                .param("invitedId", "12"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void inviteUserToFriends_InvitationExistCancelled() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/user/invite")
                .header("Authorization", "Bearer " + token)
                .param("invitedId", "16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invitationStatus", is("PROCESSING")))
                .andDo(print());
    }

    @Test
    @Transactional
    void cancelInvitation() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/cancel")
                .header("Authorization", "Bearer " + token)
                .param("invitationId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invitationStatus", is("CANCELLED")))
                .andDo(print());
    }

    @Test
    @Transactional
    void cancelInvitationUserNotAllowed() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/cancel")
                .header("Authorization", "Bearer " + token)
                .param("invitationId", "11"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void acceptInvitation() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/accept")
                .header("Authorization", "Bearer " + token)
                .param("invitationId", "11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationStatus", is("FRIENDS")))
                .andDo(print());
    }

    @Test
    @Transactional
    void acceptInvitationNotFound() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/accept")
                .header("Authorization", "Bearer " + token)
                .param("invitationId", "99"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    void acceptInvitationNotAllowed() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/accept")
                .header("Authorization", "Bearer " + token)
                .param("invitationId", "10"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void rejectInvitation() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/reject")
                .header("Authorization", "Bearer " + token)
                .param("invitationId", "11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invitationStatus", is("REJECTED")))
                .andDo(print());
    }

    @Test
    @Transactional
    void rejectInvitationNotFound() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/reject")
                .header("Authorization", "Bearer " + token)
                .param("invitationId", "99"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    void rejectInvitationNotAllowed() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/reject")
                .header("Authorization", "Bearer " + token)
                .param("invitationId", "10"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void removeFriend() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/remove")
                .header("Authorization", "Bearer " + token)
                .param("userToRemoveId", "14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationStatus", is("REMOVED")))
                .andDo(print());
    }

    @Test
    @Transactional
    void removeFriendAndInvitation() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/user/remove")
                .header("Authorization", "Bearer " + token)
                .param("userToRemoveId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationStatus", is("REMOVED")))
                .andDo(print());
    }

    @Test
    @Transactional
    void removeFriendNotAllowed() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/user/remove")
                .header("Authorization", "Bearer " + token)
                .param("userToRemoveId", "16"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void removeFriend_RelationNotExist() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/remove")
                .header("Authorization", "Bearer " + token)
                .param("userToRemoveId", "16"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    void blockUser() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/user/block")
                .header("Authorization", "Bearer " + token)
                .param("userToBlockId", "14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationStatus", is("BLOCKED")))
                .andDo(print());
    }

    @Test
    @Transactional
    void blockUserNotAllowed() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/user/block")
                .header("Authorization", "Bearer " + token)
                .param("userToBlockId", "16"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}