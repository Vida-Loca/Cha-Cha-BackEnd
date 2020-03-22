package com.vidaloca.skibidi.user.account.repository;

import com.vidaloca.skibidi.user.account.model.ResetPasswordToken;
import org.springframework.data.repository.CrudRepository;

public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, Long> {
    ResetPasswordToken findByToken(String token);
}
