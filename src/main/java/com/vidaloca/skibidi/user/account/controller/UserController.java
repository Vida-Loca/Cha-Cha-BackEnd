package com.vidaloca.skibidi.user.account.controller;

import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import com.vidaloca.skibidi.user.registration.utills.GenericResponse;
import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private UserRepository userRepository;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenProvider tokenProvider;
    private EventUserRepository eventUserRepository;

    @Autowired
    public UserController(UserRepository userRepository, JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtTokenProvider tokenProvider, EventUserRepository eventUserRepository) {
        this.userRepository = userRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.tokenProvider = tokenProvider;
        this.eventUserRepository = eventUserRepository;
    }
    @GetMapping("/user")
    public User getCurrentUser(HttpServletRequest request){
        return userRepository.findById(currentUserId(request)).orElse(null);
    }
    @GetMapping("/user/event")
    public List<Event> getAllUserEvents(HttpServletRequest request){
        List <Event> events = new ArrayList<>();
        User user =  userRepository.findById(currentUserId(request)).orElse(null);
        if (user == null) {
            return events;
        }
        List<EventUser> eu = eventUserRepository.findAllByUser(user);
        for (EventUser x: eu)
            events.add(x.getEvent());
        return events;
    }
    @GetMapping("/user/isAdmin")
    public GenericResponse isAdmin(HttpServletRequest request){
        User user = userRepository.findById(currentUserId(request)).orElse(null);
        if (user == null)
            return new GenericResponse("failed");
        if (user.getRole().getName().equals("ADMIN"))
            return new GenericResponse("true");
        else
            return new GenericResponse("false");
    }
    @PutMapping("/user/changePhoto")
    public GenericResponse changePhoto (HttpServletRequest request, @RequestParam ("url") String url ){
        User user = userRepository.findById(currentUserId(request)).orElse(null);
        if (user == null) {
            return new GenericResponse("fail");
        }
        user.setPicUrl(url);
        userRepository.save(user);
        return new GenericResponse("success");
    }
    private Long currentUserId(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getJWTFromRequest(request);
        return tokenProvider.getUserIdFromJWT(token);

    }

}