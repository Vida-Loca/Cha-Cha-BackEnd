package com.vidaloca.skibidi.unit.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import com.vidaloca.skibidi.product.controller.ProductController;
import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.product.service.ProductService;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductService service;
    @Mock
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    ProductController controller;

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
    void getEventProducts() throws Exception {
        mockMvc.perform(get("/event/{eventId}/product", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void addProductNewToEvent() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setName("Name");
        productDto.setProductCategory("Cat");
        productDto.setPrice(new BigDecimal(10));

        mockMvc.perform(post("/event/{eventId}/productNew", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(productDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateProduct() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setName("Name");
        productDto.setProductCategory("Cat");
        productDto.setPrice(new BigDecimal(10));

        mockMvc.perform(put("/event/{eventId}/product/{productId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(productDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getEventUserProducts() throws Exception {
        mockMvc.perform(get("/event/{eventId}/user/{userId}/products", 1, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getMyEventUserProducts() throws Exception {
        mockMvc.perform(get("/event/{eventId}/myproducts", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteProductFromEvent() throws Exception {
        mockMvc.perform(delete("/event/{eventId}/product", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .param("productToDeleteId", "1"))
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