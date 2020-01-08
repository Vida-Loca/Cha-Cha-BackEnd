package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.Event_UserRepository;
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

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, Event_UserRepository event_userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.event_userRepository = event_userRepository;
    }

    @Override
    public Event addNewEvent(EventDto eventDto, Long currentUserId) {
        Event event = new Event();
        User user = userRepository.findById(currentUserId).orElse(null);
        event.setAddress(new Address());
        Event finalEvent = getEvent(eventDto, event);
        eventRepository.save(finalEvent);
        Event_User eu = new Event_User(user,event,true);
        event_userRepository.save(eu);
        return  finalEvent;
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
        if (event.getProducts()==null){
            System.out.println("\n\n\nNULL\n\n\n");
            List<Product> products = new ArrayList<>();
            event.setProducts(products);
        }
        event.getProducts().add(product);
        eventRepository.save(event);
        return event;
    }

    @Override
    public Event addUserToEvent(User user, Integer eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return  null;
        Event_User eu  = new Event_User(user,event,false);
        event_userRepository.save(eu);
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
        return event;
    }
}
