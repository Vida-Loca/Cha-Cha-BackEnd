package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.Event_UserRepository;
import com.vidaloca.skibidi.event.repository.UserCardRepository;
import com.vidaloca.skibidi.model.*;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private Event_UserRepository event_userRepository;
    private UserCardRepository userCardRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            Event_UserRepository event_userRepository, UserCardRepository userCardRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.event_userRepository = event_userRepository;
        this.userCardRepository = userCardRepository;
    }

    @Override
    public String addNewEvent(EventDto eventDto, Long currentUserId) {
        Event event = new Event();
        User user = userRepository.findById(currentUserId).orElse(null);
        event.setAddress(new Address());
        Event finalEvent = getEvent(eventDto, event);
        eventRepository.save(finalEvent);
        Event_User eu = new Event_User(user, event, true);
        event_userRepository.save(eu);
        return "Successfully added event";
    }

    @Override
    public String updateEvent(EventDto eventDto, Integer id, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Event event = eventRepository.findById(id).orElse(null);
        if (user == null)
            return "Unexpected failure";
        if (event == null)
            return "Event doesn't exist";
        Event_User eu = event_userRepository.findByUserAndEvent(user, event);
        if (!eu.isAdmin())
            return "User is not allowed to update event";
        eventRepository.save(getEvent(eventDto, event));
        return "Updated successfully";
    }

    @Override
    public String addProductToEvent(Product product, Integer eventId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return "Unexpected failure";
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return "Event doesn't exist";
        Event_User eu = event_userRepository.findByUserAndEvent(user, event);
        UserCard uc = new UserCard();
        uc.setEvent_user(eu);
        uc.setProduct(product);
        userCardRepository.save(uc);
        event.getProducts().add(product);
        eventRepository.save(event);
        return "Successfully added product";
    }

    @Override
    public String addUserToEvent(String username, Integer eventId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return "Unexpected failure";
        User addingUser = userRepository.findByUsername(username);
        if (addingUser == null)
            return "User doesn't exists";
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return "Event doesn't exist";
        Event_User eu = event_userRepository.findByUserAndEvent(user, event);
        if (!eu.isAdmin())
            return "User don't have permission to add new user to event";
        Event_User euAdd = new Event_User(addingUser, event, false);
        event_userRepository.save(euAdd);
        return "Successfully added user";
    }

    @Override
    public String deleteEvent(Integer id, Long user_id) {
        User user = userRepository.findById(user_id).orElse(null);
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null)
            return "Event doesn't exist";
        Event_User eu = event_userRepository.findByUserAndEvent(user, event);
        if (!eu.isAdmin())
            return "User don't have permission to delete event";
        eventRepository.deleteById(id);
        return "Event delete successfully";
    }

    private Event getEvent(EventDto eventDto, Event event) {
        event.setName(eventDto.getName());
        event.setStartDate(eventDto.getStartDate());
        event.getAddress().setCity(eventDto.getAddress().getCity());
        event.getAddress().setCountry(eventDto.getAddress().getCountry());
        event.getAddress().setNumber(eventDto.getAddress().getNumber());
        event.getAddress().setStreet(eventDto.getAddress().getStreet());
        event.getAddress().setPostcode(eventDto.getAddress().getPostcode());
        return event;
    }
}
