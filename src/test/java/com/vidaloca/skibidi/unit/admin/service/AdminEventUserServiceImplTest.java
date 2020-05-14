package com.vidaloca.skibidi.unit.admin.service;

import com.vidaloca.skibidi.admin.service.AdminEventUserServiceImpl;
import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdminEventUserServiceImplTest {
    @Mock
    EventRepository eventRepository;
    @Mock
    EventUserRepository eventUserRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    AdminEventUserServiceImpl service;

    Event event;
    EventUser eventUser;
    User user;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        user = new User();
        user.setId(1L);
        eventUser = new EventUser();
        eventUser.setId(1L);
        eventUser.setEvent(event);
        eventUser.setUser(user);
    }

    @Test
    void findAllEventUsers() {
        //given
        List<EventUser> eventUsers = new ArrayList<>();
        eventUsers.add(eventUser);

        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        given(eventUserRepository.findAllByEvent(event)).willReturn(eventUsers);

        //when
        List<User> result = service.findAllEventUsers(1L);

        //then
        assertEquals(1, result.size());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findAllByEvent(any(Event.class));
    }

    @Test
    void findAllEventUsers_EventNotFound() {
        //given
        List<EventUser> eventUsers = new ArrayList<>();
        eventUsers.add(eventUser);

        given(eventRepository.findById(1L)).willReturn(Optional.empty());

        //when
        Exception result = assertThrows(EventNotFoundException.class, () -> {
            service.findAllEventUsers(1L);
        });

        //then
        assertEquals("Event with id: 1 not found.", result.getMessage());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).shouldHaveNoInteractions();
    }

    @Test
    void deleteUserFromEvent() {
        //given
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.ofNullable(eventUser));

        //when
        String result = service.deleteUserFromEvent(1L, 1L);

        //then
        assertEquals("Successfully removed user from event", result);
        then(eventRepository).should().findById(anyLong());
        then(userRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(eventUserRepository).should().delete(any(EventUser.class));
    }

    @Test
    void grantTakeUserEventAdmin() {
    }
}