package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.model.Role;
import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.registration.repository.RoleRepository;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import com.vidaloca.skibidi.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AdminControllerTest {
    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;
    @Mock
    private EventService eventService;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(adminController)
                .build();
    }

    @Test
    void getAllUsers() {
        List<User> userList = adminController.getAllUsers();
        assertNotNull(userList);
    }

    @Test
    void grantNotExistingUserAdmin() {
        GenericResponse genericResponse = adminController.grantUserAdmin(1L);
        assertEquals("UserNotExist", genericResponse.getMessage());
    }

    @Test
    void grantUserAdminNoRole() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        GenericResponse genericResponse = adminController.grantUserAdmin(1L);
        assertEquals("Unexpected Problem", genericResponse.getMessage());
    }

    @Transactional
    @Test
    void grantUserAdmin() {
        Role role = new Role();
        role.setId(2);
        role.setName("ADMIN");
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(roleRepository.findById(2)).thenReturn(java.util.Optional.of(role));

        GenericResponse genericResponse = adminController.grantUserAdmin(1L);
        assertEquals("successfully granted admin for user: 1", genericResponse.getMessage());
    }

    @Test
    void deleteNotExistingUser() {
        HttpServletRequest request = createMock(HttpServletRequest.class);

        GenericResponse response = adminController.deleteUser(request, 1L);

        assertEquals("User not exists", response.getMessage());
    }

    @Test
    void deleteUser() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        GenericResponse response = adminController.deleteUser(request, 1L);

        assertEquals("Successfully deleted user", response.getMessage());
    }
}