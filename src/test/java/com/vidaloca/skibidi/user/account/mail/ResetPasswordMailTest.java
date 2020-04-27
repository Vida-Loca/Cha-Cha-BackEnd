package com.vidaloca.skibidi.user.account.mail;

import com.vidaloca.skibidi.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ResetPasswordMailTest {

    @Mock
    MailSender mailSender;

    @InjectMocks
    ResetPasswordMail resetPasswordMail;

    MockHttpServletRequest request;
    User user;
    final String token = "token";

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.setContextPath("contextPath");
        request.setServerPort(3000);
        request.setServerName("serverName");
        user = new User();
    }

    @Test
    void sendMail() {
        resetPasswordMail.sendMail(request, token, user);

        then(mailSender).should().send(any(SimpleMailMessage.class));
        then(mailSender).shouldHaveNoMoreInteractions();
    }
}