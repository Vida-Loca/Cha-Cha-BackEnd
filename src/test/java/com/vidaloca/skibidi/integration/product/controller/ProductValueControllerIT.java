package com.vidaloca.skibidi.integration.product.controller;

import com.vidaloca.skibidi.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductValueControllerIT extends BaseIT {

    @Test
    @Transactional
    void getTotalEventAmount() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/amount", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(BigDecimal.valueOf(13.5)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getTotalEventAmountThrowUserIsNotInEvent() throws Exception {
        String token = authenticateUser("changePass", "password");

        mockMvc.perform(get("/event/{eventId}/amount", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("User with id: 11 is not in event with id: 10 and cannot make this action.")))
                .andDo(print());
    }

    @Test
    @Transactional
    void getTotalUserAmount() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/user/{userId}/amount", 10, 14)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(BigDecimal.valueOf(2.5)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getTotalUserAmountThrowUserIsNotInEvent() throws Exception {
        String token = authenticateUser("changePass", "password");

        mockMvc.perform(get("/event/{eventId}/user/{userId}/amount", 10, 14)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("User with id: 11 is not in event with id: 10 and cannot make this action.")))
                .andDo(print());
    }

    @Test
    @Transactional
    void getTotalCurrentUserAmount() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/current_user/amount", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(BigDecimal.valueOf(11.0)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getTotalProductAmount() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/product/{productId}/amount", 10, 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(BigDecimal.valueOf(2.0)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getTotalProductAmountThrowUserIsNotInEvent() throws Exception {
        String token = authenticateUser("changePass", "password");

        mockMvc.perform(get("/event/{eventId}/product/{productId}/amount", 10, 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("User with id: 11 is not in event with id: 10 and cannot make this action.")))
                .andDo(print());
    }

    @Test
    @Transactional
    void getTotalProductCategoryAmount() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/product_category/{productCategoryId}/amount", 10, 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(BigDecimal.valueOf(4.5)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getTotalProductCategoryAmountThrowUserIsNotInEvent() throws Exception {
        String token = authenticateUser("changePass", "password");

        mockMvc.perform(get("/event/{eventId}/product_category/{productCategoryId}/amount", 10, 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("User with id: 11 is not in event with id: 10 and cannot make this action.")))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllUsersExpenses() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/users_expenses", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllUsersExpensesNotInEvent() throws Exception {
        String token = authenticateUser("testowy4", "password");

        mockMvc.perform(get("/event/{eventId}/users_expenses", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("User with id: 16 is not in event with id: 10 and cannot make this action."))
                .andDo(print());
    }
}