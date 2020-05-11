package com.vidaloca.skibidi.user.registration.controller;

import com.vidaloca.skibidi.user.registration.exception.TokenNotValidException;
import com.vidaloca.skibidi.user.registration.exception.UsernameExistsException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.model.VerificationToken;
import com.vidaloca.skibidi.user.registration.service.UserService;
import com.vidaloca.skibidi.user.registration.mail.RegisterEvent;
import com.vidaloca.skibidi.user.registration.dto.UserRegistrationDto;
import com.vidaloca.skibidi.user.registration.exception.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
public class RegistrationController {

    private UserService userService;
    private  ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegistrationController(UserService userService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @CrossOrigin
    @PostMapping ("/registration")
    public User registerUserAccount(@RequestBody UserRegistrationDto accountDto, final HttpServletRequest request) {
        User registered = userService.registerNewUserAccount(accountDto);
        eventPublisher.publishEvent(new RegisterEvent(registered, request.getLocale()));
        return registered;
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") final String token){
        final String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            final User user = userService.getUser(token);
            authWithoutPassword(user);
            return "Success";
        }
        throw new TokenNotValidException();
    }

   /* private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }*/

    private void authWithoutPassword(User user) {
        List<String> role = new ArrayList<>();
        role.add(user.getRole().getName());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, getAuthorities(role));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

}
