package com.vidaloca.skibidi.registration.repository;

import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.model.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<VerificationToken,Integer> {
    VerificationToken findByToken(String token);
}
