package com.vidaloca.skibidi.event.forum.service;

import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotInEventException;
import com.vidaloca.skibidi.event.forum.dto.PostDto;
import com.vidaloca.skibidi.event.forum.exception.IsNotUsersPostException;
import com.vidaloca.skibidi.event.forum.exception.PostNotFoundException;
import com.vidaloca.skibidi.event.forum.model.Post;
import com.vidaloca.skibidi.event.forum.repository.PostRepository;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class PostServiceImpl implements PostService {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;

    @Autowired
    public PostServiceImpl(UserRepository userRepository, PostRepository postRepository, EventRepository eventRepository, EventUserRepository eventUserRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.eventRepository = eventRepository;
        this.eventUserRepository = eventUserRepository;
    }

    @Override
    public List<Post> findAllEventPosts(Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        return postRepository.findAllByEventUser_EventOrderByTimePostedDesc(event);
    }

    @Override
    public List<Post> findAllEventUserPosts(Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        return postRepository.findAllByEventUserOrderByTimePostedDesc(eu);
    }

    @Override
    public Post addNewPost(Long eventId, Long userId, PostDto postDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        return Post.builder().eventUser(eu).text(postDto.getPost()).build();
    }

    @Override
    public Post updatePost(Long postId, Long userId, PostDto postDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException(postId));
        if (post.getEventUser().getUser() != user)
            throw new IsNotUsersPostException(postId,userId);
        post.setText(postDto.getPost());
        post.setTimePosted(LocalDateTime.now());
        post.setUpdated(true);
        return postRepository.save(post);
    }

    @Override
    public boolean deletePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException(postId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, post.getEventUser().getEvent()).orElse(null);
        if (post.getEventUser().getUser() != user && eu == null)
            throw new IsNotUsersPostException(postId,userId);
        if (eu != null && !eu.isAdmin()){
            throw new IsNotUsersPostException(postId,userId);
        }
        postRepository.deleteById(postId);
        return true;
    }

    @Override
    public Post likePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException(postId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, post.getEventUser().getEvent()).orElseThrow(()
                -> new UserIsNotInEventException(user.getId(), post.getEventUser().getEvent().getId()));
        post.setLikes(post.getLikes() + 1);
        return postRepository.save(post);
    }
}
