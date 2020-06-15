package com.vidaloca.skibidi.unit.event.service;

import com.vidaloca.skibidi.event.access.repository.EventInvitationRepository;
import com.vidaloca.skibidi.event.exception.model.LastAdminException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotAdminException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.service.EventUserServiceImpl;
import com.vidaloca.skibidi.event.type.EventType;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventUserServiceImplTest {

    @Mock(lenient = true)
    UserRepository userRepository;
    @Mock
    EventRepository eventRepository;
    @Mock
    EventUserRepository eventUserRepository;
    @Mock
    EventInvitationRepository eventInvitationRepository;

    @InjectMocks
    EventUserServiceImpl service;

    User admin;
    User user;
    Event event;

    @BeforeEach
    void setUp() {
        admin = new User();
        admin.setId(1L);
        admin.setUsername("admin");
        user = new User();
        user.setId(2L);
        user.setUsername("user");
        event = new Event();
        event.setId(1L);
        event.setName("Event");

        given(userRepository.findById(1L)).willReturn(Optional.of(admin));
        given(userRepository.findById(2L)).willReturn(Optional.of(user));
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
    }

    @Test
    void findAllEventUsers() {
        //given
        event.setEventType(EventType.PUBLIC);
        List<EventUser> list = new ArrayList<>();
        EventUser eu = new EventUser();
        list.add(eu);

        given(eventUserRepository.findAllByEvent(event)).willReturn(list);
        given(eventUserRepository.findByUserAndEvent(admin, event)).willReturn(Optional.of(eu));

        //when
        List<User> result = service.findAllEventUsers(1L, 1L);

        //then
        assertEquals(1, result.size());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findAllByEvent(any(Event.class));
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void findAllEventUsersNotAllowedToSee() {
        //given
        event.setEventType(EventType.PRIVATE);
        List<EventUser> list = new ArrayList<>();
        EventUser eu = new EventUser();
        list.add(eu);

        given(eventUserRepository.findAllByEvent(event)).willReturn(list);
        given(eventUserRepository.findByUserAndEvent(admin, event)).willReturn(Optional.empty());

        //when
        List<User> result = service.findAllEventUsers(1L, 1L);

        //then
        assertNull(result);
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findAllByEvent(any(Event.class));
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void deleteUser() throws UserIsNotAdminException {
        //given
        EventUser eventUser = new EventUser();
        eventUser.setAdmin(true);
        eventUser.setEvent(event);
        eventUser.setUser(admin);
        eventUser.setId(1L);
        EventUser eu = new EventUser();

        given(eventUserRepository.findByUserAndEvent(admin, event)).willReturn(Optional.of(eventUser));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eu));

        //when
        String result = service.deleteUser(1L, 2L, 1L);

        //then
        assertEquals("Successfully removed user from event", result);
        then(userRepository).should(times(2)).findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should(times(2)).findByUserAndEvent(any(User.class), any(Event.class));
        then(eventUserRepository).should().delete(any(EventUser.class));
        then(eventInvitationRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
    }


    @Test
    void deleteUserNotAdmin() throws UserIsNotAdminException {
        //given
        EventUser eventUser = new EventUser();
        eventUser.setAdmin(false);
        eventUser.setEvent(event);
        eventUser.setUser(admin);
        eventUser.setId(1L);

        given(eventUserRepository.findByUserAndEvent(admin, event)).willReturn(Optional.of(eventUser));

        //when
        Throwable result = assertThrows(UserIsNotAdminException.class, () -> {
            service.deleteUser(1L, 2L, 1L);
        });

        //then
        assertEquals("User with id: 1 is not admin of that event.", result.getMessage());
        then(userRepository).should(times(2)).findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(eventUserRepository).shouldHaveNoMoreInteractions();
        then(eventInvitationRepository).shouldHaveNoInteractions();
    }

    @Test
    void grantUserAdmin() throws UserIsNotAdminException {
        //given
        user.setUsername("USER");
        EventUser eventUser = new EventUser();
        eventUser.setAdmin(true);
        eventUser.setEvent(event);
        eventUser.setUser(admin);
        eventUser.setId(1L);
        EventUser eu = new EventUser();

        given(eventUserRepository.findByUserAndEvent(admin, event)).willReturn(Optional.of(eventUser));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eu));

        //when
        String result = service.grantUserAdmin(1L, 2L, 1L);

        //then
        assertEquals("Successfully granted admin to USER", result);
        then(userRepository).should(times(2)).findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should(times(2)).findByUserAndEvent(any(User.class), any(Event.class));
        then(eventUserRepository).should().save(any(EventUser.class));
    }

    @Test
    void grantUserAdminNotAdmin() throws UserIsNotAdminException {
        //given
        user.setUsername("USER");
        EventUser eventUser = new EventUser();
        eventUser.setAdmin(false);
        eventUser.setEvent(event);
        eventUser.setUser(admin);
        eventUser.setId(1L);

        given(eventUserRepository.findByUserAndEvent(admin, event)).willReturn(Optional.of(eventUser));

        //when
        Throwable result = assertThrows(UserIsNotAdminException.class, () ->
                service.grantUserAdmin(1L, 2L, 1L));

        //then
        assertEquals("User with id: 1 is not admin of that event.", result.getMessage());
        then(userRepository).should(times(2)).findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(eventUserRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void isCurrentUserAdminOfEvent() {
        //given
        EventUser eventUser = new EventUser();
        eventUser.setId(1L);
        eventUser.setUser(user);
        eventUser.setEvent(event);
        eventUser.setAdmin(true);

        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(eventUser));

        //when
        boolean result = service.isCurrentUserAdminOfEvent(event.getId(), user.getId());

        //then
        assertTrue(result);
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void leaveEvent() {
        //given
        EventUser euu = new EventUser();
        euu.setId(3L);
        euu.setUser(user);
        euu.setEvent(event);
        euu.setAdmin(false);

        EventUser eua = new EventUser();
        eua.setId(4L);
        eua.setUser(admin);
        eua.setEvent(event);
        eua.setAdmin(true);

        event.getEventUsers().add(euu);
        event.getEventUsers().add(eua);

        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(euu));

        //when
        boolean result = service.leaveEvent(1L, 2L);

        //then
        assertTrue(result);
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(eventRepository).should().save(any(Event.class));
        then(eventUserRepository).should().delete(any(EventUser.class));
        then(eventInvitationRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(eventInvitationRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void leaveEventLastAdmin() {
        //given
        EventUser a1 = new EventUser();
        a1.setEvent(event);
        a1.setUser(admin);
        a1.setAdmin(false);

        EventUser eventUser = new EventUser();
        eventUser.setId(1L);
        eventUser.setUser(user);
        eventUser.setEvent(event);
        eventUser.setAdmin(true);

        event.getEventUsers().add(a1);
        event.getEventUsers().add(eventUser);

        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));

        //when
        Exception result = assertThrows(LastAdminException.class, () -> {
            service.leaveEvent(1L, 2L);
        });

        //then
        assertEquals("You are the last admin in this event. Before leaving give admin to another user.",
                result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(eventRepository).shouldHaveNoMoreInteractions();
        then(eventUserRepository).shouldHaveNoMoreInteractions();
        then(eventInvitationRepository).shouldHaveNoInteractions();
    }

    @Test
    void findAllEventAdmins() {
        //given
        EventUser a1 = new EventUser();
        a1.setEvent(event);
        a1.setUser(admin);
        a1.setAdmin(true);

        EventUser eventUser = new EventUser();
        eventUser.setId(1L);
        eventUser.setUser(user);
        eventUser.setEvent(event);
        eventUser.setAdmin(true);

        event.getEventUsers().add(a1);
        event.getEventUsers().add(eventUser);

        given(eventUserRepository.findByUserAndEvent(admin, event)).willReturn(Optional.of(a1));

        //when
        List<User> result = service.findAllEventAdmins(1L, 1L);

        //then
        assertEquals(2, result.size());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
    }

}