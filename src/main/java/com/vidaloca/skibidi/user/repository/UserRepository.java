package com.vidaloca.skibidi.user.repository;

import com.vidaloca.skibidi.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail (String email);
    List<User> findAllByUsernameContains(String regex);
}
