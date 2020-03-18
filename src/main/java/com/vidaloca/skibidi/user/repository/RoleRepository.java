package com.vidaloca.skibidi.user.repository;

import com.vidaloca.skibidi.user.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role,Long> {
    Optional<Role> findByName(String role);
}
