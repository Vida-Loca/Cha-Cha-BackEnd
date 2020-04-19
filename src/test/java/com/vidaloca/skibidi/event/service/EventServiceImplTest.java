package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.address.dto.AddressDto;
import com.vidaloca.skibidi.address.model.Address;
import com.vidaloca.skibidi.address.repository.AddressRepository;
import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotAdminException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotInEventException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    EventRepository eventRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    EventUserRepository eventUserRepository;
    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    EventServiceImpl eventService;

    final Long EVENT_ID = 1L;
    final String EVENT_NAME = "Event Name";

    Event event;
    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        eventService = new EventServiceImpl(eventRepository, userRepository, eventUserRepository, addressRepository);

        event = new Event();
        event.setId(1L);

        user = new User();
        user.setId(1L);
    }

    @Test
    void findById() {
        //given
        event.setName(EVENT_NAME);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        //when
        Event returnedEvent = eventService.findById(EVENT_ID);

        //then
        assertNotNull(returnedEvent);
        assertEquals(event, returnedEvent);
        assertEquals(EVENT_ID, returnedEvent.getId());
        verify(eventRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void findByIdNotFound() {
        Exception exception = assertThrows(EventNotFoundException.class, () ->
                eventService.findById(EVENT_ID));

        assertEquals("Event with id: " + EVENT_ID + " not found", exception.getMessage());
        verify(eventRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void findAllEvents() {
        //given
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);

        when(eventRepository.findAll()).thenReturn(eventList);

        //when
        List<Event> returnedEventList = eventService.findAllEvents();

        //then
        assertEquals(1, returnedEventList.size());
        verify(eventRepository, times(1)).findAll();
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void addNewEvent() {
        //given
        AddressDto addressDto = new AddressDto("Country", "City",
                "Postcode", "Street", "Num");
        EventDto eventDto = new EventDto();
        eventDto.setName("EventName");
        eventDto.setStartTime(LocalDateTime.now());
        eventDto.setAddress(addressDto);
        eventDto.setAdditionalInformation("Info");

        event.setName(eventDto.getName());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        //when
        Event returnedEvent = eventService.addNewEvent(eventDto, 1L);

        //then
        assertEquals("EventName", returnedEvent.getName());
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).save(any(Event.class));
        verifyNoMoreInteractions(eventRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void addNewEventExistingAddress() {
        //given
        AddressDto addressDto = new AddressDto("Country", "City",
                "Postcode", "Street", "Num");
        EventDto eventDto = new EventDto();
        eventDto.setName("EventName");
        eventDto.setStartTime(LocalDateTime.now());
        eventDto.setAddress(addressDto);
        eventDto.setAdditionalInformation("Info");

        Address address = new Address();
        address.setCountry(addressDto.getCountry());
        address.setCity(addressDto.getCity());
        address.setPostcode(addressDto.getPostcode());
        address.setStreet(addressDto.getStreet());
        address.setNumber(addressDto.getNumber());
        Optional<Address> addressOptional = Optional.of(address);

        event.setName(eventDto.getName());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(addressRepository.findByCountryAndCityAndPostcodeAndStreetAndNumber(anyString(),
                anyString(), anyString(), anyString(), anyString())).thenReturn(addressOptional);

        //when
        Event returnedEvent = eventService.addNewEvent(eventDto, 1L);

        //then
        assertEquals("EventName", returnedEvent.getName());
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).save(any(Event.class));
        verifyNoMoreInteractions(eventRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void addNewEventUserNotFound() {
        //given
        AddressDto addressDto = new AddressDto("Country", "City",
                "Postcode", "Street", "Num");
        EventDto eventDto = new EventDto();
        eventDto.setName("EventName");
        eventDto.setStartTime(LocalDateTime.now());
        eventDto.setAddress(addressDto);
        eventDto.setAdditionalInformation("Info");

        //when
        Exception exception = assertThrows(UserNotFoundException.class, () ->
                eventService.addNewEvent(eventDto, 1L));

        //then
        assertEquals("User with id: " + 1L + " is not found", exception.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(0)).save(any(Event.class));
        verifyNoMoreInteractions(eventRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateEvent() throws UserIsNotAdminException {
        //given
        Long eventId = 1L;
        Long userId = 1L;
        AddressDto addressDto = new AddressDto("Country", "City",
                "Postcode", "Street", "Num");
        EventDto eventDto = new EventDto();
        eventDto.setName("EventName");
        eventDto.setStartTime(LocalDateTime.now());
        eventDto.setAddress(addressDto);
        eventDto.setAdditionalInformation("Info");

        EventUser eventUser = new EventUser();
        eventUser.setUser(user);
        eventUser.setAdmin(true);
        Optional<EventUser> optionalEventUser = Optional.of(eventUser);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(optionalEventUser);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        //when
        Event returnedEvent = eventService.updateEvent(eventDto, eventId, userId);

        //then
        assertNotNull(returnedEvent);
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(), any());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void updateEventNotAdmin() {
        //given
        Long eventId = 1L;
        Long userId = 1L;
        AddressDto addressDto = new AddressDto("Country", "City",
                "Postcode", "Street", "Num");
        EventDto eventDto = new EventDto();
        eventDto.setName("EventName");
        eventDto.setStartTime(LocalDateTime.now());
        eventDto.setAddress(addressDto);
        eventDto.setAdditionalInformation("Info");

        EventUser eventUser = new EventUser();
        eventUser.setUser(user);
        eventUser.setAdmin(false);
        Optional<EventUser> optionalEventUser = Optional.of(eventUser);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(optionalEventUser);

        //when
        Throwable exception = assertThrows(UserIsNotAdminException.class, () ->
                eventService.updateEvent(eventDto, eventId, userId));

        //then
        assertEquals("User with id: " + userId + " is not admin of that event.", exception.getMessage());
    }

    @Test
    void updateEventUserNotInEvent() {
        //given
        Long eventId = 1L;
        Long userId = 1L;
        AddressDto addressDto = new AddressDto("Country", "City",
                "Postcode", "Street", "Num");
        EventDto eventDto = new EventDto();
        eventDto.setName("EventName");
        eventDto.setStartTime(LocalDateTime.now());
        eventDto.setAddress(addressDto);
        eventDto.setAdditionalInformation("Info");

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));

        //when
        Throwable exception = assertThrows(UserIsNotInEventException.class, () ->
                eventService.updateEvent(eventDto, eventId, userId));

        //then
        assertEquals("User with id: 1 is not in event with id: 1 and cannot make this action", exception.getMessage());
    }

    @Test
    void updateEventNotExistingEvent() {
        //given
        Long eventId = 1L;
        Long userId = 1L;
        AddressDto addressDto = new AddressDto("Country", "City",
                "Postcode", "Street", "Num");
        EventDto eventDto = new EventDto();
        eventDto.setName("EventName");
        eventDto.setStartTime(LocalDateTime.now());
        eventDto.setAddress(addressDto);
        eventDto.setAdditionalInformation("Info");

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        //when
        Throwable exception = assertThrows(EventNotFoundException.class, () ->
                eventService.updateEvent(eventDto, eventId, userId));

        //then
        assertEquals("Event with id: " + eventId + " not found", exception.getMessage());
    }

    @Test
    void updateEventNotExistingUser() {
        //given
        Long eventId = 1L;
        Long userId = 1L;
        AddressDto addressDto = new AddressDto("Country", "City",
                "Postcode", "Street", "Num");
        EventDto eventDto = new EventDto();
        eventDto.setName("EventName");
        eventDto.setStartTime(LocalDateTime.now());
        eventDto.setAddress(addressDto);
        eventDto.setAdditionalInformation("Info");

        //when
        Throwable exception = assertThrows(UserNotFoundException.class, () ->
                eventService.updateEvent(eventDto, eventId, userId));

        //then
        assertEquals("User with id: " + userId + " is not found", exception.getMessage());
    }

//    @Test
//    void addUserToEvent() throws UserActuallyInEventException, UserIsNotAdminException {
//        //given
//        User admin = new User();
//        admin.setId(1L);
//        Optional<User> optionalAdmin = Optional.of(admin);
//
//        user.setUsername("username");
//
//        EventUser adminEventUser = new EventUser();
//        adminEventUser.setId(1L);
//        adminEventUser.setUser(admin);
//        adminEventUser.setAdmin(true);
//        Optional<EventUser> optionalAdminEventUser = Optional.of(adminEventUser);
//
//        EventUser addedEventUser = new EventUser();
//        addedEventUser.setId(2L);
//        addedEventUser.setUser(user);
//        addedEventUser.setAdmin(false);
//
//        when(userRepository.findById(anyLong())).thenReturn(optionalAdmin);
//        when(userRepository.findByUsername(anyString())).thenReturn(optionalUser);
//        when(eventRepository.findById(anyLong())).thenReturn(optionalEvent);
//        when(eventUserRepository.findByUserAndEvent(admin, event)).thenReturn(optionalAdminEventUser);
//        when(eventUserRepository.save(any(EventUser.class))).thenReturn(addedEventUser);
//
//        //when
//        EventUser returnedEventUser = eventService.addUserToEvent("username", 1L, 1L);
//
//        //then
//        assertNotNull(returnedEventUser);
//        assertEquals("username", returnedEventUser.getUser().getUsername());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(userRepository, times(1)).findByUsername(anyString());
//        verify(eventRepository, times(1)).findById(anyLong());
//        verify(eventUserRepository, times(2)).findByUserAndEvent(any(User.class), any(Event.class));
//        verify(eventUserRepository, times(1)).save(any(EventUser.class));
//    }
//
//    @Test
//    void addUserToEventUserAlreadyInEvent() {
//        //given
//        user.setId(2L);
//        user.setUsername("username");
//
//        User admin = new User();
//        admin.setId(1L);
//        Optional<User> optionalAdmin = Optional.of(admin);
//
//        EventUser adminEventUser = new EventUser();
//        adminEventUser.setId(1L);
//        adminEventUser.setUser(admin);
//        adminEventUser.setAdmin(true);
//        Optional<EventUser> optionalAdminEventUser = Optional.of(adminEventUser);
//
//        EventUser addedEventUser = new EventUser();
//        addedEventUser.setId(2L);
//        addedEventUser.setUser(user);
//        addedEventUser.setAdmin(false);
//        Optional<EventUser> optionalAddedEventUser = Optional.of(addedEventUser);
//
//        when(userRepository.findById(anyLong())).thenReturn(optionalAdmin);
//        when(userRepository.findByUsername(anyString())).thenReturn(optionalUser);
//        when(eventRepository.findById(anyLong())).thenReturn(optionalEvent);
//        when(eventUserRepository.findByUserAndEvent(admin, event)).thenReturn(optionalAdminEventUser);
//        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(optionalAddedEventUser);
//
//        //when
//        Throwable exception = assertThrows(UserActuallyInEventException.class, () ->
//                eventService.addUserToEvent("username", 1L, 1L));
//
//        //then
//        assertEquals("User: username is actually in that event", exception.getMessage());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(userRepository, times(1)).findByUsername(anyString());
//        verify(eventRepository, times(1)).findById(anyLong());
//        verify(eventUserRepository, times(2)).findByUserAndEvent(any(User.class), any(Event.class));
//        verify(eventUserRepository, times(0)).save(any(EventUser.class));
//    }
//
//    @Test
//    void addUserToEventNotAdmin() {
//        //given
//        user.setId(2L);
//        user.setUsername("username");
//
//        User admin = new User();
//        admin.setId(1L);
//        Optional<User> optionalAdmin = Optional.of(admin);
//
//
//        EventUser adminEventUser = new EventUser();
//        adminEventUser.setId(1L);
//        adminEventUser.setUser(admin);
//        adminEventUser.setAdmin(false);
//        Optional<EventUser> optionalAdminEventUser = Optional.of(adminEventUser);
//
//        when(userRepository.findById(anyLong())).thenReturn(optionalAdmin);
//        when(userRepository.findByUsername(anyString())).thenReturn(optionalUser);
//        when(eventRepository.findById(anyLong())).thenReturn(optionalEvent);
//        when(eventUserRepository.findByUserAndEvent(admin, event)).thenReturn(optionalAdminEventUser);
//
//        //when
//        Throwable exception = assertThrows(UserIsNotAdminException.class, () ->
//                eventService.addUserToEvent("username", 1L, 1L));
//
//        //then
//        assertEquals("User with id: " + admin.getId() + " is not admin of that event.", exception.getMessage());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(userRepository, times(1)).findByUsername(anyString());
//        verify(eventRepository, times(1)).findById(anyLong());
//        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
//        verify(eventUserRepository, times(0)).save(any(EventUser.class));
//    }
//
//    @Test
//    void addUserToEventNotInEvent() {
//        //given
//        user.setId(2L);
//        user.setUsername("username");
//
//        User admin = new User();
//        admin.setId(1L);
//        Optional<User> optionalAdmin = Optional.of(admin);
//
//        when(userRepository.findById(anyLong())).thenReturn(optionalAdmin);
//        when(userRepository.findByUsername(anyString())).thenReturn(optionalUser);
//        when(eventRepository.findById(anyLong())).thenReturn(optionalEvent);
//
//        //when
//        Throwable exception = assertThrows(UserIsNotInEventException.class, () ->
//                eventService.addUserToEvent("username", 1L, 1L));
//
//        //then
//        assertEquals("User with id: " + admin.getId() + " is not in event with id: " + event.getId() + " and cannot make this action", exception.getMessage());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(userRepository, times(1)).findByUsername(anyString());
//        verify(eventRepository, times(1)).findById(anyLong());
//        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
//        verify(eventUserRepository, times(0)).save(any(EventUser.class));
//    }
//
//    @Test
//    void addUserToEventNotExistingEvent() {
//        //given
//        user.setId(2L);
//        user.setUsername("username");
//
//        User admin = new User();
//        admin.setId(1L);
//        Optional<User> optionalAdmin = Optional.of(admin);
//
//        when(userRepository.findById(anyLong())).thenReturn(optionalAdmin);
//        when(userRepository.findByUsername(anyString())).thenReturn(optionalUser);
//
//        //when
//        Throwable exception = assertThrows(EventNotFoundException.class, () ->
//                eventService.addUserToEvent("username", 1L, 1L));
//
//        //then
//        assertEquals("Event with id: " + event.getId() + " not found", exception.getMessage());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(userRepository, times(1)).findByUsername(anyString());
//        verify(eventRepository, times(1)).findById(anyLong());
//        verify(eventUserRepository, times(0)).findByUserAndEvent(any(User.class), any(Event.class));
//        verify(eventUserRepository, times(0)).save(any(EventUser.class));
//    }
//
//    @Test
//    void addUserToEventNotExistingUsername() {
//        //given
//        user.setId(2L);
//        user.setUsername("username");
//
//        User admin = new User();
//        admin.setId(1L);
//        Optional<User> optionalAdmin = Optional.of(admin);
//
//        when(userRepository.findById(anyLong())).thenReturn(optionalAdmin);
//
//        //when
//        Throwable exception = assertThrows(UsernameNotFoundException.class, () ->
//                eventService.addUserToEvent("username", 1L, 1L));
//
//        //then
//        assertEquals("User with username: " + user.getUsername() + " is not found", exception.getMessage());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(userRepository, times(1)).findByUsername(anyString());
//        verify(eventRepository, times(0)).findById(anyLong());
//        verify(eventUserRepository, times(0)).findByUserAndEvent(any(User.class), any(Event.class));
//        verify(eventUserRepository, times(0)).save(any(EventUser.class));
//    }
//
//    @Test
//    void addUserToEventNotExistingUser() {
//        //given
//        user.setId(2L);
//        user.setUsername("username");
//
//        User admin = new User();
//        admin.setId(1L);
//
//        //when
//        Throwable exception = assertThrows(UserNotFoundException.class, () ->
//                eventService.addUserToEvent("username", 1L, 1L));
//
//        //then
//        assertEquals("User with id: " + admin.getId() + " is not found", exception.getMessage());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(userRepository, times(0)).findByUsername(anyString());
//        verify(eventRepository, times(0)).findById(anyLong());
//        verify(eventUserRepository, times(0)).findByUserAndEvent(any(User.class), any(Event.class));
//        verify(eventUserRepository, times(0)).save(any(EventUser.class));
//    }

    @Test
    void deleteEvent() throws UserIsNotAdminException {
        //given
        EventUser adminEventUser = new EventUser();
        adminEventUser.setId(event.getId());
        adminEventUser.setUser(user);
        adminEventUser.setAdmin(true);
        Optional<EventUser> optionalAdminEventUser = Optional.of(adminEventUser);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        when(eventUserRepository.findByUserAndEvent(any(User.class), any(Event.class))).thenReturn(optionalAdminEventUser);

        //when
        String result = eventService.deleteEvent(event.getId(), user.getId());

        //then
        assertEquals("Event deleted successfully", result);
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void deleteEventNotAdmin() {
        //given
        EventUser adminEventUser = new EventUser();
        adminEventUser.setId(event.getId());
        adminEventUser.setUser(user);
        adminEventUser.setAdmin(false);
        Optional<EventUser> optionalAdminEventUser = Optional.of(adminEventUser);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        when(eventUserRepository.findByUserAndEvent(any(User.class), any(Event.class))).thenReturn(optionalAdminEventUser);

        //when
        Throwable exception = assertThrows(UserIsNotAdminException.class, () ->
                eventService.deleteEvent(event.getId(), user.getId()));

        //then
        assertEquals("User with id: " + user.getId() + " is not admin of that event.", exception.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void deleteEventUserNotInEvent() {
        //given
        EventUser adminEventUser = new EventUser();
        adminEventUser.setId(event.getId());
        adminEventUser.setUser(user);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));

        //when
        Throwable exception = assertThrows(UserIsNotInEventException.class, () ->
                eventService.deleteEvent(event.getId(), user.getId()));

        //then
        assertEquals("User with id: " + user.getId() + " is not in event with id: " + event.getId() + " and cannot make this action",
                exception.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void deleteEventNotExistingEvent() {
        //given
        EventUser adminEventUser = new EventUser();
        adminEventUser.setId(event.getId());
        adminEventUser.setUser(user);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        //when
        Throwable exception = assertThrows(EventNotFoundException.class, () ->
                eventService.deleteEvent(event.getId(), user.getId()));

        //then
        assertEquals("Event with id: " + event.getId() + " not found", exception.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(0)).findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void deleteEventNotExistingUser() {
        //given
        EventUser adminEventUser = new EventUser();
        adminEventUser.setId(event.getId());
        adminEventUser.setUser(user);

        //when
        Throwable exception = assertThrows(UserNotFoundException.class, () ->
                eventService.deleteEvent(event.getId(), user.getId()));

        //then
        assertEquals("User with id: " + user.getId() + " is not found", exception.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(0)).findById(anyLong());
        verify(eventUserRepository, times(0)).findByUserAndEvent(any(User.class), any(Event.class));
    }

//    @Test
//    void findAllEventUsers() {
//        //given
//        User u1 = new User();
//        User u2 = new User();
//
//        EventUser eventUser1 = new EventUser();
//        eventUser1.setId(1L);
//        eventUser1.setUser(u1);
//        EventUser eventUser2 = new EventUser();
//        eventUser2.setId(2L);
//        eventUser2.setUser(u2);
//
//        List<EventUser> eventUsers = new ArrayList<>();
//        eventUsers.add(eventUser1);
//        eventUsers.add(eventUser2);
//
//        when(eventRepository.findById(anyLong())).thenReturn(optionalEvent);
//        when(eventUserRepository.findAllByEvent(event)).thenReturn(eventUsers);
//
//        //when
//        List<User> returnedList = eventService.findAllEventUsers(event.getId());
//
//        //then
//        assertNotNull(returnedList);
//        assertEquals(2, returnedList.size());
//        verify(eventRepository, times(1)).findById(anyLong());
//        verify(eventUserRepository, times(1)).findAllByEvent(any(Event.class));
//    }

    @Test
    void deleteUser() throws UserIsNotAdminException {
        //given
        User userToDel = new User();
        userToDel.setId(2L);

        EventUser eventUser1 = new EventUser();
        eventUser1.setId(1L);
        eventUser1.setUser(user);
        eventUser1.setAdmin(true);
        EventUser eventUser2 = new EventUser();
        eventUser2.setId(2L);
        eventUser2.setUser(userToDel);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.ofNullable(event));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userToDel));
        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(eventUser1));
        when(eventUserRepository.findByUserAndEvent(userToDel, event)).thenReturn(Optional.of(eventUser2));

        //when
        String result = eventService.deleteUser(1L, 2L, 1L);

        //then
        assertEquals("Successfully removed user from event", result);
        then(userRepository).should(times(2)).findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should(times(2)).findByUserAndEvent(any(User.class), any(Event.class));
        then(eventUserRepository).should().delete(any(EventUser.class));
    }

    @Test
    void deleteUserNotAdmin() {
        //given
        User userToDel = new User();
        userToDel.setId(2L);
        Optional<User> optional = Optional.of(userToDel);

        EventUser eventUser1 = new EventUser();
        eventUser1.setId(1L);
        eventUser1.setUser(user);
        eventUser1.setAdmin(false);
        EventUser eventUser2 = new EventUser();
        eventUser2.setId(2L);
        eventUser2.setUser(userToDel);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.ofNullable(event));
        when(userRepository.findById(2L)).thenReturn(optional);
        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(eventUser1));

        //when
        Throwable exception = assertThrows(UserIsNotAdminException.class, () ->
                eventService.deleteUser(event.getId(), userToDel.getId(), user.getId()));

        //then
        assertEquals("User with id: " + user.getId() + " is not admin of that event.", exception.getMessage());
        verify(userRepository, times(2)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
        verify(eventUserRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void grantUserAdmin() throws UserIsNotAdminException {
        //given
        User userToGrant = new User();
        userToGrant.setId(2L);
        userToGrant.setUsername("grant");
        Optional<User> optional = Optional.of(userToGrant);

        EventUser eventUser1 = new EventUser();
        eventUser1.setId(1L);
        eventUser1.setUser(user);
        eventUser1.setAdmin(true);
        EventUser eventUser2 = new EventUser();
        eventUser2.setId(2L);
        eventUser2.setUser(userToGrant);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.ofNullable(event));
        when(userRepository.findById(2L)).thenReturn(optional);
        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(eventUser1));
        when(eventUserRepository.findByUserAndEvent(userToGrant, event)).thenReturn(Optional.of(eventUser2));

        //when
        String result = eventService.grantUserAdmin(event.getId(), userToGrant.getId(), user.getId());

        //then
        assertEquals("Successfully granted admin to " + userToGrant.getUsername(), result);
        assertTrue(eventUser2.isAdmin());
        verify(userRepository, times(2)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(2)).findByUserAndEvent(any(User.class), any(Event.class));
        verify(eventUserRepository, times(1)).save(any(EventUser.class));
    }

    @Test
    void grantUserAdminNotAdmin() {
        //given
        User userToGrant = new User();
        userToGrant.setId(2L);
        userToGrant.setUsername("grant");
        Optional<User> optional = Optional.of(userToGrant);

        EventUser eventUser1 = new EventUser();
        eventUser1.setId(1L);
        eventUser1.setUser(user);
        eventUser1.setAdmin(false);
        EventUser eventUser2 = new EventUser();
        eventUser2.setId(2L);
        eventUser2.setUser(userToGrant);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.ofNullable(event));
        when(userRepository.findById(2L)).thenReturn(optional);
        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(eventUser1));

        //when
        Throwable exception = assertThrows(UserIsNotAdminException.class, () ->
                eventService.grantUserAdmin(event.getId(), userToGrant.getId(), user.getId()));

        //then
        assertEquals("User with id: " + user.getId() + " is not admin of that event.", exception.getMessage());
        verify(userRepository, times(2)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
        verify(eventUserRepository, times(0)).save(any(EventUser.class));
    }

    @Test
    void isCurrentUserAdminOfEvent() {
        //given
        EventUser eventUser = new EventUser();
        eventUser.setId(1L);
        eventUser.setUser(user);
        eventUser.setAdmin(true);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.ofNullable(event));
        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(eventUser));

        //when
        boolean result = eventService.isCurrentUserAdminOfEvent(event.getId(), user.getId());

        //then
        assertTrue(result);
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
    }
}