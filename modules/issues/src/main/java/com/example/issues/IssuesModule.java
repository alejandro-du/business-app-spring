package com.example.issues;

import com.example.api.BusinessAppModule;
import com.example.api.ui.UIConfiguration;
import com.example.issues.issues.CreateIssueView;
import com.example.issues.issues.IssuesView;
import com.example.issues.issues.Session;
import com.example.issues.projects.CreateProjectView;
import com.example.issues.projects.Project;
import com.example.issues.projects.ProjectService;
import com.example.issues.projects.ProjectsView;
import com.example.issues.users.UsersView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@VaadinSessionScope
public class IssuesModule implements BusinessAppModule {

    private final UIConfiguration uiConfiguration;
    private final ProjectService projectService;

    public IssuesModule(UIConfiguration uiConfiguration, ProjectService projectService) {
        this.uiConfiguration = uiConfiguration;
        this.projectService = projectService;
    }

    @Override
    public void initialize() {
        List<Project> projects = projectService.findByUser(Session.getUserId());
        setDefaultProject(projects);
        addHeaderOptions(projects);
        addMenuOptions();
    }

    private void setDefaultProject(List<Project> allProjects) {
        if (Session.getProject() == null && !allProjects.isEmpty()) {
            Session.setProject(allProjects.get(0));
        }
    }

    private void addHeaderOptions(List<Project> allProjects) {
        uiConfiguration.addHeaderComponent(() -> {
            ComboBox<Project> projects = new ComboBox<>(null, allProjects);
            projects.setItemLabelGenerator(Project::getName);
            projects.setValue(Session.getProject());

            projects.addValueChangeListener(e -> {
                Session.setProject(e.getValue());
                UI.getCurrent().getPage().executeJavaScript("location.reload();");
            });
            return projects;
        });
    }

    private void addMenuOptions() {
        uiConfiguration.addMenuOption(IssuesView.VIEW_NAME, "Issues");
        uiConfiguration.addMenuOption(CreateIssueView.VIEW_NAME, "Create issue");
        uiConfiguration.addMenuOption(ProjectsView.VIEW_NAME, "Projects");
        uiConfiguration.addMenuOption(CreateProjectView.VIEW_NAME, "Create project");
        uiConfiguration.addMenuOption(UsersView.VIEW_NAME, "Users");
    }

}
