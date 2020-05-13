package com.vidaloca.skibidi.integration.event.open.controller;

import com.vidaloca.skibidi.integration.BaseIT;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PublicEventControllerIT extends BaseIT {

    @Test
    @Transactional
    void joinEvent() throws Exception {
        String token = authenticateUser("testowy4", "password");

        mockMvc.perform(post("/event/{eventId}/join", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

        User user = userRepository.findByUsername("testowy4").orElse(null);
        Event event = eventRepository.findById(10L).orElse(null);
        EventUser result = eventUserRepository.findByUserAndEvent(user, event).orElse(null);

        assertNotNull(result);
    }

    @Test
    @Transactional
    void joinEvent_NotAllowed() throws Exception {
        String token = authenticateUser("testowy4", "password");

        mockMvc.perform(post("/event/{eventId}/join", 12)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andDo(print());

        User user = userRepository.findByUsername("testowy4").orElse(null);
        Event event = eventRepository.findById(12L).orElse(null);
        EventUser result = eventUserRepository.findByUserAndEvent(user, event).orElse(null);

        assertNull(result);
    }

    @Test
    @Transactional
    void getAllPublicEvents() throws Exception {
        String token = authenticateUser("testowy4", "password");

        mockMvc.perform(get("/event/public")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }
}