package com.vidaloca.skibidi.admin.controller;

import com.vidaloca.skibidi.admin.service.AdminUserService;
import com.vidaloca.skibidi.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private AdminUserService service;

    @Autowired
    public AdminUserController(AdminUserService service) {
        this.service = service;
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return service.findAllUsers();
    }

    @GetMapping("/getAllAdmins")
    public List<User> getAllAdmins() {
        return service.findAllAdmins();
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return service.findUserById(id);
    }

    @DeleteMapping("/user/{id}")
    public String deleteById(@PathVariable Long id) {
        return service.deleteUserById(id);
    }

    @PutMapping("/user/{id}/grantAdmin")
    public User grantUserAuthority(@PathVariable Long id) {
        return service.grantAdminAuthority(id);
    }

    @PutMapping("/user/{id}/banishUser")
    public User banishUser(@PathVariable Long id) {
        return service.punishUser(id);
    }

}
