package com.example.issues.issues;

import com.example.issues.projects.Project;
import com.example.api.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Component
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public Set<Issue> find(Project project, String title, String ownerName, String reporterName, Status status, LocalDate date) {
        return issueRepository.find(
                project,
                title,
                ownerName.isEmpty() ? null : ownerName,
                reporterName,
                status,
                date
        );
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

    public void create(Issue issue, User reporter, Project project) {
        issue.setStatus(Status.OPEN);
        issue.setDate(LocalDate.now());
        issue.setReporter(reporter);
        issue.setProject(project);
        update(issue);
    }

}
