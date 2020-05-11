package com.vidaloca.skibidi.unit.event.open.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.open.service.PublicEventServiceImpl;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.type.EventType;
import com.vidaloca.skibidi.friendship.exception.UserNotAllowedException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
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
class PublicEventServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    EventRepository eventRepository;
    @Mock
    EventUserRepository eventUserRepository;

    @InjectMocks
    PublicEventServiceImpl service;

    @Test
    void joinEvent() {
        //given
        User user = new User();
        Event event = new Event();
        event.setEventType(EventType.PUBLIC);
        EventUser eventUser = new EventUser();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        given(eventUserRepository.save(any(EventUser.class))).willReturn(eventUser);

        //when
        EventUser result = service.joinEvent(1L, 1L);

        //then
        assertEquals(eventUser, result);
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().save(any(EventUser.class));
    }

    @Test
    void joinEventUserNotAllowed() {
        //given
        User user = new User();
        Event event = new Event();
        event.setEventType(EventType.PRIVATE);
        EventUser eventUser = new EventUser();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));

        //when
        Exception result = assertThrows(UserNotAllowedException.class, () -> {
            service.joinEvent(1L, 1L);
        });

        //then
        assertEquals("User with id: 1 is not allowed to join that event", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).shouldHaveNoInteractions();
    }

    @Test
    void findAllPublicEvents() {
        //given
        List<Event> events = new ArrayList<>();
        Event event = new Event();
        Event event1 = new Event();
        event.setEventType(EventType.PUBLIC);
        event1.setEventType(EventType.PUBLIC);
        events.add(event);
        events.add(event1);

        given(eventRepository.findAll()).willReturn(events);

        //when
        List<Event> result = service.findAllPublicEvents();

        //then
        assertEquals(events, result);
        assertEquals(2, result.size());
        then(eventRepository).should().findAll();
    }
}