package com.vidaloca.skibidi.admin.service;

import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.RoleRepository;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AdminUserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public String deleteUserById(Long id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    @Override
    public User grantAdminAuthority(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Role role = roleRepository.findByName("ADMIN").orElse(null);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public User punishUser(Long id) {
        boolean bool;
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        bool = user.isBanned() ? false : true;
        user.setBanned(bool);
        return userRepository.save(user);
    }
}
