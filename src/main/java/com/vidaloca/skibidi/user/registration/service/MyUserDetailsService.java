package com.vidaloca.skibidi.user.registration.service;

import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        try {
            User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
            System.out.println(grantedAuthorities);
            return new org.springframework.security.core.userdetails.User
                    (user.getUsername(), user.getPassword(), grantedAuthorities);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public User loadUserById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));

    }
}