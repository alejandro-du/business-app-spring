package com.example.issues.issues;

import com.example.issues.projects.ProjectRepository;
import com.example.issues.users.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Component
@SessionScope
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final Session session;

    public IssueService(IssueRepository issueRepository, ProjectRepository projectRepository, UserRepository userRepository, Session session) {
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.session = session;
    }

    public Set<Issue> find(String title, String ownerName, String reporterName, Status status, LocalDate date) {
        return issueRepository.find(
                session.getProjectId(),
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

    public void create(Issue issue) {
        issue.setStatus(Status.OPEN);
        issue.setDate(LocalDate.now());
        issue.setReporter(userRepository.findById(session.getUserId()).orElse(null));
        issue.setProject(projectRepository.findById(session.getProjectId()).orElse(null));
        update(issue);
    }

}
