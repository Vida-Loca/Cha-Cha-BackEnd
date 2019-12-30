package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
    public Event addNewEvent(EventDto eventDto) {
        Event event = new Event();
        return getEvent(eventDto, event);
    }

    @Override
    public Event updateEvent(EventDto eventDto, Integer id) {
        Event event = eventRepository.findById(id).orElse(null);
        return getEvent(eventDto, event);
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
