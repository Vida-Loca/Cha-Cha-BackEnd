package com.vidaloca.skibidi.user.account.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.user.account.dto.PasswordDto;
import com.vidaloca.skibidi.user.exception.PasswordsNotMatchesException;
import com.vidaloca.skibidi.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserAccountService {
    User getCurrentUser(Long userId);

    List<Event> getAllUserEvents (Long userId);

    boolean isUserAdmin(Long userId);

    User changePhoto (Long userId, String photoUrl);

    String resetPassword (HttpServletRequest request, String email);

    String resetPasswordConfirm (Long userId, String token);

    User changePassword (Long userId, PasswordDto passwordDto) throws PasswordsNotMatchesException;


}
