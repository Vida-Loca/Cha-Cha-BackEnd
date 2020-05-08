package com.vidaloca.skibidi.user.account.controller;

import com.vidaloca.skibidi.BaseIT;
import com.vidaloca.skibidi.user.account.dto.NamesDto;
import com.vidaloca.skibidi.user.account.dto.PasswordDto;
import com.vidaloca.skibidi.user.account.repository.ResetPasswordTokenRepository;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserAccountControllerIT extends BaseIT {
    Authentication authentication;
    String token;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ResetPasswordTokenRepository passwordTokenRepository;

    @BeforeEach
    void setUp() {
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "testowy1",
                        "password"
                )
        );

        token = jwtTokenProvider.generateToken(authentication);
    }

    @Test
    @Transactional
    void getCurrentUser() throws Exception {
        mockMvc.perform(get("/user")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("test1")))
                .andExpect(jsonPath("$.surname", is("Test1")))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllUserEvents() throws Exception {
        mockMvc.perform(get("/user/event")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @Transactional
    void isAdminReturnFalse() throws Exception {
        mockMvc.perform(get("/user/isAdmin")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)))
                .andDo(print());
    }

    @Test
    @Transactional
    void isAdminReturnTrue() throws Exception {
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "admin1",
                        "password"
                )
        );

        token = jwtTokenProvider.generateToken(authentication);

        mockMvc.perform(get("/user/isAdmin")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)))
                .andDo(print());
    }

    @Test
    @Transactional
    void changePhoto() throws Exception {
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
                .header("Authorization", "Bearer " + token)
                .param("email", "test1@o2.pl"))
                .andExpect(jsonPath("$", is("Check your mail box")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void resetPasswordPostInvalidEmail() throws Exception {
        mockMvc.perform(post("/user/resetPassword")
                .header("Authorization", "Bearer " + token)
                .param("email", "wrong@o2.pl"))
                .andExpect(jsonPath("$", is("User with following email: wrong@o2.pl not found.")))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    void confirmResetPassword() throws Exception {
        assertFalse(userRepository.findByUsername("testowy1").get().isCanChangePass());

        mockMvc.perform(get("/user/changePassword")
                .header("Authorization", "Bearer " + token)
                .param("userId", "1")
                .param("token", "valid"))
                .andExpect(status().isOk())
                .andDo(print());


        assertTrue(userRepository.findByUsername("testowy1").get().isCanChangePass());
    }

    @Test
    @Transactional
    void confirmResetPasswordInvalidToken() throws Exception {
        mockMvc.perform(get("/user/changePassword")
                .header("Authorization", "Bearer " + token)
                .param("userId", "1")
                .param("token", "invalid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("invalidToken")))
                .andDo(print());

        assertFalse(userRepository.findByUsername("testowy1").get().isCanChangePass());
    }

    @Test
    @Transactional
    void confirmResetPasswordExpiredToken() throws Exception {
        mockMvc.perform(get("/user/changePassword")
                .header("Authorization", "Bearer " + token)
                .param("userId", "1")
                .param("token", "expired"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("expired")))
                .andDo(print());

        assertFalse(userRepository.findByUsername("testowy1").get().isCanChangePass());
    }

    @Test
    @Transactional
    void resetPasswordPut() throws Exception {
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "changePass",
                        "password"
                )
        );

        token = jwtTokenProvider.generateToken(authentication);

        PasswordDto dto = new PasswordDto();
        dto.setPassword("newPassword");
        dto.setMatchingPassword("newPassword");

        mockMvc.perform(put("/user/changePassword")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk())
                .andDo(print());

        assertNotEquals("$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2",
                userRepository.findByUsername("changePass").get().getPassword());
        assertFalse(userRepository.findByUsername("changePass").get().isCanChangePass());
    }

    @Test
    @Transactional
    void resetPasswordPutNotMatching() throws Exception {
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "changePass",
                        "password"
                )
        );

        token = jwtTokenProvider.generateToken(authentication);

        PasswordDto dto = new PasswordDto();
        dto.setPassword("newPassword");
        dto.setMatchingPassword("notMatch");

        mockMvc.perform(put("/user/changePassword")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(jsonPath("$", is("Passwords not matches")))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void resetPasswordPutCantChange() throws Exception {
        PasswordDto dto = new PasswordDto();
        dto.setPassword("newPassword");
        dto.setMatchingPassword("newPassword");

        mockMvc.perform(put("/user/changePassword")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andDo(print());

        assertEquals("$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2",
                userRepository.findByUsername("testowy1").get().getPassword());
    }

}