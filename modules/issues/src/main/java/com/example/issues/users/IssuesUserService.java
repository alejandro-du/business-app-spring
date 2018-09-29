package com.example.issues.users;

import com.example.api.domain.Role;
import com.example.api.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component("issuesUserService")
public class IssuesUserService {

    private final UserRepository userRepository;

    public IssuesUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Set<User> find(String name, Role role) {
        return userRepository.find(name, role);
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
