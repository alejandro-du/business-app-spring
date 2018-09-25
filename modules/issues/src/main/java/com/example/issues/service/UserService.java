package com.example.issues.service;

import com.example.issues.domain.Role;
import com.example.issues.domain.User;
import com.example.issues.domain.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.util.Set;

@SpringComponent
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Set<User> find(String name, Role role) {
        if (role == null) {
            return userRepository.findByNameContainingIgnoreCase(name);
        } else {
            return userRepository.findByNameContainingIgnoreCaseAndRole(name, role);
        }
    }

}
