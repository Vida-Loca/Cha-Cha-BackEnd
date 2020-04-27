package com.vidaloca.skibidi.user.registration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.dto.UserRegistrationDto;
import com.vidaloca.skibidi.user.registration.exception.EmailExistsException;
import com.vidaloca.skibidi.user.registration.exception.UsernameExistsException;
import com.vidaloca.skibidi.user.registration.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    UserService userService;
    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    RegistrationController controller;

    MockMvc mockMvc;
    MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void registerUserAccount() throws Exception, EmailExistsException, UsernameExistsException {
        request.setServerName("server");
        request.setServerPort(1000);
        request.setContextPath("contextPath");

        User user = new User();

        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setName("name");
        dto.setSurname("surname");
        dto.setEmail("mail");
        dto.setPassword("pass");
        dto.setMatchingPassword("pass");
        dto.setPicUrl("url");
        dto.setUsername("username");

        given(userService.registerNewUserAccount(dto)).willReturn(user);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void registerUserAccountEmailExist() throws Exception, EmailExistsException, UsernameExistsException {
        request.setServerName("server");
        request.setServerPort(1000);
        request.setContextPath("contextPath");

        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setName("name");
        dto.setSurname("surname");
        dto.setEmail("mail");
        dto.setPassword("pass");
        dto.setMatchingPassword("pass");
        dto.setPicUrl("url");
        dto.setUsername("username");

        given(userService.registerNewUserAccount(dto)).willThrow(EmailExistsException.class);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void registerUserAccountUsernameExist() throws Exception, EmailExistsException, UsernameExistsException {
        request.setServerName("server");
        request.setServerPort(1000);
        request.setContextPath("contextPath");

        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setName("name");
        dto.setSurname("surname");
        dto.setEmail("mail");
        dto.setPassword("pass");
        dto.setMatchingPassword("pass");
        dto.setPicUrl("url");
        dto.setUsername("username");

        given(userService.registerNewUserAccount(dto)).willThrow(UsernameExistsException.class);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void confirmRegistration() throws Exception {
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");
        User user = new User();
        user.setRole(role);

        given(userService.validateVerificationToken(anyString())).willReturn("valid");
        given(userService.getUser(anyString())).willReturn(user);

        mockMvc.perform(get("/registrationConfirm")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", "token"))
                .andExpect(status().isOk());
    }

    @Test
    void confirmRegistrationWentWrong() throws Exception {

        given(userService.validateVerificationToken(anyString())).willReturn("expired");

        mockMvc.perform(get("/registrationConfirm")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", "token"))
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