package com.vidaloca.skibidi.admin.controller;

import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.RoleRepository;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AdminController {
    private UserRepository userRepository;
    private EventService eventService;
    private RoleRepository roleRepository;

    @Autowired
    public AdminController(UserRepository userRepository, EventService eventService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.eventService = eventService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin/getAllUsers")
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @PutMapping("/admin/grantUserAdmin/{id}")
    public User grantUserAdmin(@PathVariable("id") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Role role = roleRepository.findByName("ADMIN").orElse(null);
        user.setRole(role);
        return userRepository.save(user);
    }

    @DeleteMapping("/admin/deleteUser/{id}")
    public void deleteUser(HttpServletRequest request, @PathVariable("id") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(user);
    }

}
