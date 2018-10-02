package com.example.issues.users;

import com.example.api.domain.Role;
import com.example.api.domain.User;
import com.example.issues.issues.Session;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class UserService {

    private final UserRepository userRepository;

    private final Session session;

    public UserService(UserRepository userRepository, Session session) {
        this.userRepository = userRepository;
        this.session = session;
    }

    public Set<User> find(String name, Role role) {
        return userRepository.find(session.getProjectId(), name, role);
    }

    public void saveOrUpdate(User user) {
        userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

}
