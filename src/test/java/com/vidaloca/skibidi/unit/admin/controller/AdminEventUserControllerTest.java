package com.vidaloca.skibidi.unit.admin.controller;

import com.vidaloca.skibidi.admin.controller.AdminEventUserController;
import com.vidaloca.skibidi.admin.service.AdminEventUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminEventUserControllerTest {
    @Mock
    AdminEventUserService service;

    @InjectMocks
    AdminEventUserController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void deleteUserFromEvent() throws Exception {
        mockMvc.perform(delete("/admin/event/{eventId}/user", 1)
                .accept(MediaType.APPLICATION_JSON)
                .param("userToDeleteId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void findAllEventUsers() throws Exception {
        mockMvc.perform(get("/admin/event/{eventId}/users", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void grantTakeEventAdmin() throws Exception {
        mockMvc.perform(put("/admin/event/{eventId}/user/{userId}/eventAdmin", 1, 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}