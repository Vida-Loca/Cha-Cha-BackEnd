package com.vidaloca.skibidi.admin.controller;

import com.vidaloca.skibidi.admin.service.AdminEventService;
import com.vidaloca.skibidi.event.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminEventController {

    private AdminEventService eventService;

    @Autowired
    public AdminEventController(AdminEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/getAllEvents")
    public List<Event> getAllEvents() {
        return eventService.findAllEvents();
    }

    @GetMapping("/event/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @DeleteMapping("/event/{id}")
    public String deleteById(@PathVariable Long id) {
        return eventService.deleteById(id);
    }

}
