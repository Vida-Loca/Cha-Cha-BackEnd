package com.vidaloca.skibidi.admin.controller;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.repository.ProductRepository;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.RoleRepository;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserRepository userRepository;
    private EventService eventService;
    private RoleRepository roleRepository;
    private EventRepository eventRepository;
    private ProductRepository productRepository;


    @Autowired
    public AdminController(UserRepository userRepository, EventService eventService, RoleRepository roleRepository,
                           EventRepository eventRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.eventService = eventService;
        this.roleRepository = roleRepository;
        this.eventRepository = eventRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
    }

    @PutMapping("/grantUserAdmin/{id}")
    public User grantUserAdmin(@PathVariable("id") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Role role = roleRepository.findByName("ADMIN").orElse(null);
        user.setRole(role);
        return userRepository.save(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteUser(HttpServletRequest request, @PathVariable("id") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(user);
    }

    @GetMapping("/getAllEvents")
    public List<Event> getAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }

    @GetMapping("/event/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @GetMapping("/getAllProducts")
    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }


}
