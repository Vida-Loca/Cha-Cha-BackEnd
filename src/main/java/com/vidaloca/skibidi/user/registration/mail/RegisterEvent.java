package com.vidaloca.skibidi.user.registration.mail;

import com.vidaloca.skibidi.user.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class RegisterEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private User user;

    public RegisterEvent(
            User user, Locale locale, String appUrl) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
