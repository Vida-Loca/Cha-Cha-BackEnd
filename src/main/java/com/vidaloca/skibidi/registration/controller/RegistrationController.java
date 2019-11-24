package com.vidaloca.skibidi.registration.controller;

import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.model.VerificationToken;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import com.vidaloca.skibidi.registration.utills.RegisterEvent;
import com.vidaloca.skibidi.registration.dto.UserDto;
import com.vidaloca.skibidi.registration.service.UserService;
import com.vidaloca.skibidi.registration.validation.EmailExistsException;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class RegistrationController {
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


    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse registerUserAccount(@RequestBody UserDto accountDto, final HttpServletRequest request) throws EmailExistsException {
        LOGGER.debug("Registering user account with information: {}", accountDto);

        final User registered = service.registerNewUserAccount(accountDto);
        eventPublisher.publishEvent(new RegisterEvent(registered, request.getLocale(), getAppUrl(request)));
        return new GenericResponse("success");
    }
    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse showUsers() throws EmailExistsException {

        return new GenericResponse("success");
    }
    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public GenericResponse confirmRegistration(final HttpServletRequest request, @RequestParam("token") final String token){
        final String result = service.validateVerificationToken(token);
        if (result.equals("valid")) {
            final User user = service.getUser(token);
            authWithoutPassword(user);
            return new GenericResponse("success");
        }

       return  new GenericResponse("fail");
    }
    @RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = service.generateNewVerificationToken(existingToken);
        final User user = service.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }





    //Non REST


    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
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
    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
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
