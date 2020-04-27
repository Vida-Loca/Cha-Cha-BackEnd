package com.vidaloca.skibidi.event.forum.service;

import com.vidaloca.skibidi.event.forum.dto.PostDto;
import com.vidaloca.skibidi.event.forum.model.Post;

import java.util.List;

public interface PostService {
    List<Post> findAllEventPosts(Long eventId, Long userId);

    List<Post> findAllEventUserPosts(Long eventId, Long userId);

    Post addNewPost(Long eventId, Long userId, PostDto postDto);

    Post updatePost(Long postId, Long userId, PostDto postDto);

    boolean deletePost (Long postId, Long userId);

    Post likePost (Long postId, Long userId);
}
