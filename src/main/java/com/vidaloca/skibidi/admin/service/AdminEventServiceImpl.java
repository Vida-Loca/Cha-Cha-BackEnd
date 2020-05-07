package com.vidaloca.skibidi.admin.service;

import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminEventServiceImpl implements AdminEventService {

    EventRepository eventRepository;

    @Autowired
    public AdminEventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
    public String deleteById(Long eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        eventRepository.deleteById(eventId);
        return "Event deleted successfully";
    }
}
