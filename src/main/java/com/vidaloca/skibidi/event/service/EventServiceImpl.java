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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private EventUserRepository eventUserRepository;
    private AddressRepository addressRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            EventUserRepository eventUserRepository, AddressRepository addressRepository) {
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
        event.getEventUsers().add(eu);
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

    private Address getAddress(AddressDto addressDto) {
        Address address = addressRepository.findByCountryAndCityAndPostcodeAndStreetAndNumberAAndLatitudeAndLongitude(addressDto.getCountry(),
                addressDto.getCity(), addressDto.getPostcode(), addressDto.getStreet(), addressDto.getNumber(),addressDto.getLatitude(),addressDto.getLongitude()).orElse(null);
        if (address != null)
            return address;
        return Address.builder().country(addressDto.getCountry()).street(addressDto.getStreet()).postcode(addressDto.getPostcode()).
                number(addressDto.getNumber()).city(addressDto.getCity()).latitude(addressDto.getLatitude()).
                longitude(addressDto.getLongitude()).build();
    }

    private Event getEvent(EventDto eventDto, Event event) {
        event.setName(eventDto.getName());
        event.setStartTime(eventDto.getStartTime());
        event.setAddress(getAddress(eventDto.getAddress()));
        event.setStartTime(eventDto.getStartTime());
        event.setAdditionalInformation(eventDto.getAdditionalInformation());
        event.setEventType(eventDto.getEventType());
        event.setCurrency(eventDto.getCurrency());
        event.setOver(eventDto.isOver());
        return event;
    }
}
