package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.exception.model.UserIsNotAdminException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.event.service.EventUserService;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import com.vidaloca.skibidi.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class EventController {

    private EventService eventService;
    private EventUserService eventUserService;


    @Autowired
    public EventController(EventService eventService, EventUserService eventUserService) {
        this.eventService = eventService;
        this.eventUserService = eventUserService;
    }

    @GetMapping("/event/{eventId}/users")
    public List<User> findAllEventUsers(@PathVariable Long eventId, HttpServletRequest request){
        return eventUserService.findAllEventUsers(eventId,CurrentUser.currentUserId(request));
    }

    @GetMapping("/event")
    public List<Event> getEvents() {
        return (List<Event>) eventService.findAllEvents();
    }

    @GetMapping("/event/{eventId}")
    public Event getEventById(@PathVariable Long eventId) {
        return eventService.findById(eventId);
    }

    @CrossOrigin
    @PostMapping("/event")
    public Event addNewEvent(@Valid @RequestBody EventDto eventDto, HttpServletRequest request) {
        return eventService.addNewEvent(eventDto, CurrentUser.currentUserId(request));
    }

    @CrossOrigin
    @PutMapping("/event/{eventId}")
    public Event updateEvent(@Valid @RequestBody EventDto eventDto, @PathVariable Long eventId, HttpServletRequest request) throws UserIsNotAdminException {
        return eventService.updateEvent(eventDto, eventId, CurrentUser.currentUserId(request));
    }

    @DeleteMapping("/event/{eventId}")
    public String deleteById(@PathVariable Long eventId, HttpServletRequest request) throws UserIsNotAdminException {
        return eventService.deleteEvent(eventId, CurrentUser.currentUserId(request));
    }

    @DeleteMapping("/event/{eventId}/leave")
    public boolean leaveEvent(@PathVariable Long eventId, HttpServletRequest request){
        return eventUserService.leaveEvent(eventId,CurrentUser.currentUserId(request));
    }

    @DeleteMapping("/event/{eventId}/user")
    public String deleteUserFromEvent(@PathVariable Long eventId, @RequestParam Long userToDeleteId, HttpServletRequest request) throws UserIsNotAdminException {
        return eventUserService.deleteUser(eventId, userToDeleteId, CurrentUser.currentUserId(request));
    }

    @PutMapping("/event/{eventId}/user/{userId}/grantAdmin")
    public String grantAdminForUser(@PathVariable Long eventId, @PathVariable Long userId, HttpServletRequest request) throws UserIsNotAdminException {
        return eventUserService.grantUserAdmin(eventId, userId, CurrentUser.currentUserId(request));
    }

    @GetMapping("/event/{eventId}/isAdmin")
    public boolean isAdmin(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
        return eventUserService.isCurrentUserAdminOfEvent(eventId, CurrentUser.currentUserId(request));
    }
    @GetMapping("/event/{eventId}/admin")
    public List<User> findAllEventAdmins(@PathVariable Long eventId, HttpServletRequest request){
        return eventUserService.findAllEventAdmins(eventId,CurrentUser.currentUserId(request));
    }

}
