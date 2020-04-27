package com.vidaloca.skibidi.event.forum.exception;

public class IsNotUsersPostException extends RuntimeException {
    public IsNotUsersPostException(Long postId, Long userId){
        super("User with id: "  + userId + " can't change/delete post with id: " + postId);
    }
}
