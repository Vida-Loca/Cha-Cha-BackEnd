package com.vidaloca.skibidi.integration.admin.controller;

import com.vidaloca.skibidi.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminUserControllerIT extends BaseIT {

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(get("/admin/getAllUsers")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllAdmins() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(get("/admin/getAllAdmins")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getUserById() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(get("/admin/user/{id}", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testowy1")))
                .andDo(print());
    }

    @Test
    @Transactional
    void deleteById() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(delete("/admin/user/{id}", 16)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("User deleted successfully")))
                .andDo(print());
    }

    @Test
    @Transactional
    void deleteByIdNotFound() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(delete("/admin/user/{id}", 99)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("User with id: 99 is not found")))
                .andDo(print());
    }

    @Test
    @Transactional
    void grantUserAuthority() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(put("/admin/user/{id}/grantAdmin", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals("ADMIN", userRepository.findById(10L).get().getRole().getName());
    }

    @Test
    @Transactional
    void banishUser() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(put("/admin/user/{id}/banishUser", 16)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.banned").value(true))
                .andDo(print());

        assertEquals("BANNED", userRepository.findById(16L).get().getRole().getName());
    }

    @Test
    @Transactional
    void unbanUser() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(put("/admin/user/{id}/banishUser", 17)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.banned").value(false))
                .andDo(print());

        assertEquals("USER", userRepository.findById(16L).get().getRole().getName());
    }
}