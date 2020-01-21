package com.vidaloca.skibidi.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.model.Event;
import com.vidaloca.skibidi.model.EventUser;
import com.vidaloca.skibidi.model.Role;
import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import com.vidaloca.skibidi.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private EventUserRepository eventUserRepository;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void getCurrentNullUser() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        User user = userController.getCurrentUser(request);
        assertNull(user);
    }

    @Test
    void getCurrentUser() {
        User u = new User();
        u.setId(1L);
        u.setUsername("username");
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(tokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(u));

        User user = userController.getCurrentUser(request);
        assertEquals("username", user.getUsername());
    }

    @Test
    void getCurrentUserStatus() throws Exception {
        mockMvc.perform(
                get("/user")
        ).andExpect(status().isOk());
    }

    @Test
    void getAllNullUserEvents() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        List<Event> result = userController.getAllUserEvents(request);

        assertEquals(0, result.size());
    }

    @Test
    void getAllUserEvents() {
        User u = new User();
        u.setId(1L);
        u.setUsername("username");
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";
        Event event = new Event();
        EventUser eventUser = new EventUser();
        eventUser.setEvent(event);
        eventUser.setUser(u);
        List<EventUser> eventUserList = new ArrayList<>();
        eventUserList.add(eventUser);

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(tokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(u));
        when(eventUserRepository.findAllByUser(u)).thenReturn(eventUserList);

        List<Event> result = userController.getAllUserEvents(request);
        assertEquals(1, result.size());
    }

    @Test
    void getAllUserEventsStatus() throws Exception{
        mockMvc.perform(
                get("/user/event")
        ).andExpect(status().isOk());
    }

    @Test
    void isAdminNull() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        GenericResponse response = userController.isAdmin(request);

        assertEquals("failed", response.getMessage());
    }

    @Test
    void isAdminNope() {
        Role role = new Role();
        role.setName("USER");
        User u = new User();
        u.setId(1L);
        u.setUsername("username");
        u.setRole(role);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(tokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(u));

        GenericResponse response = userController.isAdmin(request);

        assertEquals("false", response.getMessage());
    }

    @Test
    void isAdmin() {
        Role role = new Role();
        role.setName("ADMIN");
        User u = new User();
        u.setId(1L);
        u.setUsername("username");
        u.setRole(role);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(tokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(u));

        GenericResponse response = userController.isAdmin(request);

        assertEquals("true", response.getMessage());
    }

    @Test
    void isAdminStatus() throws Exception {
        mockMvc.perform(
                get("/user/isAdmin")
        ).andExpect(status().isOk());
    }

    @Test
    void changePhotoNull() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String url = "URL";
        GenericResponse response = userController.changePhoto(request, url);

        assertEquals("fail", response.getMessage());
    }

    @Test
    void changePhoto() {
        User u = new User();
        u.setId(1L);
        u.setUsername("username");
        String url = "URL";
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(tokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(u));

        GenericResponse response = userController.changePhoto(request, url);

        assertEquals("success", response.getMessage());
    }

    @Test
    void changePhotoStatus() throws Exception {
        String url = "URL";
        mockMvc.perform(
                put("/user/changePhoto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(url))
        ).andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}