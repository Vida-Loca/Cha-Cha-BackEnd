package com.vidaloca.skibidi.user.registration.service;

import com.vidaloca.skibidi.user.registration.exception.UsernameExistsException;
import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.model.VerificationToken;
import com.vidaloca.skibidi.user.registration.dto.UserRegistrationDto;
import com.vidaloca.skibidi.user.repository.RoleRepository;
import com.vidaloca.skibidi.user.registration.repository.TokenRepository;
import com.vidaloca.skibidi.user.repository.UserRepository;
import com.vidaloca.skibidi.user.registration.exception.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TokenRepository tokenRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Transactional
    @Override
    public User registerNewUserAccount(UserRegistrationDto accountDto) {

        if (emailExists(accountDto.getEmail())) {
            throw new EmailExistsException(accountDto.getEmail());
        }
        if (userRepository.findByUsername(accountDto.getUsername()).isPresent()) {
            throw new UsernameExistsException(accountDto.getUsername());
        }
        User user = User.builder().username(accountDto.getUsername()).name(accountDto.getName()).surname(accountDto.getSurname()).
                password(passwordEncoder.encode(accountDto.getPassword())).email(accountDto.getEmail()).picUrl(accountDto.getPicUrl()).
                role(getRole()).build();
        return userRepository.save(user);
    }

    private Role getRole(){
        Role role = roleRepository.findByName("USER").orElse(null);
        if (role == null){
            role = new Role();
            role.setName("USER");
            role = roleRepository.save(role);
        }
        return  role;
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User getUser(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime()
                - cal.getTime()
                .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
       // tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return TOKEN_VALID;
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
                .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public SimpleMailMessage constructMail(String subject, String body, User user) {
            final SimpleMailMessage email = new SimpleMailMessage();
            email.setSubject(subject);
            email.setText(body);
            email.setTo(user.getEmail());
            return email;
    }

}