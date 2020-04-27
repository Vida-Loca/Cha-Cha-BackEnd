package com.vidaloca.skibidi.event.forum.controller;

import com.vidaloca.skibidi.event.forum.dto.PostDto;
import com.vidaloca.skibidi.event.forum.model.Post;
import com.vidaloca.skibidi.event.forum.service.PostService;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ForumController {

    private PostService postService;

    @Autowired
    public ForumController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/event/{eventId}/forum")
    public List<Post> getEventPosts(@PathVariable Long eventId, HttpServletRequest request){
       return postService.findAllEventPosts(eventId, CurrentUser.currentUserId(request));
    }

    @GetMapping("/event/{eventId}/forum/user")
    public List<Post> getAllUserPostInEvent(@PathVariable Long eventId,HttpServletRequest request){
        return postService.findAllEventUserPosts(eventId,CurrentUser.currentUserId(request));
    }

    @PostMapping("/event/{eventId}/forum")
    public Post addNewPost(@PathVariable Long eventId, @RequestBody PostDto postDto, HttpServletRequest request){
        return postService.addNewPost(eventId,CurrentUser.currentUserId(request),postDto);
    }

    @PutMapping("/event/forum/{postId}")
    public Post updatePost(@PathVariable Long postId, @RequestBody PostDto postDto, HttpServletRequest request){
        return postService.updatePost(postId,CurrentUser.currentUserId(request),postDto);
    }

    @PutMapping("/event/forum/{postId}/like")
    public Post likePost(@PathVariable Long postId, HttpServletRequest request){
        return postService.likePost(postId,CurrentUser.currentUserId(request));
    }

    @DeleteMapping("/event/fourm/{postId}")
    public boolean deletePost(@PathVariable Long postId, HttpServletRequest request){
        return postService.deletePost(postId,CurrentUser.currentUserId(request));
    }


}
