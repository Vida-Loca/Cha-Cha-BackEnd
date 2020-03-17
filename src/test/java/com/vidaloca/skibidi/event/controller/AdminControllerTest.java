package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.RoleRepository;
import com.vidaloca.skibidi.user.repository.UserRepository;
import com.vidaloca.skibidi.user.registration.utills.GenericResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    @Mock
    private UserRepository userRepository;
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
    void getAllStatus() throws Exception {
        mockMvc.perform(
                get("/admin/getAllUsers")
        ).andExpect(status().isOk());
    }

    @Test
    void getAllVerifyTimes() {
        adminController.getAllUsers();
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
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
    void grantUserAdminStatus() throws Exception {
        mockMvc.perform(
                put("/admin/grantUserAdmin/{id}", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void grantUserAdminVerifyTimes() {
        User user = new User();
        Role role = new Role();
        role.setId(2);
        role.setName("ADMIN");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(roleRepository.findById(2)).thenReturn(java.util.Optional.of(role));

        adminController.grantUserAdmin(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).findById(2);
        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userRepository, roleRepository);
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

    @Test
    void deleteUserStatus() throws Exception {
        mockMvc.perform(
                delete("/admin/deleteUser/{id}", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void deleteNullUserVerifyTimes() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        adminController.deleteUser(request, 1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).deleteById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUserVerifyTimes() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        adminController.deleteUser(request, 1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(userRepository);
    }
}