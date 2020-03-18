package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.exception.model.UserActuallyInEventException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotAdminException;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class EventController {

    private EventService eventService;


    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
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

    //This going to product controller
/*
    @GetMapping("/event/{id}/product")
    public List<Product> getEventProducts(@PathVariable Integer id) {
        return eventService.findAllEventProducts(id);
    }

    @CrossOrigin
    @PostMapping("/event/{id}/productNew")
    public GenericResponse addProductToEvent(@Valid @RequestBody ProductDto productDto, @PathVariable Integer id,
                                    HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        Product p = productService.addProduct(productDto);
        if (p==null)
            return new GenericResponse("Cant't add product");
        return new GenericResponse(eventService.addProductToEvent(p, id, currentUserId));
    }


        @GetMapping("/event/{eventId}/user")
    public List<User> getEventUsers (@PathVariable Long eventId) {
        return eventService.findAllEventUsers(eventId);
    }
        @DeleteMapping("/event/{id}/product")
    public GenericResponse deleteProductFromEvent (@PathVariable Integer id,@RequestParam("productToDeleteId") Integer productToDeleteId, HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        return new GenericResponse(eventService.deleteProduct(id,productToDeleteId,currentUserId));
    }

*/
    @CrossOrigin
    @PostMapping("/event/{eventId}/user")
    public EventUser addUserToEvent(@RequestParam("username") String username, @PathVariable Long eventId, HttpServletRequest request) throws UserActuallyInEventException, UserIsNotAdminException {
        return eventService.addUserToEvent(username, eventId, CurrentUser.currentUserId(request));
    }

    @DeleteMapping("/event/{eventId}/user")
    public String deleteUserFromEvent(@PathVariable Long eventId, @RequestParam("userToDeleteId") Long userToDeleteId, HttpServletRequest request) throws UserIsNotAdminException {
        return eventService.deleteUser(eventId, userToDeleteId, CurrentUser.currentUserId(request));
    }

    @PutMapping("/event/{eventId}/user/{userId}/grantAdmin")
    public String grantAdminForUser(@PathVariable Long eventId, @PathVariable Long userId, HttpServletRequest request) throws UserIsNotAdminException {
        return eventService.grantUserAdmin(eventId, userId, CurrentUser.currentUserId(request));
    }

    @GetMapping("/event/{eventId}/isAdmin")
    public boolean isAdmin(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
        return eventService.isCurrentUserAdminOfEvent(eventId, CurrentUser.currentUserId(request));
    }

}
