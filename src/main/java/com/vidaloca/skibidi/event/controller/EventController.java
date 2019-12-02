package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
public class EventController {
    private EventRepository eventRepository;
    private EventService eventService;
    @Autowired
    public EventController( EventRepository eventRepository, EventService eventService) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    @GetMapping("/event")
    public List<Event> getEvents(){
        return (List<Event>) eventRepository.findAll();
    }

    @GetMapping ("/event/{id}")
    public Event getEventById(@PathVariable Integer id){
        return eventRepository.findById(id).orElse(null);
    }
    @PostMapping("/event")
    public String addNewEvent(@RequestBody EventDto eventDto){
        Event event = eventService.addNewEvent(eventDto);
        eventRepository.save(event);
        return "successfully added event";
    }
    @PutMapping("/event/{id}")
    public String updateEvent(@RequestBody EventDto eventDto, @PathVariable Integer id){
        Event event = eventService.updateEvent(eventDto,id);
        eventRepository.save(event);
        return "successfully updated event";
    }
    @DeleteMapping("/event/{id}")
    public String deleteById(@PathVariable Integer id){
        eventRepository.deleteById(id);
        return "delete successfully";
    }
}
