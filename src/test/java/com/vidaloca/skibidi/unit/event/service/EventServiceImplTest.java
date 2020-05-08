package com.vidaloca.skibidi.unit.event.service;

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
import com.vidaloca.skibidi.event.service.EventServiceImpl;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
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

        assertEquals("Event with id: " + EVENT_ID + " not found.", exception.getMessage());
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
                "Postcode", "Street", "Num", 10f, 10f);
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
        when(addressRepository.findByCountryAndCityAndPostcodeAndStreetAndNumberAndLatitudeAndLongitude(anyString(),
                anyString(), anyString(), anyString(), anyString(),anyFloat(),anyFloat())).thenReturn(addressOptional);
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
        assertEquals("User with id: 1 is not in event with id: 1 and cannot make this action.", exception.getMessage());
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
        assertEquals("Event with id: " + eventId + " not found.", exception.getMessage());
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
        assertEquals("User with id: " + user.getId() + " is not in event with id: " + event.getId() + " and cannot make this action.",
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
        assertEquals("Event with id: " + event.getId() + " not found.", exception.getMessage());
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

}