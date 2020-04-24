package com.vidaloca.skibidi.user.registration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.user.registration.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    UserService userService;
    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    RegistrationController controller;

    MockMvc mockMvc;
    HttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void name() {
    }

    //    @Test
//    void registerUserAccount() throws Exception {
//        UserRegistrationDto dto = new UserRegistrationDto();
//        dto.setName("name");
//        dto.setSurname("surname");
//        dto.setEmail("mail");
//        dto.setPassword("pass");
//        dto.setMatchingPassword("pass");
//        dto.setPicUrl("url");
//        dto.setUsername("username");
//
//        mockMvc.perform(post("/registration")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJson(dto)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void confirmRegistration() throws Exception {
//        mockMvc.perform(get("/registrationConfirm")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("token", "token"))
//                .andExpect(status().isOk());
//    }

    private String asJson(Object o) throws JsonProcessingException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);
        String jsonString = mapper.writeValueAsString(o);
        mapper.setDateFormat(df);
        return jsonString;
    }
}