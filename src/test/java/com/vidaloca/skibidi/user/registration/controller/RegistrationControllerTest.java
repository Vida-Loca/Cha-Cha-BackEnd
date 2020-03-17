package com.vidaloca.skibidi.user.registration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.user.registration.exception.EmailExistsException;
import com.vidaloca.skibidi.user.registration.exception.UsernameExistsException;
import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.model.VerificationToken;
import com.vidaloca.skibidi.user.login.dto.LoginDto;
import com.vidaloca.skibidi.user.registration.dto.UserRegistrationDto;
import com.vidaloca.skibidi.user.login.service.MapValidationErrorService;
import com.vidaloca.skibidi.user.registration.service.UserServiceImpl;
import com.vidaloca.skibidi.user.registration.utills.GenericResponse;
import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegistrationControllerTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private MapValidationErrorService mapValidationErrorService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserServiceImpl service;
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
    private RegistrationController registrationController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(registrationController)
                .build();
    }

    //----------------------------authenticateUser
    @Test
    void authenticateUser() {
        LoginDto loginDto = new LoginDto("testowy", "test");

        BindingResult bindingResult = createMock(BindingResult.class);

        when(mapValidationErrorService.MapValidationService(bindingResult)).thenReturn(null);

        ResponseEntity<?> responseEntity = registrationController.authenticateUser(loginDto, bindingResult);
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
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("unique", "user", "surname", "pasword", "pasword", "unique@o2.pl");
        User user = new User();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addPreferredLocale(Locale.ENGLISH);
        request.setServerName("SERVER_NAME");
        request.setServerPort(1010);
        request.setContextPath("PATH");

        when(service.registerNewUserAccount(userRegistrationDto)).thenReturn(user);

        GenericResponse response = registrationController.registerUserAccount(userRegistrationDto, request);

        assertEquals("success", response.getMessage());
    }

    @Test
    void registerUserAccountExistingEmail() throws EmailExistsException, UsernameExistsException {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("unique", "user", "surname", "pasword", "pasword", "unique@o2.pl");
        User user = new User();
        UserRegistrationDto userRegistrationDto1 = new UserRegistrationDto("unique1", "user", "surname", "pasword", "pasword", "unique@o2.pl");
        User user1 = new User();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addPreferredLocale(Locale.ENGLISH);
        request.setServerName("SERVER_NAME");
        request.setServerPort(1010);
        request.setContextPath("PATH");

        when(service.registerNewUserAccount(userRegistrationDto)).thenReturn(user);
        when(service.registerNewUserAccount(userRegistrationDto1)).thenThrow(EmailExistsException.class);

        registrationController.registerUserAccount(userRegistrationDto, request);
        registrationController.registerUserAccount(userRegistrationDto1, request);
        expectedException.expect(EmailExistsException.class);
    }

    @Test
    void registerUserAccountExistingUsername() throws EmailExistsException, UsernameExistsException {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("unique", "user", "surname", "pasword", "pasword", "unique@o2.pl");
        User user = new User();
        UserRegistrationDto userRegistrationDto1 = new UserRegistrationDto("unique", "user", "surname", "pasword", "pasword", "unique1@o2.pl");
        User user1 = new User();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addPreferredLocale(Locale.ENGLISH);
        request.setServerName("SERVER_NAME");
        request.setServerPort(1010);
        request.setContextPath("PATH");

        when(service.registerNewUserAccount(userRegistrationDto)).thenReturn(user);
        when(service.registerNewUserAccount(userRegistrationDto1)).thenThrow(UsernameExistsException.class);

        registrationController.registerUserAccount(userRegistrationDto, request);
        registrationController.registerUserAccount(userRegistrationDto1, request);
        expectedException.expect(UsernameExistsException.class);
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

        GenericResponse genericResponse = registrationController.confirmRegistration(request, token);
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

        GenericResponse genericResponse = registrationController.confirmRegistration(request, token);
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

        GenericResponse response = registrationController.resendRegistrationToken(request, "EXTOKEN");
    }

    //----------------------------authWithoutPassword

    @Test
    void authWithoutPassword() {
        User user = new User();
        Role role = new Role();
        role.setId(1);
        role.setName("USER");

        user.setRole(role);

        registrationController.authWithoutPassword(user);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}