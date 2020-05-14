package com.vidaloca.skibidi.integration.event.controller;

import com.vidaloca.skibidi.integration.BaseIT;
import com.vidaloca.skibidi.address.dto.AddressDto;
import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.type.EventType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void findAllEventUsersReturnsNull() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/users", 13)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist())
                .andDo(print());
    }

    @Test
    @Transactional
    void getEvents() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
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
    void getEventByIdNotExisting() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}", 99)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    void addNewEvent() throws Exception {
        LocalDateTime data = LocalDateTime.parse("2020-09-10T20:20");
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
        dto.setStartTime(data);
        dto.setAddress(addressDto);
        dto.setAdditionalInformation("AddInfo");
        dto.setEventType(EventType.PUBLIC);
        dto.setCurrency("PLN");
        dto.setOver(false);

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/event")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewTestEvent")))
                .andDo(print());
    }

    @Test
    @Transactional
    void addNewEventExistingAddress() throws Exception {
        LocalDateTime data = LocalDateTime.parse("2020-09-10T20:20");
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry("Country10");
        addressDto.setCity("City10");
        addressDto.setLatitude(10f);
        addressDto.setLongitude(10f);
        addressDto.setStreet("Street10");
        addressDto.setPostcode("10-240");
        addressDto.setNumber("10");

        EventDto dto = new EventDto();
        dto.setName("NewTestEvent");
        dto.setStartTime(data);
        dto.setAddress(addressDto);
        dto.setAdditionalInformation("AddInfo");
        dto.setEventType(EventType.PUBLIC);
        dto.setCurrency("PLN");
        dto.setOver(false);

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/event")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewTestEvent")))
                .andDo(print());
    }

    @Test
    @Transactional
    void updateEvent() throws Exception {
        LocalDateTime data = LocalDateTime.parse("2020-09-10T20:20");
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry("Country");
        addressDto.setCity("City");
        addressDto.setLatitude(87.3f);
        addressDto.setLongitude(132.3f);
        addressDto.setStreet("Street");
        addressDto.setPostcode("10-100");
        addressDto.setNumber("5B");

        EventDto dto = new EventDto();
        dto.setName("UpdatedEvent");
        dto.setStartTime(data);
        dto.setAddress(addressDto);
        dto.setAdditionalInformation("AddInfo");
        dto.setEventType(EventType.PUBLIC);
        dto.setCurrency("PLN");
        dto.setOver(false);

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/event/{eventId}", 10)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name", is("UpdatedEvent")))
                .andDo(print());
    }

    @Test
    @Transactional
    void updateEventNotAdmin() throws Exception {
        LocalDateTime data = LocalDateTime.parse("2020-09-10T20:20");
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry("Country");
        addressDto.setCity("City");
        addressDto.setLatitude(87.3f);
        addressDto.setLongitude(132.3f);
        addressDto.setStreet("Street");
        addressDto.setPostcode("10-100");
        addressDto.setNumber("5B");

        EventDto dto = new EventDto();
        dto.setName("UpdatedEvent");
        dto.setStartTime(data);
        dto.setAddress(addressDto);
        dto.setAdditionalInformation("AddInfo");
        dto.setEventType(EventType.PUBLIC);
        dto.setCurrency("PLN");
        dto.setOver(false);

        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/event/{eventId}", 10)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$", is("User with id: 14 is not admin of that event.")))
                .andDo(print());
    }

    @Test
    @Transactional
    void deleteById() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(delete("/event/{eventId}", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Event deleted successfully")))
                .andDo(print());
    }

    @Test
    @Transactional
    void deleteByIdNotAdmin() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(delete("/event/{eventId}", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$", is("User with id: 14 is not admin of that event.")))
                .andDo(print());
    }

    @Test
    @Transactional
    void leaveEvent() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(delete("/event/{eventId}/leave", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true))
                .andDo(print());
    }

    @Test
    @Transactional
    void leaveEventLastAdmin() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(delete("/event/{eventId}/leave", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$",
                        is("You are the last admin in this event. Before leaving give admin to another user.")))
                .andDo(print());
    }

    @Test
    @Transactional
    void deleteUserFromEvent() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(delete("/event/{eventId}/user", 10)
                .header("Authorization", "Bearer " + token)
                .param("userToDeleteId", "14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Successfully removed user from event")))
                .andDo(print());
    }

    @Test
    @Transactional
    void deleteUserFromEventNotAdmin() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(delete("/event/{eventId}/user", 10)
                .header("Authorization", "Bearer " + token)
                .param("userToDeleteId", "10"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @Transactional
    void grantAdminForUser() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/event/{eventId}/user/{userId}/grantAdmin", 10, 14)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Successfully granted admin to testowy2")))
                .andDo(print());
    }

    @Test
    @Transactional
    void grantAdminForUserNotAdmin() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/event/{eventId}/user/{userId}/grantAdmin", 10, 15)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @Transactional
    void isAdmin() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/isAdmin", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true))
                .andDo(print());
    }

    @Test
    @Transactional
    void findAllEventAdmins() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/admin", 12)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }
}