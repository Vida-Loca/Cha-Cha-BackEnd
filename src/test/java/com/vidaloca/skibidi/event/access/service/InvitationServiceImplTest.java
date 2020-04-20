package com.vidaloca.skibidi.event.access.service;

import com.vidaloca.skibidi.event.access.model.EventInvitation;
import com.vidaloca.skibidi.event.access.repository.EventInvitationRepository;
import com.vidaloca.skibidi.event.access.status.AccessStatus;
import com.vidaloca.skibidi.event.exception.model.UserActuallyInEventException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class InvitationServiceImplTest {

    @Mock(lenient = true)
    EventRepository eventRepository;
    @Mock(lenient = true)
    UserRepository userRepository;
    @Mock
    EventInvitationRepository eventInvitationRepository;
    @Mock
    EventUserRepository eventUserRepository;

    @InjectMocks
    InvitationServiceImpl service;

    User user1;
    User user2;
    Event event;
    EventUser eventUser;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user2 = new User();
        user2.setId(2L);
        event = new Event();
        event.setId(1L);
        eventUser = new EventUser();
        eventUser.setId(1L);
        eventUser.setUser(user1);
        eventUser.setAdmin(true);
        eventUser.setEvent(event);

        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        given(userRepository.findById(2L)).willReturn(Optional.of(user2));
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
    }

    @Test
    void inviteToEvent() throws UserActuallyInEventException {
        //given
        EventInvitation eventInvitation = new EventInvitation();

        given(eventUserRepository.findByUserAndEvent(user1, event)).willReturn(Optional.of(eventUser));
        given(eventUserRepository.findByUserAndEvent(user2, event)).willReturn(Optional.empty());
        given(eventInvitationRepository.save(any(EventInvitation.class))).willReturn(eventInvitation);

        //when
        EventInvitation result = service.inviteToEvent(1L, 2L, 1L);

        //then
        assertEquals(eventInvitation, result);
        then(userRepository).should(times(2)).findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should(times(2)).findByUserAndEvent(any(User.class), any(Event.class));
        then(eventInvitationRepository).should().save(any(EventInvitation.class));
    }

    @Test
    void showAllUserInvitations() {
        //given
        EventInvitation eventInvitation1 = new EventInvitation();
        EventInvitation eventInvitation2 = new EventInvitation();
        user1.getEventInvitations().add(eventInvitation1);
        user1.getEventInvitations().add(eventInvitation2);

        //when
        List<EventInvitation> result = service.showAllUserInvitations(1L);

        //then
        assertEquals(2, result.size());
        then(userRepository).should().findById(anyLong());
    }

    @Test
    void showAllEventInvitations() {
        //given
        EventInvitation eventInvitation1 = new EventInvitation();
        EventInvitation eventInvitation2 = new EventInvitation();
        event.getEventInvitations().add(eventInvitation1);
        event.getEventInvitations().add(eventInvitation2);
        given(eventUserRepository.findByUserAndEvent(user1, event)).willReturn(Optional.of(eventUser));

        //when
        List<EventInvitation> result = service.showAllEventInvitations(1L, 1L);

        //then
        assertEquals(2, result.size());
        then(eventRepository).should().findById(anyLong());
        then(userRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void acceptInvitation() {
        //given
        EventInvitation eventInvitation = new EventInvitation();
        eventInvitation.setId(1L);
        eventInvitation.setEvent(event);
        eventInvitation.setAccessStatus(AccessStatus.PROCESSING);

        given(eventInvitationRepository.findById(1L)).willReturn(Optional.of(eventInvitation));
        given(eventInvitationRepository.save(any(EventInvitation.class))).willReturn(eventInvitation);

        //when
        EventInvitation result = service.acceptInvitation(1L, 1L);

        //then
        assertEquals("Accepted", result.getAccessStatus().toString());
        then(eventInvitationRepository).should().findById(anyLong());
        then(userRepository).should().findById(anyLong());
        then(eventUserRepository).should().save(any(EventUser.class));
        then(eventInvitationRepository).should().save(any(EventInvitation.class));
    }

    @Test
    void rejectInvitation() {
        //given
        EventInvitation eventInvitation = new EventInvitation();
        eventInvitation.setId(1L);
        eventInvitation.setUser(user1);
        eventInvitation.setEvent(event);
        eventInvitation.setAccessStatus(AccessStatus.PROCESSING);

        given(eventInvitationRepository.findById(1L)).willReturn(Optional.of(eventInvitation));
        given(eventInvitationRepository.save(any(EventInvitation.class))).willReturn(eventInvitation);

        //when
        EventInvitation result = service.rejectInvitation(1L, 1L);

        //then
        assertEquals("Rejected", result.getAccessStatus().toString());
        then(eventInvitationRepository).should().findById(anyLong());
        then(userRepository).should().findById(anyLong());
        then(eventInvitationRepository).should().save(any(EventInvitation.class));
    }
}