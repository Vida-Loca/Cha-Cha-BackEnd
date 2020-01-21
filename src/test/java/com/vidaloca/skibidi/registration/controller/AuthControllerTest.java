package com.vidaloca.skibidi.registration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.exceptions.EmailExistsException;
import com.vidaloca.skibidi.exceptions.UsernameExistsException;
import com.vidaloca.skibidi.model.Role;
import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.model.VerificationToken;
import com.vidaloca.skibidi.registration.dto.LoginDto;
import com.vidaloca.skibidi.registration.dto.UserDto;
import com.vidaloca.skibidi.registration.service.MapValidationErrorService;
import com.vidaloca.skibidi.registration.service.UserService;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import com.vidaloca.skibidi.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private MapValidationErrorService mapValidationErrorService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService service;
    @Mock
    private ApplicationEventPublisher eventPublisher;
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

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();
    }

    //----------------------------authenticateUser
    @Test
    void authenticateUser() {
        LoginDto loginDto = new LoginDto("testowy", "test");

        BindingResult bindingResult = createMock(BindingResult.class);

        when(mapValidationErrorService.MapValidationService(bindingResult)).thenReturn(null);

        ResponseEntity<?> responseEntity = authController.authenticateUser(loginDto, bindingResult);
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

    //----------------------------registerUserAccount

    @Test
    void registerUserAccount() throws EmailExistsException, UsernameExistsException {
        UserDto userDto = new UserDto("unique", "user", "surname", "pasword", "pasword", "unique@o2.pl");
        User user = new User();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addPreferredLocale(Locale.ENGLISH);
        request.setServerName("SERVER_NAME");
        request.setServerPort(1010);
        request.setContextPath("PATH");

        when(service.registerNewUserAccount(userDto)).thenReturn(user);

        GenericResponse response = authController.registerUserAccount(userDto, request);

        assertEquals("success", response.getMessage());
    }

    //----------------------------confirmRegistration

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

        GenericResponse genericResponse = authController.confirmRegistration(request, token);
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

        GenericResponse genericResponse = authController.confirmRegistration(request, token);
        assertEquals("fail", genericResponse.getMessage());
    }

    //----------------------------resendRegistrationToken

    @Transactional
    @Test
    void resendRegistrationToken() throws Exception {
        User user = new User();
        VerificationToken token = new VerificationToken();
        token.setToken("TOKEN");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addPreferredLocale(Locale.ENGLISH);
        request.setServerName("SERVER_NAME");
        request.setServerPort(1010);
        request.setContextPath("PATH");

        when(service.generateNewVerificationToken("EXTOKEN")).thenReturn(token);
        when(service.getUser("TOKEN")).thenReturn(user);

        GenericResponse response = authController.resendRegistrationToken(request, "EXTOKEN");
    }

    //----------------------------authWithoutPassword

    @Test
    void authWithoutPassword() {
        User user = new User();
        Role role = new Role();
        role.setId(1);
        role.setName("USER");

        user.setRole(role);

        authController.authWithoutPassword(user);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}