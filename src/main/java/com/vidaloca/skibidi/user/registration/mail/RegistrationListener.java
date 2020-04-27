package com.vidaloca.skibidi.user.registration.mail;

import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<RegisterEvent> {
  
    @Autowired
    private UserService service;

    @Autowired
    private JavaMailSender mailSender;
 
    @Override
    public void onApplicationEvent(RegisterEvent event) {
        this.confirmRegistration(event);
    }
 
    private void confirmRegistration(RegisterEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl 
          = event.getAppUrl() + "/registrationConfirm?token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Click link to verify your account : " + confirmationUrl);
        mailSender.send(email);
    }

}