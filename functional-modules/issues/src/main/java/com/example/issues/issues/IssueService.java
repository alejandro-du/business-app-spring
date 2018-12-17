package com.example.issues.issues;

import com.example.common.domain.UserRepository;
import com.example.issues.projects.ProjectRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Component
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssuesModuleState issuesModuleState;

    public IssueService(IssueRepository issueRepository,
                        ProjectRepository projectRepository,
                        UserRepository userRepository,
                        IssuesModuleState issuesModuleState) {
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.issuesModuleState = issuesModuleState;
    }

    public Set<Issue> find(String title, String ownerName, String reporterName, Status status, LocalDate date) {
        return issueRepository.find(
                issuesModuleState.getProjectId(),
                title,
                ownerName.isEmpty() ? null : ownerName,
                reporterName,
                status,
                date);
    }

    public Optional<Issue> findById(Long issueId) {
        return issueRepository.findById(issueId);
    }

    public void update(Issue issue) {
        issueRepository.save(issue);
    }

    public void delete(Issue issue) {
        issueRepository.delete(issue);
    }

    public void create(Issue issue) {
        issue.setStatus(Status.OPEN);
        issue.setDate(LocalDate.now());
        issue.setReporter(userRepository.findById(issuesModuleState.getUserId()).orElse(null));
        issue.setProject(projectRepository.findById(issuesModuleState.getProjectId()).orElse(null));
        update(issue);
    }

}
