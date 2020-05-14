package com.vidaloca.skibidi.integration.event.access.controller;

import com.vidaloca.skibidi.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RequestControllerIT extends BaseIT {

    @Test
    @Transactional
    void getAllEventRequests() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/requests", 14)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllEventRequestsReturnsNull() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(get("/event/{eventId}/requests", 14)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist())
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllUserEventRequests() throws Exception {
        String token = authenticateUser("testowy3", "password");

        mockMvc.perform(get("/user/event_requests")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    @Transactional
    void sendRequest() throws Exception {
        String token = authenticateUser("testowy3", "password");

        mockMvc.perform(post("/event/{eventId}/send_request", 13)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andDo(print());
    }

    @Test
    @Transactional
    void sendRequestNotAllowed() throws Exception {
        String token = authenticateUser("testowy4", "password");

        mockMvc.perform(post("/event/{eventId}/send_request", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$").value("User with id: 16 can't request to that event"))
                .andDo(print());
    }

    @Test
    @Transactional
    void sendRequestActuallyIn() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(post("/event/{eventId}/send_request", 12)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("User: testowy2 is actually in that event."))
                .andDo(print());
    }

    @Test
    @Transactional
    void acceptRequest() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/event/request/{requestId}/accept", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessStatus").value("ACCEPTED"))
                .andDo(print());
    }

    @Test
    @Transactional
    void acceptRequestNotAdmin() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/event/request/{requestId}/accept", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$").value("User with id: 14 can't accept requests in that event"))
                .andDo(print());
    }

    @Test
    @Transactional
    void acceptRequestNotFound() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/event/request/{requestId}/accept", 99)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Request with id: 99 not found"))
                .andDo(print());
    }

    @Test
    @Transactional
    void acceptRequestNotProcessing() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/event/request/{requestId}/accept", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$").value("Request with id: 11 is not processing"))
                .andDo(print());
    }

    @Test
    @Transactional
    void rejectRequest() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/event/request/{requestId}/reject", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessStatus").value("REJECTED"))
                .andDo(print());
    }

    @Test
    @Transactional
    void rejectRequestNotAdmin() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/event/request/{requestId}/reject", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$").value("User with id: 14 can't reject requests in that event"))
                .andDo(print());
    }

    @Test
    @Transactional
    void rejectRequestNotProcessing() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/event/request/{requestId}/reject", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$").value("Request with id: 11 is not processing"))
                .andDo(print());
    }
}