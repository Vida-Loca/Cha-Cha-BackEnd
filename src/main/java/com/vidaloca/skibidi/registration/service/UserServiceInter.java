package com.vidaloca.skibidi.registration.service;

import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.model.VerificationToken;
import com.vidaloca.skibidi.registration.dto.UserDto;
import com.vidaloca.skibidi.exceptions.EmailExistsException;

public interface UserServiceInter {
        User registerNewUserAccount(UserDto accountDto)
                throws EmailExistsException;
        User getUser(String verificationToken);

        void saveRegisteredUser(User user);

        void createVerificationToken(User user, String token);

        VerificationToken getVerificationToken(String VerificationToken);

        String validateVerificationToken(String token);

        VerificationToken generateNewVerificationToken(final String existingVerificationToken);
}
