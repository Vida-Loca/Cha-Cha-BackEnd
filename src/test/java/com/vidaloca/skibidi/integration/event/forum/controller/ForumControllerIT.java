package com.vidaloca.skibidi.integration.event.forum.controller;

import com.vidaloca.skibidi.BaseIT;
import com.vidaloca.skibidi.event.forum.dto.PostDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ForumControllerIT extends BaseIT {

    @Test
    @Transactional
    void getEventPosts() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/forum", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getAllUserPostInEvent() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/forum/user", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    @Transactional
    void addNewPost() throws Exception {
        String token = authenticateUser("testowy1", "password");
        PostDto dto = new PostDto();
        dto.setPost("NewTestPost");

        mockMvc.perform(post("/event/{eventId}/forum", 10)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void updatePost() throws Exception {
        String token = authenticateUser("testowy1", "password");
        PostDto dto = new PostDto();
        dto.setPost("UpdatedPost");

        mockMvc.perform(put("/event/forum/{postId}", 10)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.updated").value(true))
                .andDo(print());
    }

    @Test
    @Transactional
    void updatePost_NotExisting() throws Exception {
        String token = authenticateUser("testowy1", "password");
        PostDto dto = new PostDto();
        dto.setPost("UpdatedPost");

        mockMvc.perform(put("/event/forum/{postId}", 99)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    void updatePost_NotAllowed() throws Exception {
        String token = authenticateUser("testowy2", "password");
        PostDto dto = new PostDto();
        dto.setPost("UpdatedPost");

        mockMvc.perform(put("/event/forum/{postId}", 10)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void likePost() throws Exception {
        String token = authenticateUser("testowy2", "password");

        mockMvc.perform(put("/event/forum/{postId}/like", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likes").value(2))
                .andDo(print());
    }

    @Test
    @Transactional
    void likePostAlreadyLiked() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/event/forum/{postId}/like", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void likePostNotAllowed() throws Exception {
        String token = authenticateUser("testowy4", "password");

        mockMvc.perform(put("/event/forum/{postId}/like", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    void deletePost() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(delete("/event/forum/{postId}", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true))
                .andDo(print());
    }

    @Test
    @Transactional
    void deletePost_NotAllowed() throws Exception {
        String token = authenticateUser("testowy3", "password");

        mockMvc.perform(delete("/event/forum/{postId}", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void deletePost_NotAllowed1() throws Exception {
        String token = authenticateUser("testowy4", "password");

        mockMvc.perform(delete("/event/forum/{postId}", 11)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}