package com.example.issues;

import com.example.api.BusinessAppModule;
import com.example.api.domain.User;
import com.example.api.service.AuthenticationService;
import com.example.api.ui.UIConfiguration;
import com.example.issues.issues.Session;
import com.example.issues.issues.ui.CreateIssueView;
import com.example.issues.issues.ui.IssuesView;
import com.example.issues.projects.Project;
import com.example.issues.projects.ProjectRepository;
import com.example.issues.projects.ProjectService;
import com.example.issues.projects.ui.CreateProjectView;
import com.example.issues.projects.ui.ProjectsView;
import com.example.issues.users.UserRepository;
import com.example.issues.users.ui.UsersView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@VaadinSessionScope
public class IssuesModule implements BusinessAppModule {

    private final UIConfiguration uiConfiguration;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final Session session;

    public IssuesModule(UIConfiguration uiConfiguration, ProjectService projectService, ProjectRepository projectRepository, UserRepository userRepository, Session session) {
        this.uiConfiguration = uiConfiguration;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.session = session;
    }

    @Override
    public void initialize() {
        Long userId = (Long) VaadinSession.getCurrent().getAttribute(AuthenticationService.USER_ID_SESSION_KEY);
        User user = userRepository.findById(userId).get();
        session.setUserId(userId);
        session.setRole(user.getRole());

        List<Project> projects = projectRepository.findByMembersIn(user);
        setDefaultProject(projects);
        addHeaderOptions(projects);
        addMenuOptions();
    }

    private void setDefaultProject(List<Project> allProjects) {
        if (session.getProjectId() == null && !allProjects.isEmpty()) {
            session.setProjectId(allProjects.get(0).getId());
        }
    }

    private void addHeaderOptions(List<Project> allProjects) {
        uiConfiguration.addHeaderComponent(() -> {
            ComboBox<Project> projects = new ComboBox<>(null, allProjects);
            projects.setItemLabelGenerator(Project::getName);
            Optional<Project> project = projectRepository.findById(session.getProjectId());
            projects.setValue(project.orElse(null));

            projects.addValueChangeListener(e -> selectProject(e.getValue()));
            return projects;
        });
    }

    private void selectProject(Project selectedProject) {
        if (selectedProject != null) {
            session.setProjectId(selectedProject.getId());
            UI.getCurrent().getPage().executeJavaScript("location.reload();");
        }
    }

    private void addMenuOptions() {
        uiConfiguration.addMenuOption(IssuesView.VIEW_NAME, "Issues");
        uiConfiguration.addMenuOption(CreateIssueView.VIEW_NAME, "Create issue");
        uiConfiguration.addMenuOption(ProjectsView.VIEW_NAME, "Projects");
        uiConfiguration.addMenuOption(CreateProjectView.VIEW_NAME, "Create project");
        uiConfiguration.addMenuOption(UsersView.VIEW_NAME, "Users");
    }

}
