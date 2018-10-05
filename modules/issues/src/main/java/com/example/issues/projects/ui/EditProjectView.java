package com.example.issues.projects.ui;

import com.example.api.domain.User;
import com.example.api.ui.ConfirmDialog;
import com.example.api.ui.MainLayout;
import com.example.issues.projects.Project;
import com.example.issues.projects.ProjectService;
import com.example.issues.users.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route(value = "edit-project", layout = MainLayout.class)
@PageTitle("Edit project | Business Application")
public class EditProjectView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    private TextField name = new TextField("Name");
    private Grid<User> grid = new Grid<>();
    private MultiSelect<Grid<User>, User> members;

    private final ProjectService projectService;
    private final UserService userService;
    private BeanValidationBinder<Project> binder = new BeanValidationBinder<>(Project.class);

    public EditProjectView(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @Override
    public void setParameter(BeforeEvent event, Long projectId) {
        Optional<Project> project = projectService.findById(projectId);
        if (!project.isPresent()) {
            UI.getCurrent().navigate(ProjectsView.class);
        } else {
            editProject(project.get());
        }
    }

    private void editProject(Project project) {
        Span viewTitle = new Span("Edit project");
        viewTitle.addClassName("view-title");

        name.setWidth("100%");
        name.focus();

        grid.setId("members");
        grid.setSizeFull();
        grid.addColumn(User::getName).setHeader("Name");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(User::getRole).setHeader("Role");
        grid.setItems(userService.findAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        members = grid.asMultiSelect();

        Label membersLabel = new Label("Members");
        membersLabel.setFor(grid);

        Button delete = new Button("Delete...", e -> delete(project));
        delete.getElement().setAttribute("theme", "error");

        Button save = new Button("Save", e -> save(project));
        save.getElement().setAttribute("theme", "primary");

        HorizontalLayout actionsLayout = new HorizontalLayout(delete, save);

        getContent().setSizeFull();
        getContent().removeAll();
        getContent().add(viewTitle, name, membersLabel, grid, actionsLayout);
        getContent().setAlignSelf(FlexComponent.Alignment.END, actionsLayout);

        binder.bindInstanceFields(this);
        binder.setBean(project);
    }

    private void delete(Project project) {
        new ConfirmDialog("Do you want to delete this project?", e -> {
            projectService.delete(project);
            UI.getCurrent().navigate(ProjectsView.class);
        }).open();
    }

    private void save(Project project) {
        if (binder.validate().hasErrors()) {
            Notification.show("Please fix the errors and try again.");
        } else {
            projectService.saveOrUpdate(project);
            UI.getCurrent().navigate(ProjectsView.class);
        }
    }

}
