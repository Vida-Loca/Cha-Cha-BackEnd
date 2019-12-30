package com.vidaloca.skibidi.registration.repository;

import com.vidaloca.skibidi.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail (String email);
}
