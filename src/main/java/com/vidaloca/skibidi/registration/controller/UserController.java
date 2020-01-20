package com.vidaloca.skibidi.registration.controller;

import com.vidaloca.skibidi.model.Role;
import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.model.VerificationToken;
import com.vidaloca.skibidi.registration.dto.LoginDto;
import com.vidaloca.skibidi.registration.repository.RoleRepository;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import com.vidaloca.skibidi.registration.service.MapValidationErrorService;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import com.vidaloca.skibidi.registration.utills.RegisterEvent;
import com.vidaloca.skibidi.registration.dto.UserDto;
import com.vidaloca.skibidi.registration.service.UserService;
import com.vidaloca.skibidi.exceptions.EmailExistsException;
import com.vidaloca.skibidi.registration.utills.JwtLoginSucessResponse;
import com.vidaloca.skibidi.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.security.JwtTokenProvider;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
public class UserController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;
    @Autowired
    private  ApplicationEventPublisher eventPublisher;
    @Qualifier("messageSource")
    @Autowired
    private MessageSource messages;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private Environment env;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/currentUserId")
    public Long currentUserId(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getJWTFromRequest(request);
        return tokenProvider.getUserIdFromJWT(token);

    }
    @GetMapping("/roles")
    public List<Role> rolesGet(){
        return (List<Role>) roleRepository.findAll();
    }
    @GetMapping("/currentUser")
    public User getCurrentUser(HttpServletRequest request){
        System.out.println("\n"  + jwtAuthenticationFilter.getJWTFromRequest(request) + "\n");
        return userRepository.findById(currentUserId(request)).orElse(null);

    }
    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = "Bearer " +  tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtLoginSucessResponse(true, jwt));
    }


    @CrossOrigin
    @PostMapping ("/user/registration")
    public GenericResponse registerUserAccount(@RequestBody UserDto accountDto, final HttpServletRequest request) throws EmailExistsException {
        System.out.println(accountDto);
        LOGGER.debug("Registering user account with information: {}", accountDto);
        System.out.println(accountDto);
        final User registered = service.registerNewUserAccount(accountDto);
        eventPublisher.publishEvent(new RegisterEvent(registered, request.getLocale(), getAppUrl(request)));
        return new GenericResponse("success");
    }

    @GetMapping("/registrationConfirm")
    public GenericResponse confirmRegistration(final HttpServletRequest request, @RequestParam("token") final String token){
        final String result = service.validateVerificationToken(token);
        if (result.equals("valid")) {
            final User user = service.getUser(token);
            authWithoutPassword(user);
            return new GenericResponse("success");
        }

       return  new GenericResponse("fail");
    }
    @GetMapping("/user/resendRegistrationToken")
    public GenericResponse resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = service.generateNewVerificationToken(existingToken);
        final User user = service.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }


    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    public void authWithoutPassword(User user) {
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


    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
