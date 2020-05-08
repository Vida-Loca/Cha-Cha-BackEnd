package com.vidaloca.skibidi.unit.product.controller;

import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import com.vidaloca.skibidi.product.controller.ProductValueController;
import com.vidaloca.skibidi.product.service.ProductValueService;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductValueControllerTest {

    @Mock
    ProductValueService service;
    @Mock
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    ProductValueController controller;

    CurrentUser currentUser;
    MockMvc mockMvc;
    HttpServletRequest request;

    @BeforeEach
    void setUp() {
        currentUser = new CurrentUser(jwtAuthenticationFilter, jwtTokenProvider);
        request = mock(HttpServletRequest.class);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getTotalEventAmount() throws Exception {
        mockMvc.perform(get("/event/{eventId}/amount", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getTotalUserAmount() throws Exception {
        mockMvc.perform(get("/event/{eventId}/user/{userId}/amount", 1, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getTotalCurrentUserAmount() throws Exception {
        mockMvc.perform(get("/event/{eventId}/current_user/amount", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getTotalProductAmount() throws Exception {
        mockMvc.perform(get("/event/{eventId}/product/{productId}/amount", 1, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getTotalProductCategoryAmount() throws Exception {
        mockMvc.perform(get("/event/{eventId}/product_category/{productCategoryId}/amount", 1, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}