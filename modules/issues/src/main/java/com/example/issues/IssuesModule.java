package com.example.issues;

import com.example.api.BusinessAppModule;
import com.example.api.ui.MainMenu;
import com.example.issues.issues.CreateIssueView;
import com.example.issues.projects.CreateProjectView;
import com.example.issues.issues.IssuesView;
import com.example.issues.projects.ProjectsView;
import com.example.issues.users.UsersView;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class IssuesModule implements BusinessAppModule {

    private final MainMenu mainMenu;

    public IssuesModule(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    public void initialize() {
        mainMenu.addOption(IssuesView.VIEW_NAME, "Issues");
        mainMenu.addOption(CreateIssueView.VIEW_NAME, "Create issue");
        mainMenu.addOption(ProjectsView.VIEW_NAME, "Projects");
        mainMenu.addOption(CreateProjectView.VIEW_NAME, "Create project");
        mainMenu.addOption(UsersView.VIEW_NAME, "Users");
    }

}
