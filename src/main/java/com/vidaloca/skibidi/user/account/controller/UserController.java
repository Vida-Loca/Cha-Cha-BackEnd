package com.vidaloca.skibidi.user.account.controller;

import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import com.vidaloca.skibidi.user.registration.utills.GenericResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private UserRepository userRepository;
    private EventUserRepository eventUserRepository;

    @Autowired
    public UserController(UserRepository userRepository, EventUserRepository eventUserRepository) {
        this.userRepository = userRepository;
        this.eventUserRepository = eventUserRepository;
    }
    @GetMapping("/user")
    public User getCurrentUser(HttpServletRequest request){
        return userRepository.findById(CurrentUser.currentUserId(request)).orElseThrow(()->new UserNotFoundException(CurrentUser.currentUserId(request)));
    }
    @GetMapping("/user/event")
    public List<Event> getAllUserEvents(HttpServletRequest request){
        User user = userRepository.findById(CurrentUser.currentUserId(request)).orElseThrow(()->new UserNotFoundException(CurrentUser.currentUserId(request)));
        List<EventUser> eu = eventUserRepository.findAllByUser(user);
        return eu.stream().map(EventUser::getEvent).collect(Collectors.toList());
    }
    @GetMapping("/user/isAdmin")
    public GenericResponse isAdmin(HttpServletRequest request){
        User user = userRepository.findById(CurrentUser.currentUserId(request)).orElseThrow(()->new UserNotFoundException(CurrentUser.currentUserId(request)));
        if (user.getRole().getName().equals("ADMIN"))
            return new GenericResponse("true");
        else
            return new GenericResponse("false");
    }
    @PutMapping("/user/changePhoto")
    public User changePhoto (HttpServletRequest request, @RequestParam ("url") String url ){
        User user = userRepository.findById(CurrentUser.currentUserId(request)).orElseThrow(()->new UserNotFoundException(CurrentUser.currentUserId(request)));
        user.setPicUrl(url);
        return userRepository.save(user);
    }
    @GetMapping("/user/resetPassword")
    public User resetPassword(){
        return null;
    }

}
