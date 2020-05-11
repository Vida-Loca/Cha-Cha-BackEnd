package com.vidaloca.skibidi.integration.event.controller;

import com.vidaloca.skibidi.BaseIT;
import com.vidaloca.skibidi.address.dto.AddressDto;
import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.type.EventType;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerIT extends BaseIT {

    @Test
    @Transactional
    void findAllEventUsers() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/users", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getEvents() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getEventById() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestEvent10")))
                .andDo(print());
    }

    @Test
    @Transactional
    void addNewEvent() throws Exception {
        String stringDate = "2020-10-15T20:21";
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry("Country");
        addressDto.setCity("City");
        addressDto.setLatitude(87.3f);
        addressDto.setLongitude(132.3f);
        addressDto.setStreet("Street");
        addressDto.setPostcode("10-100");
        addressDto.setNumber("5B");
        EventDto dto = new EventDto();
        dto.setName("NewTestEvent");
        dto.setStartTime(LocalDateTime.now());
        dto.setAddress(addressDto);
        dto.setAdditionalInformation("AddInfo");
        dto.setEventType(EventType.PUBLIC);
        dto.setCurrency("PLN");
        dto.setOver(false);

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestEvent10")))
                .andDo(print());
    }

    @Test
    @Transactional
    void updateEvent() {
    }

    @Test
    @Transactional
    void deleteById() {
    }

    @Test
    @Transactional
    void leaveEvent() {
    }

    @Test
    @Transactional
    void deleteUserFromEvent() {
    }

    @Test
    @Transactional
    void grantAdminForUser() {
    }

    @Test
    @Transactional
    void isAdmin() {
    }

    @Test
    @Transactional
    void findAllEventAdmins() {
    }
}