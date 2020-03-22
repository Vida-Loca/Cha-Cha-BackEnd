package com.vidaloca.skibidi.user.account.mail;

import com.vidaloca.skibidi.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class ResetPasswordMail {

    private MailSender mailSender;

    @Autowired
    public ResetPasswordMail(MailSender mailSender) {
        this.mailSender = mailSender;
    }


    private SimpleMailMessage constructResetTokenEmail(
            String contextPath, String token, User user) {
        String message = "Click link to reset password: ";
        String url = contextPath + "/user/changePassword?userId=" +
                user.getId() + "&token=" + token;
        return constructEmail(message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Reset Password");
        email.setText(body);
        email.setTo(user.getEmail());
        return email;
    }

    public void sendMail(HttpServletRequest request, String token, User user) {
        mailSender.send(constructResetTokenEmail(getAppUrl(request), token, user));
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
