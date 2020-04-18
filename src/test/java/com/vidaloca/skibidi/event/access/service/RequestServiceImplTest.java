package com.vidaloca.skibidi.event.access.service;

import com.vidaloca.skibidi.event.access.model.EventRequest;
import com.vidaloca.skibidi.event.access.repository.EventRequestRepository;
import com.vidaloca.skibidi.event.access.status.AccessStatus;
import com.vidaloca.skibidi.event.exception.model.UserActuallyInEventException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.type.EventType;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock(lenient = true)
    EventRepository eventRepository;
    @Mock
    EventUserRepository eventUserRepository;
    @Mock
    EventRequestRepository eventRequestRepository;

    @InjectMocks
    RequestServiceImpl service;

    User user;
    Event event;
    EventUser eventUser;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        event = new Event();
        event.setId(1L);
        eventUser = new EventUser();
        eventUser.setId(1L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
    }

    @Test
    void sendRequestToEvent() throws UserActuallyInEventException {
        //given
        EventRequest eventRequest = new EventRequest();
        event.setEventType(EventType.NORMAL);

        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.empty());
        given(eventRequestRepository.save(any(EventRequest.class))).willReturn(eventRequest);

        //when
        EventRequest result = service.sendRequestToEvent(1L, 1L);

        //then
        assertEquals(eventRequest, result);
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(eventRequestRepository).should().save(any(EventRequest.class));
    }

    @Test
    void showAllEventRequest() {
        //given
        EventRequest e1 = new EventRequest();
        EventRequest e2 = new EventRequest();

        event.getEventRequests().add(e1);
        event.getEventRequests().add(e2);

        eventUser.setUser(user);
        eventUser.setEvent(event);
        eventUser.setAdmin(true);

        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));

        //when
        List<EventRequest> result = service.showAllEventRequest(1L, 1L);

        //then
        assertEquals(2, result.size());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void showAllUserRequest() {
        //given
        EventRequest e1 = new EventRequest();
        EventRequest e2 = new EventRequest();

        user.getEventRequests().add(e1);
        user.getEventRequests().add(e2);

        //when
        List<EventRequest> result = service.showAllUserRequest(1L);

        //then
        assertEquals(2, result.size());
        then(userRepository).should().findById(anyLong());
    }

    @Test
    void rejectRequest() {
        //given
        EventRequest eventRequest = new EventRequest();
        eventRequest.setId(1L);
        eventRequest.setEvent(event);
        eventRequest.setAccessStatus(AccessStatus.PROCESSING);

        eventUser.setUser(user);
        eventUser.setEvent(event);
        eventUser.setAdmin(true);

        given(eventRequestRepository.findById(1L)).willReturn(Optional.of(eventRequest));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));
        given(eventRequestRepository.save(any(EventRequest.class))).willReturn(eventRequest);

        //when
        EventRequest result = service.rejectRequest(1L, 1L);

        //then
        assertEquals("Rejected", result.getAccessStatus().toString());
        then(userRepository).should().findById(anyLong());
        then(eventRequestRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(eventRequestRepository).should().save(any(EventRequest.class));
    }

    @Test
    void acceptRequest() {
        //given
        EventRequest eventRequest = new EventRequest();
        eventRequest.setId(1L);
        eventRequest.setEvent(event);
        eventRequest.setAccessStatus(AccessStatus.PROCESSING);

        eventUser.setUser(user);
        eventUser.setEvent(event);
        eventUser.setAdmin(true);

        given(eventRequestRepository.findById(1L)).willReturn(Optional.of(eventRequest));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));
        given(eventRequestRepository.save(any(EventRequest.class))).willReturn(eventRequest);

        //when
        EventRequest result = service.acceptRequest(1L, 1L);

        //then
        assertEquals("Accepted", result.getAccessStatus().toString());
        then(userRepository).should().findById(anyLong());
        then(eventRequestRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(eventUserRepository).should().save(any(EventUser.class));
        then(eventRequestRepository).should().save(any(EventRequest.class));
    }
}