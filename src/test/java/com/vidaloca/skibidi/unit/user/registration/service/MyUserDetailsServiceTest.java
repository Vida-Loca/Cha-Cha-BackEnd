package com.vidaloca.skibidi.unit.user.registration.service;

import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.service.MyUserDetailsService;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    MyUserDetailsService service;

    User user;
    Role role;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("USER");
        user.setPassword("PASSWORD");

        role = new Role();
        role.setId(1L);
        role.setName("USER");

        user.setRole(role);
    }

    @Test
    void loadUserByUsername() {
        //given
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));

        //when
        UserDetails result = service.loadUserByUsername("USER");

        //then
        assertNotNull(result);
        assertEquals("USER", result.getUsername());
        assertEquals("PASSWORD", result.getPassword());
        then(userRepository).should().findByUsername(anyString());
    }

    @Test
    void loadUserByUsernameNotFoundEx() {
        //given
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.loadUserByUsername("USER");
        });

        //then
        assertEquals("USER", exception.getCause().getMessage());
        then(userRepository).should().findByUsername(anyString());
    }

    @Test
    void loadUserById() {
        //given
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));

        //when
        User result = service.loadUserById(1L);

        //then
        assertEquals("USER", result.getUsername());
        then(userRepository).should().findById(anyLong());
    }

    @Test
    void loadUserByIdNotFoundEx() {
        //given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
       Exception exception = assertThrows(UserNotFoundException.class, () -> {
           service.loadUserById(1L);
       });

        //then
        assertEquals("User with id: 1 is not found", exception.getMessage());
        then(userRepository).should().findById(anyLong());
    }
}