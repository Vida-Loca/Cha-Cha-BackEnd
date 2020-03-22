package com.vidaloca.skibidi.user.account.current;

import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
@Component
public class CurrentUser {

    private static JwtAuthenticationFilter jwtAuthenticationFilter;
    private static JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CurrentUser(JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider) {
        CurrentUser.jwtAuthenticationFilter = jwtAuthenticationFilter;
        CurrentUser.jwtTokenProvider = jwtTokenProvider;
    }

    public static Long currentUserId(HttpServletRequest request) {
            String token = jwtAuthenticationFilter.getJWTFromRequest(request);
            return jwtTokenProvider.getUserIdFromJWT(token);
        }
}
