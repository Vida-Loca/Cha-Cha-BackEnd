package com.vidaloca.skibidi.integration.user.account.controller;

import com.vidaloca.skibidi.integration.BaseIT;
import com.vidaloca.skibidi.user.account.dto.NamesDto;
import com.vidaloca.skibidi.user.account.dto.PasswordDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserAccountControllerIT extends BaseIT {

    @Test
    @Transactional
    void getCurrentUser() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/user")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Name10")))
                .andExpect(jsonPath("$.surname", is("Surname10")))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllUserEvents() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/user/event")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andDo(print());
    }

    @Test
    @Transactional
    void isAdminReturnFalse() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/user/isAdmin")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)))
                .andDo(print());
    }

    @Test
    @Transactional
    void isAdminReturnTrue() throws Exception {
        String token = authenticateUser("admin1", "password");

        mockMvc.perform(get("/user/isAdmin")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)))
                .andDo(print());
    }

    @Test
    @Transactional
    void changePhoto() throws Exception {
        String token = authenticateUser("testowy1", "password");

        String url = "NewUrl";
        mockMvc.perform(put("/user/changePhoto")
                .header("Authorization", "Bearer " + token)
                .param("url", url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("picUrl", is(url)))
                .andDo(print());
    }

    @Test
    @Transactional
    void changeNames() throws Exception {
        String token = authenticateUser("testowy1", "password");

        NamesDto dto = new NamesDto();
        dto.setName("NewName");
        dto.setSurname("NewSurname");

        mockMvc.perform(put("/user/changeNames")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("NewName")))
                .andExpect(jsonPath("surname", is("NewSurname")))
                .andDo(print());
    }

    @Test
    @Transactional
    void resetPasswordPost() throws Exception {
        mockMvc.perform(post("/user/resetPassword")
                .param("email", "test10@o2.pl"))
                .andExpect(jsonPath("$", is("Check your mail box")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void resetPasswordPostEmailNotFound() throws Exception {
        mockMvc.perform(post("/user/resetPassword")
                .param("email", "wrong@o2.pl"))
                .andExpect(jsonPath("$", is("User with following email: wrong@o2.pl not found.")))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    void resetPassword() throws Exception {
        PasswordDto dto = new PasswordDto("password", "password");

        mockMvc.perform(put("/user/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto))
                .param("userId", "10")
                .param("token", "valid"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void resetPasswordInvalidToken() throws Exception {
        PasswordDto dto = new PasswordDto("password", "password");

        mockMvc.perform(put("/user/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto))
                .param("userId", "10")
                .param("token", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Token not valid")))
                .andDo(print());
    }

    @Test
    @Transactional
    void resetPasswordExpiredToken() throws Exception {
        PasswordDto dto = new PasswordDto("password", "password");

        mockMvc.perform(put("/user/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto))
                .param("userId", "10")
                .param("token", "expired"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Token expired")))
                .andDo(print());
    }

    @Test
    @Transactional
    void resetPasswordNotMatches() throws Exception {
        PasswordDto dto = new PasswordDto("password", "notMatch");

        mockMvc.perform(put("/user/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto))
                .param("userId", "10")
                .param("token", "valid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Passwords not matches")))
                .andDo(print());
    }


}