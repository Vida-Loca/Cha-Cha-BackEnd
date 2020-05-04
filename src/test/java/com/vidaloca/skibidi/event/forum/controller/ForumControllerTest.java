package com.vidaloca.skibidi.event.forum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import com.vidaloca.skibidi.event.forum.dto.PostDto;
import com.vidaloca.skibidi.event.forum.service.PostService;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ForumControllerTest {

    @Mock
    PostService service;

    @Mock
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    ForumController controller;

    MockHttpServletRequest request;
    MockMvc mockMvc;
    CurrentUser currentUser;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        currentUser = new CurrentUser(jwtAuthenticationFilter, jwtTokenProvider);
    }

    @Test
    void getEventPosts() throws Exception {
        mockMvc.perform(get("/event/{eventId}/forum", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUserPostInEvent() throws Exception {
        mockMvc.perform(get("/event/{eventId}/forum/user", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addNewPost() throws Exception {
        PostDto dto = new PostDto();
        dto.setPost("Test post");

        mockMvc.perform(post("/event/{eventId}/forum", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updatePost() throws Exception {
        PostDto dto = new PostDto();
        dto.setPost("Test post");

        mockMvc.perform(put("/event/forum/{postId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(dto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void likePost() throws Exception {
        mockMvc.perform(put("/event/forum/{postId}/like", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete("/event/forum/{postId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
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