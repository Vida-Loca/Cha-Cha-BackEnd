package com.vidaloca.skibidi.user.account.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.user.account.dto.PasswordDto;
import com.vidaloca.skibidi.user.account.mail.ResetPasswordMail;
import com.vidaloca.skibidi.user.account.model.ResetPasswordToken;
import com.vidaloca.skibidi.user.account.repository.ResetPasswordTokenRepository;
import com.vidaloca.skibidi.user.exception.PasswordsNotMatchesException;
import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserAccountServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    ResetPasswordTokenRepository resetPasswordTokenRepository;
    @Mock
    ResetPasswordMail resetPasswordMail;

    @Mock
    EventUserRepository eventUserRepository;

    @InjectMocks
    UserAccountServiceImpl userAccountService;

    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userAccountService = new UserAccountServiceImpl(userRepository, eventUserRepository, passwordEncoder,
                resetPasswordTokenRepository, resetPasswordMail);

        user = new User();
        user.setId(1L);
    }

    @Test
    void getCurrentUser() {
        //given
        Optional<User> userOptional = Optional.of(user);
        when(userRepository.findById(anyLong())).thenReturn(userOptional);

        //when
        User returned = userAccountService.getCurrentUser(user.getId());

        //then
        assertEquals(user, returned);
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllUserEvents() {
        //given
        Event event = new Event();
        EventUser eventUser = new EventUser();
        eventUser.setEvent(event);
        eventUser.setUser(user);
        Event event1 = new Event();
        EventUser eventUser1 = new EventUser();
        eventUser1.setEvent(event1);
        eventUser1.setUser(user);
        user.getEventUsers().add(eventUser);
        user.getEventUsers().add(eventUser1);
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(anyLong())).thenReturn(userOptional);

        //when
        List<Event> returned = userAccountService.getAllUserEvents(user.getId());

        //then
        assertEquals(2, returned.size());
        assertEquals(event, returned.get(0));
        assertEquals(event1, returned.get(1));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void isUserAdminTrue() {
        //given
        Role role = new Role();
        role.setName("ADMIN");
        user.setRole(role);
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(anyLong())).thenReturn(userOptional);

        //when
        boolean result = userAccountService.isUserAdmin(user.getId());

        //then
        assertTrue(result);
    }

    @Test
    void isUserAdminFalse() {
        //given
        Role role = new Role();
        role.setName("USER");
        user.setRole(role);
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(anyLong())).thenReturn(userOptional);

        //when
        boolean result = userAccountService.isUserAdmin(user.getId());

        //then
        assertFalse(result);
    }

    @Test
    void changePhoto() {
        //given
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(anyLong())).thenReturn(userOptional);
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        User returned = userAccountService.changePhoto(user.getId(), "TestURL");

        //then
        assertEquals("TestURL", returned.getPicUrl());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void resetPassword() {
        //given
        String email = "mail@test.com";
        user.setEmail(email);
        Optional<User> userOptional = Optional.of(user);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(userRepository.findByEmail(anyString())).thenReturn(userOptional);

        //when
        String result = userAccountService.resetPassword(request, email);

        //then
        assertEquals("Check your mail box", result);
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(resetPasswordMail, times(1)).sendMail(any(HttpServletRequest.class),
                anyString(), any(User.class));
        verify(resetPasswordTokenRepository, times(1)).save(any(ResetPasswordToken.class));
    }

    @Test
    void resetPasswordConfirm() {
        //given
        ResetPasswordToken passwordToken = new ResetPasswordToken();
        passwordToken.setId(1L);
        passwordToken.setToken("TOKEN");
        passwordToken.setUser(user);
        passwordToken.setExpiryDate(LocalDateTime.MAX);

        when(resetPasswordTokenRepository.findByToken(anyString())).thenReturn(passwordToken);

        //when
        String result = userAccountService.resetPasswordConfirm(user.getId(), "TOKEN");

        //then
        assertNull(result);
        verify(resetPasswordTokenRepository, times(1)).findByToken(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void changePassword() throws PasswordsNotMatchesException {
        //given
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setPassword("password");
        passwordDto.setMatchingPassword("password");
        user.setCanChangePass(true);
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(anyLong())).thenReturn(userOptional);
        when(passwordEncoder.encode(passwordDto.getPassword())).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        User returned = userAccountService.changePassword(user.getId(), passwordDto);

        //then
        assertEquals("password", returned.getPassword());
        verify(userRepository, times(1)).findById(anyLong());
        verify(passwordEncoder, times(1)).encode(any(CharSequence.class));
        verify(userRepository, times(1)).save(any(User.class));
    }
}