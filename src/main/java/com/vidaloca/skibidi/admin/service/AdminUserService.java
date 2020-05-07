package com.vidaloca.skibidi.admin.service;

import com.vidaloca.skibidi.user.model.User;

import java.util.List;

public interface AdminUserService {

    List<User> findAllUsers();

    User findUserById(Long id);

    String deleteUserById(Long id);

    User grantAdminAuthority(Long id);

    User punishUser(Long id);
}
