package com.vidaloca.skibidi.admin.controller;

import com.vidaloca.skibidi.admin.service.AdminEventUserService;
import com.vidaloca.skibidi.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminEventUserController {

    private AdminEventUserService eventUserService;

    @Autowired
    public AdminEventUserController(AdminEventUserService eventUserService) {
        this.eventUserService = eventUserService;
    }

    @DeleteMapping("/event/{eventId}/user")
    public String deleteUserFromEvent(@PathVariable Long eventId, @RequestParam Long userToDeleteId) {
        return eventUserService.deleteUserFromEvent(userToDeleteId, eventId);
    }

    @GetMapping("/event/{eventId}/users")
    public List<User> findAllEventUsers(@PathVariable Long eventId) {
        return eventUserService.findAllEventUsers(eventId);
    }

    @PutMapping("/event/{eventId}/user/{userId}/eventAdmin")
    public boolean grantTakeEventAdmin(@PathVariable Long eventId, @PathVariable Long userId) {
        return eventUserService.grantTakeUserEventAdmin(eventId,userId);
    }
}
