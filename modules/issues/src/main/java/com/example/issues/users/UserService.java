package com.example.issues.users;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import com.example.issues.issues.Session;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final Session session;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, Session session, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.session = session;
        this.passwordEncoder = passwordEncoder;
    }

    public Set<User> find(String name, Role role) {
        return userRepository.find(session.getProjectId(), name, role);
    }

    public Set<User> findByRole(Role role) {
        return userRepository.find(session.getProjectId(), "", role);
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

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void delete(User user) {
        user.setDeleted(true);
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

}
