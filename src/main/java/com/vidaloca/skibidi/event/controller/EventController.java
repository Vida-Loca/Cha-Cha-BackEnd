package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.exception.handler.UserActuallyInEventException;
import com.vidaloca.skibidi.event.exception.handler.UserIsNotAdminException;
import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.product.repository.ProductRepository;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.service.ProductService;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import com.vidaloca.skibidi.user.registration.utills.GenericResponse;
import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
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

    @GetMapping("/event/{eventId}")
    public Event getEventById(@PathVariable Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    @CrossOrigin
    @PostMapping("/event")
    public Event addNewEvent(@Valid @RequestBody EventDto eventDto, HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        return eventService.addNewEvent(eventDto,currentUserId);
    }

    @CrossOrigin
    @PutMapping("/event/{eventId}")
    public Event updateEvent(@Valid @RequestBody EventDto eventDto, @PathVariable Long eventId, HttpServletRequest request) throws UserIsNotAdminException {
        Long currentUserId = currentUserId(request);
        return eventService.updateEvent(eventDto, eventId, currentUserId);
    }

    @DeleteMapping("/event/{eventId}")
    public String deleteById(@PathVariable Long eventId, HttpServletRequest request) throws UserIsNotAdminException {
        Long currentUserId = currentUserId(request);
        return eventService.deleteEvent(eventId, currentUserId);
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

    @CrossOrigin
    @PostMapping("/event/{id}/product")
    public GenericResponse addProductToEvent(@RequestParam("productId") Integer productId, @PathVariable Integer id, HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        Product product = productRepository.findById(productId).orElse(null);
        if (product==null)
            return new GenericResponse("Product not exist");
        return new GenericResponse(eventService.addProductToEvent(product,id,currentUserId));
    }
     @GetMapping("/event/{eventId}/user/{userId}/products")
    public List<Product> getEventUserProducts(@PathVariable Integer eventId, @PathVariable Long userId){
        return eventService.findUserEventProducts(eventId,userId);
    }

       @GetMapping("/event/{id}/myproducts")
    public List<Product> getMyEventUserProducts(@PathVariable Integer id,HttpServletRequest request){
        Long currentUserId = currentUserId(request);
        return eventService.findUserEventProducts(id,currentUserId);
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
        Long currentUserId = currentUserId(request);
        return eventService.addUserToEvent(username, eventId,currentUserId);
    }

    @DeleteMapping("/event/{eventId}/user")
    public String deleteUserFromEvent (@PathVariable Long eventId,@RequestParam("userToDeleteId") Long userToDeleteId, HttpServletRequest request) throws UserIsNotAdminException {
        Long currentUserId = currentUserId(request);
        return eventService.deleteUser(eventId,userToDeleteId,currentUserId);
    }
    @PutMapping ("/event/{eventId}/user/{userId}/grantAdmin")
    public String grantAdminForUser(@PathVariable Long eventId, @PathVariable Long userId, HttpServletRequest request) throws UserIsNotAdminException {
        Long currentUserId = currentUserId(request);
        return  eventService.grantUserAdmin(eventId,userId,currentUserId);
    }
    /* To to wgl do serwisu trzeba
    @GetMapping ("/event/{event_id}/isAdmin")
    public GenericResponse isAdmin(@PathVariable("event_id") Integer eventId, HttpServletRequest request){
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return new GenericResponse("failed");
        User user = userRepository.findById(currentUserId(request)).orElse(null);
        if (user == null)
            return new GenericResponse("failed");
        EventUser eu = event_userRepository.findByUserAndEvent(user,event);
        if (eu.isAdmin())
            return new GenericResponse("true");
        else
            return new GenericResponse("false");
    }
*/
    private Long currentUserId(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getJWTFromRequest(request);
        return jwtTokenProvider.getUserIdFromJWT(token);
    }
}
