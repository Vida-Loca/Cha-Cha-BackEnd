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

    @DeleteMapping("/event/{eventId}")
    public String deleteById(@PathVariable Long eventId) {
        return eventService.deleteById(eventId);
    }

//    @GetMapping("/getAllUsers")
//    public List<User> getAllUsers() {
//        return (List<User>) userRepository.findAll();
//    }
//
//    @GetMapping("/user/{id}")
//    public User getUserById(@PathVariable Long id) {
//        return userRepository.findById(id).orElseThrow(
//                () -> new UserNotFoundException(id)
//        );
//    }
//
//    @PutMapping("/grantUserAdmin/{id}")
//    public User grantUserAdmin(@PathVariable("id") Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
//        Role role = roleRepository.findByName("ADMIN").orElse(null);
//        user.setRole(role);
//        return userRepository.save(user);
//    }
//
//    @DeleteMapping("/deleteUser/{id}")
//    public void deleteUser(HttpServletRequest request, @PathVariable("id") Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
//        userRepository.delete(user);
//    }
//
//    @GetMapping("/getAllProducts")
//    public List<Product> getAllProducts() {
//        return (List<Product>) productRepository.findAll();
//    }


}
