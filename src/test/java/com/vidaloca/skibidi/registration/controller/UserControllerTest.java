package com.vidaloca.skibidi.registration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.exceptions.EmailExistsException;
import com.vidaloca.skibidi.model.Role;
import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.registration.dto.LoginDto;
import com.vidaloca.skibidi.registration.dto.UserDto;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import com.vidaloca.skibidi.registration.service.MapValidationErrorService;
import com.vidaloca.skibidi.registration.service.UserService;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import com.vidaloca.skibidi.registration.utills.RegisterEvent;
import com.vidaloca.skibidi.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Mock
    private UserService service;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Qualifier("messageSource")
    @Mock
    private MessageSource messages;
    @Mock
    private MailSender mailSender;
    @Mock
    private Environment env;
    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private MapValidationErrorService mapValidationErrorService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }


    @Test
    void currentUserId() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

//        Long id = userController.currentUserId(request);
//
//        assertEquals(1L, id);
    }

    @Test
    void currentUserIdStatus() throws Exception {
        mockMvc.perform(
                get("/currentUserId")
        ).andExpect(status().isNotFound());
    }

    @Test
    void getCurrentUser() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

//        User user = userController.getCurrentUser(request);
    }

    @Test
    void getCurrentUserStatus() throws Exception {
        mockMvc.perform(
                get("/currentUser")
        ).andExpect(status().isNotFound());
    }

    @Test
    void authenticateUser() {
        LoginDto loginDto = new LoginDto("testowy", "test");

        BindingResult bindingResult = createMock(BindingResult.class);

        when(mapValidationErrorService.MapValidationService(bindingResult)).thenReturn(null);

        ResponseEntity<?> responseEntity = userController.authenticateUser(loginDto, bindingResult);
        assertNotNull(responseEntity);
    }

    @Test
    void authenticateUserStatus() throws Exception {
        LoginDto loginDto = new LoginDto("testowy", "test");

        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginDto))
        ).andExpect(status().isOk());
    }

//    @Test
//    void registerUserAccount() throws Exception {
//        UserDto userDto = new UserDto("raz", "raz", "raz", "raz", "raz", "raz@mail.com");
//
//        mockMvc.perform(
//                post("/user/registration")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }

    @Test
    void confirmRegistrationSuccess() {

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        User user = new User();
        Role role = new Role();
        role.setId(1);
        role.setName("USER");
        user.setRole(role);

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(service.validateVerificationToken(token)).thenReturn("valid");
        when(service.getUser(token)).thenReturn(user);

        GenericResponse genericResponse = userController.confirmRegistration(request, token);
        assertEquals("success", genericResponse.getMessage());
    }

    @Test
    void confirmRegistrationFail() {

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        User user = new User();
        Role role = new Role();
        role.setId(1);
        role.setName("USER");
        user.setRole(role);

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(service.validateVerificationToken(token)).thenReturn("not valid");
        when(service.getUser(token)).thenReturn(user);

        GenericResponse genericResponse = userController.confirmRegistration(request, token);
        assertEquals("fail", genericResponse.getMessage());
    }

//    @Test
//    void resendRegistrationToken() {
//        User user = new User();
//
//        HttpServletRequest request = createMock(HttpServletRequest.class);
//        String token = "TOKEN";
//        VerificationToken verificationToken = createMock(VerificationToken.class);
//
//        when(service.generateNewVerificationToken(token)).thenReturn(verificationToken);
//        when(service.getUser(verificationToken.getToken())).thenReturn(user);
//
//        GenericResponse genericResponse = userController.resendRegistrationToken(request, token);
//    }

    @Test
    void authWithoutPassword() {
        User user = new User();
        Role role = new Role();
        role.setId(1);
        role.setName("USER");

        user.setRole(role);

        userController.authWithoutPassword(user);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}