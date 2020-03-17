package com.vidaloca.skibidi.user.repository;

import com.vidaloca.skibidi.user.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail (String email);
}
