package com.vidaloca.skibidi.unit.event.forum.service;

import com.vidaloca.skibidi.event.exception.model.UserIsNotInEventException;
import com.vidaloca.skibidi.event.forum.dto.PostDto;
import com.vidaloca.skibidi.event.forum.exception.IsNotUsersPostException;
import com.vidaloca.skibidi.event.forum.exception.PostNotFoundException;
import com.vidaloca.skibidi.event.forum.model.Post;
import com.vidaloca.skibidi.event.forum.repository.PostRepository;
import com.vidaloca.skibidi.event.forum.service.PostServiceImpl;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;
    @Mock(lenient = true)
    EventRepository eventRepository;
    @Mock(lenient = true)
    EventUserRepository eventUserRepository;

    @InjectMocks
    PostServiceImpl service;

    User user;
    Event event;
    EventUser eventUser;
    Post post;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        event = new Event();
        event.setId(1L);
        eventUser = new EventUser();
        eventUser.setId(1L);
        eventUser.setUser(user);
        eventUser.setEvent(event);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
    }

    @Test
    void findAllEventPosts() {
        //given
        post = new Post();
        List<Post> list = new ArrayList<>();
        list.add(post);
        given(postRepository.findAllByEventUser_EventOrderByTimePostedDesc(event)).willReturn(list);
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));

        //when
        List<Post> result = service.findAllEventPosts(1L, 1L);

        //then
        assertEquals(1, result.size());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(postRepository).should().findAllByEventUser_EventOrderByTimePostedDesc(any(Event.class));
    }

    @Test
    void findAllEventUserPosts() {
        //given
        post = new Post();
        List<Post> list = new ArrayList<>();
        list.add(post);
        given(postRepository.findAllByEventUserOrderByTimePostedDesc(eventUser)).willReturn(list);
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));

        //when
        List<Post> result = service.findAllEventUserPosts(1L, 1L);

        //then
        assertEquals(1, result.size());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(postRepository).should().findAllByEventUserOrderByTimePostedDesc(any(EventUser.class));
    }

    @Test
    void addNewPost() {
        //given
        Post post = new Post();
        post.setText("Test post");
        PostDto dto = new PostDto("Test post");
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));
        given(postRepository.save(any(Post.class))).willReturn(post);

        //when
        Post result = service.addNewPost(1L, 1L, dto);

        //then
        assertEquals("Test post", result.getText());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(postRepository).should().save(any(Post.class));
    }

    @Test
    void updatePost() {
        //given
        PostDto dto = new PostDto();
        dto.setPost("Update post");
        post = new Post();
        post.setId(1L);
        post.setEventUser(eventUser);

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(post);

        //when
        Post result = service.updatePost(1L, 1L, dto);

        //then
        assertEquals("Update post", result.getText());
        then(userRepository).should().findById(anyLong());
        then(postRepository).should().findById(anyLong());
        then(postRepository).should().save(any(Post.class));
    }

    @Test
    void updatePostNotFound() {
        //given
        PostDto dto = new PostDto();

        given(postRepository.findById(1L)).willReturn(Optional.empty());

        //when
        Exception result = assertThrows(PostNotFoundException.class, () -> {
            service.updatePost(1L, 1L, dto);
        });

        //then
        assertEquals("Post with id: 1 not found", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(postRepository).should().findById(anyLong());
        then(postRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void updatePostNotAllowed() {
        PostDto dto = new PostDto();
        dto.setPost("Update post");
        post = new Post();
        post.setId(1L);
        eventUser.setUser(null);
        post.setEventUser(eventUser);

        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        //when
        Exception result = assertThrows(IsNotUsersPostException.class, () -> {
            service.updatePost(1L, 1L, dto);
        });

        //then
        assertEquals("User with id: 1 can't change/delete post with id: 1", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(postRepository).should().findById(anyLong());
        then(postRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deletePost() {
        //given
        eventUser.setAdmin(true);

        post = new Post();
        post.setId(1L);
        post.setEventUser(eventUser);

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));


        //when
        boolean result = service.deletePost(1L, 1L);

        //then
        assertTrue(result);
        then(userRepository).should().findById(anyLong());
        then(postRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(postRepository).should().deleteById(anyLong());
    }

    @Test
    void deletePostNotAdminAndNotOwner() {
        //given
        eventUser.setAdmin(false);
        EventUser eu = new EventUser();
        eu.setEvent(event);
        post = new Post();
        post.setId(1L);
        post.setEventUser(eu);

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));

        //when
        Exception result = assertThrows(IsNotUsersPostException.class, () -> {
            service.deletePost(1L, 1L);
        });

        //then
        assertEquals("User with id: 1 can't change/delete post with id: 1", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(postRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(postRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void likePost() {
        //given
        post = new Post();
        post.setId(1L);
        post.setEventUser(eventUser);

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));
        given(postRepository.save(post)).willReturn(post);

        //when
        Post result = service.likePost(1L, 1L);

        //then
        assertEquals(1, result.getLikes());
        then(userRepository).should().findById(anyLong());
        then(postRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(postRepository).should().save(any(Post.class));
    }

    @Test
    void likePostUserIsNotInEvent() {
        //given
        Event newEvent = new Event();
        newEvent.setId(3L);
        eventUser.setEvent(newEvent);
        post = new Post();
        post.setId(1L);
        post.setEventUser(eventUser);

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));

        //when
        Exception result = assertThrows(UserIsNotInEventException.class, () -> {
            service.likePost(1L, 1L);
        });

        //then
        assertEquals("User with id: 1 is not in event with id: 3 and cannot make this action.", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(postRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(postRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void likePostActuallyLiked() {
        //given
        Post post = new Post();
        post.setId(1L);
        post.setEventUser(eventUser);
        post.getLikers().add(eventUser);

        eventUser.getLikes().add(post);

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));
        given(postRepository.save(post)).willReturn(post);

        //when
        Post result = service.likePost(1L, 1L);

        //then
        assertEquals(0, result.getLikes());
        then(userRepository).should().findById(anyLong());
        then(postRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(postRepository).should().save(any(Post.class));
    }

}