package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.model.Address;
import com.vidaloca.skibidi.model.Event;
import com.vidaloca.skibidi.model.Product;
import com.vidaloca.skibidi.model.User;
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

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }



    @Override
    public Event addNewEvent(EventDto eventDto, Long currentUserId) {
        Event event = new Event();
        User user = userRepository.findById(currentUserId).orElse(null);
        if (user== null)
            System.out.println(currentUserId+"\n \n");
        event.setUsers(new ArrayList<>());
        event.getUsers().add(user);
        event.setAddress(new Address());
        return getEvent(eventDto, event);
    }

    @Override
    public Event updateEvent(EventDto eventDto, Integer id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            return getEvent(eventDto, event);
        }
        return null;
    }

    @Override
    public Event addProductToEvent(Product product, Integer eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return  null;
        event.getProducts().add(product);
        eventRepository.save(event);
        return event;
    }

    @Override
    public Event addUserToEvent(User user, Integer eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return  null;
        event.getUsers().add(user);
        eventRepository.save(event);
        return event;
    }

    private Event getEvent(EventDto eventDto, Event event) {
        event.setName(eventDto.getName());
        event.setStartDate(eventDto.getStartDate());
        event.getAddress().setCity(eventDto.getAddress().getCity());
        event.getAddress().setCountry(eventDto.getAddress().getCountry());
        event.getAddress().setNumber(eventDto.getAddress().getNumber());
        event.getAddress().setStreet(eventDto.getAddress().getStreet());
        event.getAddress().setPostcode(eventDto.getAddress().getPostcode());
        return eventRepository.save(event);
    }
}
