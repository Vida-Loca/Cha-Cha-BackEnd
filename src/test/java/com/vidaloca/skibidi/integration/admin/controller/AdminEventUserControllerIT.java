package com.vidaloca.skibidi.integration.admin.controller;

import com.vidaloca.skibidi.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminEventUserControllerIT extends BaseIT {

    @Test
    @Transactional
    void deleteUserFromEvent() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(delete("/admin/event/{eventId}/user", 10)
                .header("Authorization", "Bearer " + token)
                .param("userToDeleteId", "14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Successfully removed user from event")))
                .andDo(print());
    }

    @Test
    @Transactional
    void findAllEventUsers() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(get("/admin/event/{eventId}/users", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    @Transactional
    void grantTakeEventAdmin() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(put("/admin/event/{eventId}/user/{userId}/eventAdmin", 10, 14)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true))
                .andDo(print());
    }

    @Test
    @Transactional
    void findAllEventAdmins() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(get("/admin/event/{eventId}/admins", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }
}