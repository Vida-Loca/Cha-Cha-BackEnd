package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.address.dto.AddressDto;
import com.vidaloca.skibidi.address.model.Address;
import com.vidaloca.skibidi.address.repository.AddressRepository;
import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.exception.model.UserActuallyInEventException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotAdminException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotInEventException;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.model.*;
import com.vidaloca.skibidi.event.type.EventType;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.exception.UsernameNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private EventUserRepository eventUserRepository;
    private AddressRepository addressRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            EventUserRepository eventUserRepository,
                            AddressRepository addressRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventUserRepository = eventUserRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(()-> new EventNotFoundException(id));
    }

    @Override
    public List<Event> findAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }

    @Override
    public Event addNewEvent(EventDto eventDto, Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        Event event = new Event();
        EventUser eu = new EventUser(user, event, true);
        event.setEventUsers(List.of(eu));
        return eventRepository.save(getEvent(eventDto, event));
    }

    @Override
    public Event updateEvent(EventDto eventDto, Long eventId, Long userId) throws UserIsNotAdminException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        if (!eu.isAdmin())
            throw new UserIsNotAdminException(userId);
        return eventRepository.save(getEvent(eventDto, event));
    }

    @Override
    public String deleteEvent(Long eventId, Long userId) throws UserIsNotAdminException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        if (!eu.isAdmin())
            throw new UserIsNotAdminException(userId);
        eventRepository.deleteById(eventId);
        return "Event deleted successfully";
    }

    @Override
    public List<User> findAllEventUsers(Long eventId, Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        List<EventUser> eventUsers = eventUserRepository.findAllByEvent(event);
        Optional<EventUser> eu = eventUserRepository.findByUserAndEvent(user,event);
        if (event.getEventType()!= EventType.PUBLIC && eu.isEmpty() )
            return null;
        return eventUsers.stream().map(EventUser::getUser).collect(Collectors.toList());
    }

//Only FOR ADMIN
    @Override
    public String deleteUser(Long eventId, Long userToDeleteId, Long userId) throws UserIsNotAdminException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User userToDelete = userRepository.findById(userToDeleteId).orElseThrow(() -> new UserNotFoundException(userToDeleteId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        if (!eu.isAdmin())
            throw new UserIsNotAdminException(userId);
        EventUser eu2 = eventUserRepository.findByUserAndEvent(userToDelete, event).orElseThrow(() -> new UserIsNotInEventException(userToDelete.getId(), event.getId()));
        eventUserRepository.delete(eu2);
        return "Successfully removed user from event";

    }

    @Override
    public String grantUserAdmin(Long eventId, Long userToGrantId, Long userId) throws UserIsNotAdminException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User userToGrant = userRepository.findById(userToGrantId).orElseThrow(() -> new UserNotFoundException(userToGrantId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        if (!eu.isAdmin())
            throw new UserIsNotAdminException(userId);
        EventUser eu2 = eventUserRepository.findByUserAndEvent(userToGrant, event).orElseThrow(() -> new UserIsNotInEventException(userToGrant.getId(), event.getId()));
        eu2.setAdmin(true);
        eventUserRepository.save(eu2);
        return "Successfully granted admin to " + userToGrant.getUsername();

    }

    @Override
    public boolean isCurrentUserAdminOfEvent(Long eventId , Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        return (eu.isAdmin());
    }

    private Address getAddress(AddressDto addressDto) {
        Address address = addressRepository.findByCountryAndCityAndPostcodeAndStreetAndNumber(addressDto.getCountry(),
                addressDto.getCity(), addressDto.getPostcode(), addressDto.getStreet(), addressDto.getNumber()).orElse(null);
        if (address != null)
            return address;
        return Address.AddressBuilder.anAddress().withCountry(addressDto.getCountry()).withCity(addressDto.getCity()).withPostcode(addressDto.getPostcode()).
                withStreet(addressDto.getStreet()).withNumber(addressDto.getNumber()).build();
    }

    private Event getEvent(EventDto eventDto, Event event) {
        event.setName(eventDto.getName());
        event.setStartTime(eventDto.getStartTime());
        event.setAddress(getAddress(eventDto.getAddress()));
        event.setStartTime(eventDto.getStartTime());
        event.setAdditionalInformation(eventDto.getAdditionalInformation());
        event.setEventType(eventDto.getEventType());
        return event;
    }
}
