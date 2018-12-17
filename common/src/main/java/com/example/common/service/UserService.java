package com.example.common.service;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import com.example.common.domain.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Set<User> find(String name, Role role) {
        return userRepository.findByNameAndRole(name, role);
    }

    public Optional<User> find(Long id) {
        return userRepository.findById(id);
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void update(User user, boolean newPassword) {
        if (newPassword) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    public void delete(User user) {
        user.setDeleted(true);
        userRepository.save(user);
    }

}
