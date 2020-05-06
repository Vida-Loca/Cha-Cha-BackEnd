package com.vidaloca.skibidi.user.registration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.dto.UserRegistrationDto;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/data-test.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application.properties")
@AutoConfigureMockMvc
class RegistrationControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    void registerUserAccount() throws Exception {
        List<User> userListBefore = (List<User>) userRepository.findAll();
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setName("TestName");
        dto.setSurname("TestSurname");
        dto.setUsername("testUsername");
        dto.setPassword("password");
        dto.setMatchingPassword("password");
        dto.setEmail("testMail@mail.com");
        dto.setPicUrl("picUrl");

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk())
                .andDo(print());

        List<User> userListAfter = (List<User>) userRepository.findAll();
        assertNotEquals(userListBefore, userListAfter);
    }

    @Test
    @Transactional
    void registerUserAccountExistingUsername() throws Exception {
        List<User> userListBefore = (List<User>) userRepository.findAll();
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setName("TestName");
        dto.setSurname("TestSurname");
        dto.setUsername("testowy1");
        dto.setPassword("password");
        dto.setMatchingPassword("password");
        dto.setEmail("testMail@mail.com");
        dto.setPicUrl("picUrl");

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk())
                .andDo(print());

        List<User> userListAfter = (List<User>) userRepository.findAll();
        assertEquals(userListBefore, userListAfter);
    }

    @Test
    @Transactional
    void registerUserAccountExistingEmail() throws Exception {
        List<User> userListBefore = (List<User>) userRepository.findAll();
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setName("TestName");
        dto.setSurname("TestSurname");
        dto.setUsername("testUsername");
        dto.setPassword("password");
        dto.setMatchingPassword("password");
        dto.setEmail("testTest@o2.pl");
        dto.setPicUrl("picUrl");

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk())
                .andDo(print());

        List<User> userListAfter = (List<User>) userRepository.findAll();
        assertEquals(userListBefore, userListAfter);
    }

    @Test
    @Transactional
    void registerUserAccountInvalidData() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setName("");
        dto.setSurname("");
        dto.setUsername("");
        dto.setPassword("");
        dto.setMatchingPassword("");
        dto.setEmail("");
        dto.setPicUrl("");

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void confirmRegistration() {

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