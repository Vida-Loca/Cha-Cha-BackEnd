package com.vidaloca.skibidi.user.account.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.user.account.dto.PasswordDto;
import com.vidaloca.skibidi.user.account.mail.ResetPasswordMail;
import com.vidaloca.skibidi.user.account.model.ResetPasswordToken;
import com.vidaloca.skibidi.user.account.repository.ResetPasswordTokenRepository;
import com.vidaloca.skibidi.user.exception.EmailNotFoundException;
import com.vidaloca.skibidi.user.exception.PasswordsNotMatchesException;
import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceImplTest {

    @Mock(lenient = true)
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
    UserAccountServiceImpl service;

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
    }

    @Test
    void getCurrentUser() {
        //given

        //when
        User returned = service.getCurrentUser(user.getId());

        //then
        assertEquals(user, returned);
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllUserEvents() {
        //given
        Event event = new Event();
        Event event1 = new Event();
        EventUser eventUser = new EventUser();
        eventUser.setEvent(event);
        EventUser eventUser1 = new EventUser();
        eventUser1.setEvent(event1);
        List<EventUser> eventUserList = new ArrayList<>();
        eventUserList.add(eventUser);
        eventUserList.add(eventUser1);

        given(eventUserRepository.findAllByUser(user)).willReturn(eventUserList);

        //when
        List<Event> returned = service.getAllUserEvents(1L);

        //then
        assertEquals(2, returned.size());
        then(userRepository).should().findById(anyLong());
        then(eventUserRepository).should().findAllByUser(any(User.class));
    }

    @Test
    void isUserAdminTrue() {
        //given
        Role role = new Role();
        role.setName("ADMIN");
        user.setRole(role);

        //when
        boolean result = service.isUserAdmin(user.getId());

        //then
        assertTrue(result);
    }

    @Test
    void isUserAdminFalse() {
        //given
        Role role = new Role();
        role.setName("USER");
        user.setRole(role);

        //when
        boolean result = service.isUserAdmin(user.getId());

        //then
        assertFalse(result);
    }

    @Test
    void changePhoto() {
        //given
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        User returned = service.changePhoto(user.getId(), "TestURL");

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
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        //when
        String result = service.resetPassword(request, email);

        //then
        assertEquals("Check your mail box", result);
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(resetPasswordMail, times(1)).sendMail(any(HttpServletRequest.class),
                anyString(), any(User.class));
        verify(resetPasswordTokenRepository, times(1)).save(any(ResetPasswordToken.class));
    }

    @Test
    void resetPasswordEmailNotFound() {
        //given
        String email = "mail@test.com";
        user.setEmail(email);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        //when
        Exception result = assertThrows(EmailNotFoundException.class, () -> {
            service.resetPassword(request, email);
        });

        //then
        assertEquals("User with following email :mail@test.com not found", result.getMessage());
        then(userRepository).should().findByEmail(anyString());
        then(resetPasswordTokenRepository).shouldHaveNoInteractions();
        then(resetPasswordMail).shouldHaveNoInteractions();
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
        String result = service.resetPasswordConfirm(user.getId(), "TOKEN");

        //then
        assertNull(result);
        verify(resetPasswordTokenRepository, times(1)).findByToken(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void resetPasswordConfirmInvalid() {
        //given
        ResetPasswordToken passwordToken = new ResetPasswordToken();
        passwordToken.setId(1L);
        passwordToken.setToken("TOKEN");
        passwordToken.setUser(user);
        passwordToken.setExpiryDate(LocalDateTime.MIN);

        when(resetPasswordTokenRepository.findByToken(anyString())).thenReturn(passwordToken);

        //when
        String result = service.resetPasswordConfirm(2L, "TOKEN");

        //then
        assertEquals("invalidToken", result);
        then(resetPasswordTokenRepository).should().findByToken("TOKEN");
        then(userRepository).shouldHaveNoInteractions();
    }

    @Test
    void resetPasswordConfirmExpired() {
        //given
        ResetPasswordToken passwordToken = new ResetPasswordToken();
        passwordToken.setId(1L);
        passwordToken.setToken("TOKEN");
        passwordToken.setUser(user);
        passwordToken.setExpiryDate(LocalDateTime.MIN);

        when(resetPasswordTokenRepository.findByToken(anyString())).thenReturn(passwordToken);

        //when
        String result = service.resetPasswordConfirm(user.getId(), "TOKEN");

        //then
        assertEquals("expired", result);
        then(resetPasswordTokenRepository).should().findByToken("TOKEN");
        then(userRepository).shouldHaveNoInteractions();
    }

    @Test
    void changePassword() throws PasswordsNotMatchesException {
        //given
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setPassword("password");
        passwordDto.setMatchingPassword("password");
        user.setCanChangePass(true);

        when(passwordEncoder.encode(passwordDto.getPassword())).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        User returned = service.changePassword(user.getId(), passwordDto);

        //then
        assertEquals("password", returned.getPassword());
        verify(userRepository, times(1)).findById(anyLong());
        verify(passwordEncoder, times(1)).encode(any(CharSequence.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void changePasswordNotAllowed() throws PasswordsNotMatchesException {
        //given
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setPassword("password");
        passwordDto.setMatchingPassword("password");
        user.setCanChangePass(false);

        //when
        User returned = service.changePassword(user.getId(), passwordDto);

        //then
        assertNull(returned);
        then(userRepository).should().findById(anyLong());
        then(passwordEncoder).shouldHaveNoInteractions();
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void changePasswordNotMatching() throws PasswordsNotMatchesException {
        //given
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setPassword("password");
        passwordDto.setMatchingPassword("not_matching");
        user.setCanChangePass(true);

        //when
        Throwable result = assertThrows(PasswordsNotMatchesException.class, () -> {
            service.changePassword(1L, passwordDto);
        });

        //then
        assertEquals("Passwords not matches", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(passwordEncoder).shouldHaveNoInteractions();
        then(userRepository).shouldHaveNoMoreInteractions();
    }
}