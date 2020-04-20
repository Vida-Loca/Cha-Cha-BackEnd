package com.vidaloca.skibidi.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.address.dto.AddressDto;
import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.event.type.EventType;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import com.vidaloca.skibidi.user.model.User;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {
    @Mock
    EventService eventService;
    @Mock
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    JwtTokenProvider jwtTokenProvider;

    CurrentUser currentUser;

    @InjectMocks
    EventController controller;

    MockMvc mockMvc;
    HttpServletRequest request;

    @BeforeEach
    void setUp() {
        currentUser = new CurrentUser(jwtAuthenticationFilter, jwtTokenProvider);
        request = mock(HttpServletRequest.class);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void findAllEventUsers() throws Exception {
        List<User> users = new ArrayList<>();

        given(controller.findAllEventUsers(1L, request)).willReturn(users);

        mockMvc.perform(get("/event/1/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getEvents() throws Exception {
        List<Event> eventList = new ArrayList<>();
        Event event = new Event();
        event.setName("NAME");
        eventList.add(event);
        given(eventService.findAllEvents()).willReturn(eventList);

        mockMvc.perform(get("/event"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void getEventById() throws Exception {
        mockMvc.perform(get("/event/{eventId}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addNewEvent() throws Exception {
        AddressDto addressDto = new AddressDto("Country", "City",
                "Postcode", "Street", "Num");
        EventDto eventDto = new EventDto();
        eventDto.setName("NAME");
        eventDto.setAddress(addressDto);
        eventDto.setEventType(EventType.PUBLIC);
        eventDto.setAdditionalInformation("INFO");

        mockMvc.perform(post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(eventDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateEvent() throws Exception {
        AddressDto addressDto = new AddressDto("Country", "City",
                "Postcode", "Street", "Num");
        EventDto eventDto = new EventDto();
        eventDto.setName("NAME");
        eventDto.setAddress(addressDto);
        eventDto.setEventType(EventType.PUBLIC);
        eventDto.setAdditionalInformation("INFO");

        mockMvc.perform(put("/event/{eventId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(eventDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete("/event/{eventId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void leaveEvent() throws Exception {
        mockMvc.perform(delete("/event/{eventId}/leave", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserFromEvent() throws Exception {
        mockMvc.perform(delete("/event/{eventId}/user", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .param("userToDeleteId", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void grantAdminForUser() throws Exception {
        mockMvc.perform(put("/event/{eventId}/user/{userId}/grantAdmin", 1, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void isAdmin() throws Exception {
        mockMvc.perform(get("/event/{eventId}/isAdmin", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void findAllEventAdmins() throws Exception {
        mockMvc.perform(get("/event/{eventId}/admin", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private String asJson(Object o) throws JsonProcessingException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);
        String jsonString = mapper.writeValueAsString(o);
        mapper.setDateFormat(df);
        return jsonString;
    }

}