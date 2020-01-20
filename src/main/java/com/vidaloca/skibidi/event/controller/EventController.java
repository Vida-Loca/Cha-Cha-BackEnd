package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.dto.ProductDto;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.repository.ProductRepository;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.event.service.ProductService;
import com.vidaloca.skibidi.model.Event;
import com.vidaloca.skibidi.model.Product;
import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import com.vidaloca.skibidi.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    private EventUserRepository event_userRepository;

    @Autowired
    public EventController(JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider,
                           EventRepository eventRepository, EventService eventService,
                           ProductService productService, ProductRepository productRepository,
                           UserRepository userRepository, EventUserRepository event_userRepository) {
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
    public List<Event> getEvents() {
        return (List<Event>) eventRepository.findAll();
    }

    @GetMapping("/event/{id}")
    public Event getEventById(@PathVariable Integer id) {
        return eventRepository.findById(id).orElse(null);
    }

    @CrossOrigin
    @PostMapping("/event")
    public Event addNewEvent(@Valid @RequestBody EventDto eventDto, HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        eventService.addNewEvent(eventDto,currentUserId);
        return new Event();
    }

    @CrossOrigin
    @PutMapping("/event/{id}")
    public String updateEvent(@Valid @RequestBody EventDto eventDto, @PathVariable Integer id, HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        return eventService.updateEvent(eventDto, id, currentUserId);
    }

    @DeleteMapping("/event/{id}")
    public String deleteById(@PathVariable Integer id, HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        return eventService.deleteEvent(id, currentUserId);
    }

    @GetMapping("/event/{id}/product")
    public List<Product> getEventProducts(@PathVariable Integer id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null)
            return null;
        return event.getProducts();
    }

    @CrossOrigin
    @PostMapping("/event/{id}/productNew")
    public String addProductToEvent(@Valid @RequestBody ProductDto productDto, @PathVariable Integer id,
                                    HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        Product p = productService.addProduct(productDto);
        return eventService.addProductToEvent(p, id, currentUserId);
    }

    @CrossOrigin
    @PostMapping("/event/{id}/product")
    public String addProductToEvent(@RequestParam Integer productId, @PathVariable Integer id, HttpServletRequest request) {
        Long currentUserId = currentUserId(request);

        productRepository.findById(productId).ifPresent(p ->  eventService.addProductToEvent(p, id, currentUserId));
        return "Successfully added existing product to event";
    }

    @CrossOrigin
    @PostMapping("/event/{id}/user")
    public String addUserToEvent(@RequestParam String username, @PathVariable Integer id,HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        eventService.addUserToEvent(username, id,currentUserId);
        return "Successfully added user to event";
    }
    @GetMapping("/event/{id}/user")
    public List<User> getEventUsers (@PathVariable Integer id,HttpServletRequest request) {
        return eventService.findAllUsers(id);
    }
    @GetMapping("/event/{event_id}/user/{user_id}/products")
    public List<Product> getEventUserProducts(@PathVariable Integer event_id, @PathVariable Long user_id){
        return eventService.findUserEventProducts(event_id,user_id);
    }
    @GetMapping("/event/{id}/myproducts")
    public List<Product> getMyEventUserProducts(@PathVariable Integer id,HttpServletRequest request){
        Long currentUserId = currentUserId(request);
        return eventService.findUserEventProducts(id,currentUserId);
    }
    @DeleteMapping("/event/{id}/product")
    public String deleteProductFromEvent (@PathVariable Integer id,@RequestParam Integer productToDeleteId, HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        return eventService.deleteProduct(id,productToDeleteId,currentUserId);
    }
    @DeleteMapping("/event/{id}/user")
    public String deleteUserFromEvent (@PathVariable Integer id,@RequestParam Long userToDeleteId, HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        return eventService.deleteUser(id,userToDeleteId,currentUserId);
    }
    @PutMapping ("/event/{event_id}/user/{user_id}/grantAdmin")
    public String grantAdminForUser(@PathVariable Integer event_id, @PathVariable Long user_id, HttpServletRequest request){
        Long currentUserId = currentUserId(request);
        return eventService.grantUserAdmin(event_id,user_id,currentUserId);
    }

    private Long currentUserId(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getJWTFromRequest(request);
        return jwtTokenProvider.getUserIdFromJWT(token);
    }
}
