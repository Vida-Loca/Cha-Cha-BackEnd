package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.dto.ProductDto;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.Event_UserRepository;
import com.vidaloca.skibidi.event.repository.ProductRepository;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.event.service.ProductService;
import com.vidaloca.skibidi.model.Event;
import com.vidaloca.skibidi.model.Event_User;
import com.vidaloca.skibidi.model.Product;
import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import com.vidaloca.skibidi.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;
import java.util.List;

@RestController
public class EventController {

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenProvider jwtTokenProvider;
    private EventRepository eventRepository;
    private EventService eventService;
    private ProductService productService;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private Event_UserRepository event_userRepository;

    @Autowired
    public EventController(JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider,
                           EventRepository eventRepository, EventService eventService,
                           ProductService productService, ProductRepository productRepository,
                           UserRepository userRepository, Event_UserRepository event_userRepository) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.productService = productService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.event_userRepository = event_userRepository;
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
    public String addNewEvent(@Valid @RequestBody EventDto eventDto,HttpServletRequest request){
        Long currentUserId = currentUserId(request);
        Event event = eventService.addNewEvent(eventDto, currentUserId);
        return "successfully added event";
    }
    @PutMapping("/event/{id}")
    public String updateEvent(@Valid @RequestBody EventDto eventDto, @PathVariable Integer id){
        Event event = eventService.updateEvent(eventDto,id);
        return "successfully updated event";
    }
    @DeleteMapping("/event/{id}")
    public String deleteById(@PathVariable Integer id,HttpServletRequest request){
        Long currentUserId = currentUserId(request);
        User user = userRepository.findById(currentUserId).orElse(null);
        Event event = eventRepository.findById(id).orElse(null);
        if (user == null )
            return "Delete failure";
        if (event == null)
            return "This event not exist";
        Event_User eu = event_userRepository.findByUserAndEvent(user,event);
        if (eu.isAdmin()) {
            eventRepository.deleteById(id);
            return "delete successfully";
        }
        else
            return "user is not allowed to delete event";
    }
    @GetMapping ("/event/{id}/products")
    public List<Product> getEventProducts(@PathVariable Integer id){
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null)
            return null;
        return event.getProducts();
    }
    @PostMapping ("/event/productsNew")
    public String addProductToEvent(@Valid @RequestBody ProductDto productDto, @RequestParam(required = false)  Integer eventId) {
        Product p = productService.addProduct(productDto);
        eventService.addProductToEvent(p,eventId);
        return "Successfully added product";
    }
    @PostMapping ("/event/products")
    public String addProductToEvent(@RequestParam Integer productId, @RequestParam Integer eventId){
        productRepository.findById(productId).ifPresent(p -> eventService.addProductToEvent(p, eventId));
        return "Successfully added existing product to event";
    }
    @PostMapping ("/event/user")
    public String addUserToEvent(@RequestParam String username, @RequestParam Integer eventId){
        User user = userRepository.findByUsername(username);
        eventService.addUserToEvent(user,eventId);
        return "Successfully added user to event";
    }
    private Long currentUserId(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getJWTFromRequest(request);
        return jwtTokenProvider.getUserIdFromJWT(token);
    }
}
