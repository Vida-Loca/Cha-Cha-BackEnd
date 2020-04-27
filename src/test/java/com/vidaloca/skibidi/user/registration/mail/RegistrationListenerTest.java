package com.vidaloca.skibidi.user.registration.mail;

import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RegistrationListenerTest {

    @Mock
    UserService service;
    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    RegistrationListener listener;

    RegisterEvent registerEvent = mock(RegisterEvent.class);
    User user;
    Locale locale;
    final String appUrl = "url";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("mail");
        locale = Locale.ENGLISH;

        registerEvent = new RegisterEvent(user, locale, appUrl);
        registerEvent.setAppUrl(appUrl);
        registerEvent.setUser(user);
        registerEvent.setLocale(locale);
    }

    @Test
    void onApplicationEvent() {
        listener.onApplicationEvent(registerEvent);

        then(service).should().createVerificationToken(any(User.class), anyString());
        then(mailSender).should().send(any(SimpleMailMessage.class));
    }
}