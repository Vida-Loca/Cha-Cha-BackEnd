package com.vidaloca.skibidi.user.registration.service;

import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.dto.UserRegistrationDto;
import com.vidaloca.skibidi.user.registration.exception.EmailExistsException;
import com.vidaloca.skibidi.user.registration.exception.UsernameExistsException;
import com.vidaloca.skibidi.user.registration.model.VerificationToken;
import com.vidaloca.skibidi.user.registration.repository.TokenRepository;
import com.vidaloca.skibidi.user.repository.RoleRepository;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    TokenRepository tokenRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl service;

    final String token = "TOKEN";

    User user;
    VerificationToken verificationToken;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("USER");
        verificationToken = new VerificationToken(token, user);
    }

    @Test
    void registerNewUserAccount() throws EmailExistsException, UsernameExistsException {
        //given
        UserRegistrationDto accountDto = new UserRegistrationDto("NAME", "SURNAME", "USERNAME", "PASSWORD",
                "PASSWORD", "EMAIL", "URL");

        Role role = new Role();
        role.setName("USER");

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
        given(roleRepository.findByName("USER")).willReturn(Optional.of(role));
        given(userRepository.save(any(User.class))).willReturn(user);

        //when
        User result = service.registerNewUserAccount(accountDto);

        //then
        then(userRepository).should().findByEmail(anyString());
        then(userRepository).should().findByUsername(anyString());
        then(passwordEncoder).should().encode(any());
        then(roleRepository).should().findByName(anyString());
        then(roleRepository).should(times(0)).save(any(Role.class));
        then(userRepository).should().save(any(User.class));
    }

    @Test
    void getUser() {
        //given
        given(tokenRepository.findByToken(token)).willReturn(verificationToken);

        //when
        User result = service.getUser(token);

        //then
        assertEquals("USER", result.getUsername());
        assertEquals("USER", verificationToken.getUser().getUsername());
        then(tokenRepository).should().findByToken(anyString());
    }

    @Test
    void getVerificationToken() {
        //given
        given(tokenRepository.findByToken(anyString())).willReturn(verificationToken);

        //when
        VerificationToken result = service.getVerificationToken(token);

        //then
        assertEquals(verificationToken, result);
        then(tokenRepository).should().findByToken(anyString());
    }

    @Test
    void saveRegisteredUser() {
        //given

        //when
        service.saveRegisteredUser(user);

        //then
        then(userRepository).should().save(any(User.class));
    }

    @Test
    void createVerificationToken() {
        //given

        //when
        service.createVerificationToken(user, token);

        //then
        then(tokenRepository).should().save(any(VerificationToken.class));
    }

    @Test
    void validateVerificationToken() {
        //given
        given(tokenRepository.findByToken(token)).willReturn(verificationToken);

        //when
        String result = service.validateVerificationToken(token);

        //then
        assertEquals("valid", result);
        then(tokenRepository).should().findByToken(anyString());
        then(userRepository).should().save(any(User.class));
    }

    @Test
    void generateNewVerificationToken() {
        //given
        verificationToken.setExpiryDate(new Date(2020, 03, 20));
        Date dateBefore = verificationToken.getExpiryDate();
        given(tokenRepository.findByToken(token)).willReturn(verificationToken);
        given(tokenRepository.save(any(VerificationToken.class))).willReturn(verificationToken);

        //when
        VerificationToken result = service.generateNewVerificationToken(token);

        //then
        assertNotEquals(dateBefore.getTime(), result.getExpiryDate().getTime());
        then(tokenRepository).should().findByToken(anyString());
        then(tokenRepository).should().save(any(VerificationToken.class));
    }

    @Test
    void constructMail() {
        //given
        user.setEmail("mail");

        //when
        SimpleMailMessage result = service.constructMail("subject", "body", user);

        //then
        assertEquals("subject", result.getSubject());
        assertEquals("body", result.getText());
    }
}