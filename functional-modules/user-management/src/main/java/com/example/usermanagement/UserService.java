package com.example.usermanagement;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("usermanagementUserService")
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Set<User> find(String name, Role role) {
        return userRepository.find(name, role);
    }

}
