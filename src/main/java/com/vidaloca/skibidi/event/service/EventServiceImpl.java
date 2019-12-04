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
        event.setName(eventDto.getName());
        event.setStartDate(eventDto.getStartDate());
        event.setAddress(eventDto.getAddress());
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(EventDto eventDto, Integer id) {
        Event event = eventRepository.findById(id).orElse(null);
        event.setName(eventDto.getName());
        event.setStartDate(eventDto.getStartDate());
        event.setAddress(eventDto.getAddress());
        return eventRepository.save(event);
    }
}
