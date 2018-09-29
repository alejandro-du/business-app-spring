package com.example.api.service;

import com.example.api.domain.User;
import com.example.api.domain.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailIgnoreCaseAndPassword(email, password);
    }

}
