package com.vidaloca.skibidi.admin.service;

import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.Role;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.RoleRepository;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<User> findAllAdmins() {
        List<User> admins = new ArrayList<>();
        Role admin = roleRepository.findByName("ADMIN").orElse(null);
        List<User> allUsers = (List<User>) userRepository.findAll();
        for (User user : allUsers) {
            if (user.getRole() == admin) {
                admins.add(user);
            }
        }
        return admins;
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public String deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } else {
            throw new UserNotFoundException(id);
        }
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
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Role role;
        if (user.getRole().getId() == 1) {
            role = roleRepository.findByName("BANNED").orElse(null);
            user.setRole(role);
            user.setBanned(true);
        } else if (user.getRole().getId() == 3) {
            role = roleRepository.findByName("USER").orElse(null);
            user.setRole(role);
            user.setBanned(false);
        }
        return userRepository.save(user);
    }
}
