package com.vidaloca.skibidi.integration.product.controller;

import com.vidaloca.skibidi.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerIT extends BaseIT {

    @Test
    @Transactional
    void getEventProducts() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/product", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("TestProduct")))
                .andDo(print());
    }

    @Test
    @Transactional
    void addProductToEvent() {
    }

    @Test
    @Transactional
    void testAddProductToEvent() {
    }

    @Test
    @Transactional
    void updateProduct() {
    }

    @Test
    @Transactional
    void getEventUserProducts() {
    }

    @Test
    @Transactional
    void getMyEventUserProducts() {
    }

    @Test
    @Transactional
    void deleteProductFromEvent() {
    }
}