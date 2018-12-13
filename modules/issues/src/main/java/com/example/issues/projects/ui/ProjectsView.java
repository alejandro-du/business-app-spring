package com.example.issues.projects.ui;

import com.example.common.ui.MainLayout;
import com.example.common.ui.Messages;
import com.example.issues.projects.Project;
import com.example.issues.projects.ProjectService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "projects", layout = MainLayout.class)
public class ProjectsView extends Composite<VerticalLayout> {

    public ProjectsView(ProjectService projectService) {
        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.issues.projects") + " | " + Messages.get("com.example.appName"));

        Span viewTitle = new Span(Messages.get("com.example.issues.projects"));
        viewTitle.addClassName("view-title");

        Grid<Project> grid = new Grid<>();
        grid.addColumn(Project::getName).setHeader(Messages.get("com.example.issues.name"));
        grid.addComponentColumn(p -> new Button(null,
                VaadinIcon.EDIT.create(),
                e -> UI.getCurrent().navigate(EditProjectView.class, p.getId())));
        grid.setItems(projectService.findAll());

        getContent().add(viewTitle, grid);
        getContent().setSizeFull();
    }

}
