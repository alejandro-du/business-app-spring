package com.example.issues.projects;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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

}
