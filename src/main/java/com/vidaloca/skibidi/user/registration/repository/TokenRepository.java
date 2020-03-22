package com.vidaloca.skibidi.user.registration.repository;

import com.vidaloca.skibidi.user.registration.model.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<VerificationToken,Integer> {
    VerificationToken findByToken(String token);
}
