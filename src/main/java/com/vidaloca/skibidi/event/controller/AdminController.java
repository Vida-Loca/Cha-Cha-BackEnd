package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.model.Role;
import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.registration.repository.RoleRepository;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<User> getAllUsers(){
        return (List<User>) userRepository.findAll();
    }
    @PutMapping("/admin/grantUserAdmin/{id}")
    public GenericResponse grantUserAdmin(@PathVariable ("id") Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return  new GenericResponse("UserNotExist");
        Role role = roleRepository.findById(2).orElse(null);
        if (role == null)
            return new GenericResponse("Unexpected Problem");
        user.setRole(role);
        userRepository.save(user);
        return new GenericResponse("successfully granted admin for user: " +userId);
    }

    @DeleteMapping("/admin/deleteUser/{id}")
    public GenericResponse deleteUser(HttpServletRequest request, @PathVariable("id") Long userId) {
        if (userRepository.findById(userId).orElse(null) == null)
            return new GenericResponse("User not exists");
        userRepository.deleteById(userId);
        return new GenericResponse("Successfully deleted user");
    }

}
