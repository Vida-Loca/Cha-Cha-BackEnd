package com.vidaloca.skibidi.integration.admin.controller;

import com.vidaloca.skibidi.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminEventControllerIT extends BaseIT {

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(get("/admin/getAllEvents")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getEventById() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(get("/admin/event/{id}", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestEvent10")))
                .andDo(print());
    }

    @Test
    @Transactional
    void deleteById() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(delete("/admin/event/{id}", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Event deleted successfully")))
                .andDo(print());
    }
}