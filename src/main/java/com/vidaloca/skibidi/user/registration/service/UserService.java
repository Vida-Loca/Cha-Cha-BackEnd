package com.vidaloca.skibidi.user.registration.service;

import com.vidaloca.skibidi.user.registration.exception.UsernameExistsException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.registration.model.VerificationToken;
import com.vidaloca.skibidi.user.registration.dto.UserRegistrationDto;
import com.vidaloca.skibidi.user.registration.exception.EmailExistsException;

public interface UserService {
        User registerNewUserAccount(UserRegistrationDto accountDto)
                throws EmailExistsException, UsernameExistsException;
        User getUser(String verificationToken);

        void saveRegisteredUser(User user);

        void createVerificationToken(User user, String token);

        VerificationToken getVerificationToken(String VerificationToken);

        String validateVerificationToken(String token);

        VerificationToken generateNewVerificationToken(final String existingVerificationToken);
}
