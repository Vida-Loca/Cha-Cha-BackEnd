package com.vidaloca.skibidi.registration.utills;

import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.registration.service.UserServiceInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<RegisterEvent> {
  
    @Autowired
    private UserServiceInter service;

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
        email.setText("Clik link to verify your account : " + confirmationUrl);
        mailSender.send(email);
    }

}