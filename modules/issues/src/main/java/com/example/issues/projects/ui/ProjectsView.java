package com.example.issues.projects.ui;

import com.example.api.ui.MainLayout;
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

@Route(value = ProjectsView.VIEW_NAME, layout = MainLayout.class)
public class ProjectsView extends Composite<VerticalLayout> {

    public static final String VIEW_NAME = "projects";

    public ProjectsView(ProjectService projectService) {
        Span viewTitle = new Span("Projects");
        viewTitle.addClassName("view-title");

        Grid<Project> grid = new Grid<>();
        grid.addColumn(Project::getName).setHeader("Name");
        grid.addComponentColumn(p -> new Button(null, VaadinIcon.EDIT.create(),
                e -> UI.getCurrent().navigate(EditProjectView.getViewName(p.getId()))));
        grid.setItems(projectService.findAll());

        getContent().add(viewTitle, grid);
        getContent().setSizeFull();
    }

}
