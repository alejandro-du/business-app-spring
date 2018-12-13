package com.example.issues;

import com.example.common.BusinessAppModule;
import com.example.common.domain.User;
import com.example.common.service.AuthenticationService;
import com.example.common.ui.Messages;
import com.example.common.ui.UIConfiguration;
import com.example.issues.issues.Session;
import com.example.issues.issues.ui.CreateIssueView;
import com.example.issues.issues.ui.IssuesView;
import com.example.issues.projects.Project;
import com.example.issues.projects.ProjectRepository;
import com.example.issues.projects.ui.CreateProjectView;
import com.example.issues.projects.ui.ProjectsView;
import com.example.issues.users.UserRepository;
import com.example.issues.users.ui.UsersView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
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
    private ComboBox<Project> projectsSelector;

    public IssuesModule(UIConfiguration uiConfiguration,
                        ProjectRepository projectRepository,
                        UserRepository userRepository,
                        Session session) {
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
        addHeaderOptions();
        addMenuOptions();
    }

    public void updateProjectsSelector() {
        User user = userRepository.findById(session.getUserId()).get();
        List<Project> projects = projectRepository.findByMembersIn(user);
        projectsSelector.setItems(projects);

        if (!projects.isEmpty()) {
            long projectId = session.getProjectId();
            Optional<Project> project = projects.stream().filter(p -> p.getId().equals(projectId)).findFirst();
            projectsSelector.setValue(project.orElse(projects.get(0)));
        }
    }

    private void setDefaultProject(List<Project> allProjects) {
        if (session.getProjectId() == null && !allProjects.isEmpty()) {
            session.setProjectId(allProjects.get(0).getId());
        }
    }

    private void addHeaderOptions() {
        uiConfiguration.addHeaderComponent(() -> {
            projectsSelector = new ComboBox<>(null);
            projectsSelector.addClassName("no-clear-button");
            projectsSelector.setItemLabelGenerator(Project::getName);
            updateProjectsSelector();
            projectsSelector.addValueChangeListener(e -> selectProject(e.getValue()));
            return projectsSelector;
        });
    }

    private void selectProject(Project selectedProject) {
        if (selectedProject != null) {
            session.setProjectId(selectedProject.getId());
            UI.getCurrent().navigate("");
        }
    }

    private void addMenuOptions() {
        uiConfiguration.addMenuOption(IssuesView.class, Messages.get("com.example.issues.issues"), VaadinIcon.BUG);
        uiConfiguration.addMenuOption(CreateIssueView.class,
                Messages.get("com.example.issues.createIssue"),
                VaadinIcon.PLUS
        );
        uiConfiguration.addMenuOption(ProjectsView.class, Messages.get("com.example.issues.projects"), VaadinIcon.CODE);
        uiConfiguration.addMenuOption(CreateProjectView.class,
                Messages.get("com.example.issues.createProject"),
                VaadinIcon.PLUS_SQUARE_O
        );
        uiConfiguration.addMenuOption(UsersView.class, Messages.get("com.example.issues.users"), VaadinIcon.USERS);
    }

}
