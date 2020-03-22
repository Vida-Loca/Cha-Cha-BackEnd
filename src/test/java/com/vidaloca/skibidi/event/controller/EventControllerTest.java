package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest {

    @Mock
    EventService eventService;

    EventController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        controller = new EventController(eventService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getEvents() {
    }

    @Test
    void getEventById() throws Exception {
        //given
        Event event = new Event();
        event.setId(1L);

        when(eventService.findById(anyLong())).thenReturn(event);

        //when
        mockMvc.perform(get("/event/{eventId}"))
                .andExpect(status().isOk());

        //then
        verify(eventService, times(1)).findById(anyLong());
    }

    @Test
    void addNewEvent() {
    }

    @Test
    void updateEvent() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void addUserToEvent() {
    }

    @Test
    void deleteUserFromEvent() {
    }

    @Test
    void grantAdminForUser() {
    }

    @Test
    void isAdmin() {
    }
}