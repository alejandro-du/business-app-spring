package com.example.issues.projects;

import com.example.api.domain.User;
import com.example.api.domain.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    public void delete(Project project) {
        projectRepository.delete(project);
    }

    public void saveOrUpdate(Project project) {
        projectRepository.save(project);
    }

    public List<Project> findByUser(Long id) {
        User user = userRepository.findById(id).get();
        return projectRepository.findByMembersIn(user);
    }

}
