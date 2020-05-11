package com.vidaloca.skibidi.integration.user.registration.controller;

import com.vidaloca.skibidi.BaseIT;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegistrationControllerIT extends BaseIT {

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
                .andExpect(status().isBadRequest())
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
        dto.setEmail("test10@o2.pl");
        dto.setPicUrl("picUrl");

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isBadRequest())
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
    void confirmRegistration() throws Exception {
        mockMvc.perform(get("/registrationConfirm")
                .param("token", "valid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Success")))
                .andDo(print());
    }

    @Test
    @Transactional
    void confirmRegistrationExpired() throws Exception {
        mockMvc.perform(get("/registrationConfirm")
                .param("token", "expired"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Token is not valid")))
                .andDo(print());
    }

    @Test
    @Transactional
    void confirmRegistrationInvalid() throws Exception {
        mockMvc.perform(get("/registrationConfirm")
                .param("token", "notExisting"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Token is not valid")))
                .andDo(print());
    }
}