package com.vidaloca.skibidi.user.account.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import com.vidaloca.skibidi.user.account.dto.PasswordDto;
import com.vidaloca.skibidi.user.account.mail.ResetPasswordMail;
import com.vidaloca.skibidi.user.account.model.ResetPasswordToken;
import com.vidaloca.skibidi.user.account.repository.ResetPasswordTokenRepository;
import com.vidaloca.skibidi.user.exception.PasswordsNotMatchesException;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ResetPasswordTokenRepository resetPasswordTokenRepository;
    private ResetPasswordMail resetPasswordMail;

    @Autowired
    public UserAccountServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ResetPasswordTokenRepository resetPasswordTokenRepository, ResetPasswordMail resetPasswordMail) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.resetPasswordMail = resetPasswordMail;
    }

    @Override
    public User getCurrentUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<Event> getAllUserEvents(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Event> events = new ArrayList<>();
        user.getEventUsers().forEach(eu -> events.add(eu.getEvent()));
        return events;
    }

    @Override
    public boolean isUserAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return user.getRole().getName().equals("ADMIN");
    }

    @Override
    public User changePhoto(Long userId, String photoUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.setPicUrl(photoUrl);
        return userRepository.save(user);
    }

    @Override
    public String resetPassword(HttpServletRequest request) {
        User user = userRepository.findById(CurrentUser.currentUserId(request)).orElseThrow(() -> new UserNotFoundException(CurrentUser.currentUserId(request)));
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        resetPasswordMail.sendMail(request,token,user);
        return "Check your mail box";
    }

    @Override
    public String resetPasswordConfirm(Long userId, String token) {
        return validatePasswordResetToken(userId, token);
    }

    @Override
    public User changePassword(Long userId, PasswordDto passwordDto) throws PasswordsNotMatchesException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (passwordDto.getPassword().equals(passwordDto.getMatchingPassword())) {
            user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        } else
            throw new PasswordsNotMatchesException();
        user.setCanChangePass(false);
        return userRepository.save(user);
    }

    private void createPasswordResetTokenForUser(User user, String token) {
        ResetPasswordToken myToken = new ResetPasswordToken(user,token);
        resetPasswordTokenRepository.save(myToken);
    }
    private String validatePasswordResetToken(long id, String token) {
        ResetPasswordToken passToken =
                resetPasswordTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser()
                .getId() != id)) {
            return "invalidToken";
        }
        if (passToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "expired";
        }

        User user = passToken.getUser();
        user.setCanChangePass(true);
        userRepository.save(user);
        return null;
    }
}
