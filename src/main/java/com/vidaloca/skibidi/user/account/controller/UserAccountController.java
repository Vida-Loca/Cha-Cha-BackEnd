package com.vidaloca.skibidi.user.account.controller;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import com.vidaloca.skibidi.user.account.dto.NamesDto;
import com.vidaloca.skibidi.user.account.dto.PasswordDto;
import com.vidaloca.skibidi.user.account.service.UserAccountService;
import com.vidaloca.skibidi.user.exception.PasswordsNotMatchesException;
import com.vidaloca.skibidi.user.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserAccountController {
    private UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/user")
    public User getCurrentUser(HttpServletRequest request) {
        return userAccountService.getCurrentUser(CurrentUser.currentUserId(request));
    }

    @GetMapping("/user/event")
    public List<Event> getAllUserEvents(HttpServletRequest request) {
        return userAccountService.getAllUserEvents(CurrentUser.currentUserId(request));
    }

    @GetMapping("/user/isAdmin")
    public boolean isAdmin(HttpServletRequest request) {
        return userAccountService.isUserAdmin(CurrentUser.currentUserId(request));
    }

    @PutMapping("/user/changePhoto")
    public User changePhoto(HttpServletRequest request, @RequestParam("url") String url) {
        return userAccountService.changePhoto(CurrentUser.currentUserId(request), url);
    }

    @PutMapping("/user/changeNames")
    public User changeNames(HttpServletRequest request, @RequestBody NamesDto namesDto){
        return userAccountService.changeNames(namesDto,CurrentUser.currentUserId(request));
    }

    @PostMapping("/user/resetPassword")
    public String resetPassword(HttpServletRequest request, @RequestParam String email) {
         return userAccountService.resetPassword(request,email);
    }

    @PutMapping("/user/changePassword")
    public User resetPassword(@RequestParam Long userId, @RequestParam String token, @RequestBody PasswordDto passwordDto){
        return userAccountService.changePassword(userId, token, passwordDto);
    }

}
