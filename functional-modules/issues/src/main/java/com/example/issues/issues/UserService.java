package com.example.issues.issues;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("issuesUserService")
public class UserService {

    private final UserRepository userRepository;
    private final IssuesModuleState issuesModuleState;

    public UserService(UserRepository userRepository, IssuesModuleState issuesModuleState) {
        this.userRepository = userRepository;
        this.issuesModuleState = issuesModuleState;
    }

    public Set<User> findByRole(Role role) {
        return userRepository.find(issuesModuleState.getProjectId(), role);
    }

}
