package com.example.issues.projects.ui;

import com.example.api.domain.User;
import com.example.api.ui.MainLayout;
import com.example.issues.projects.Project;
import com.example.issues.projects.ProjectService;
import com.example.issues.users.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.router.Route;

@Route(value = CreateProjectView.VIEW_NAME, layout = MainLayout.class)
public class CreateProjectView extends Composite<VerticalLayout> {

    public static final String VIEW_NAME = "create-project";

    private TextField name = new TextField("Name");
    private Grid<User> grid = new Grid<>();
    private MultiSelect<Grid<User>, User> members;

    private final ProjectService projectService;
    private final UserService userService;

    public CreateProjectView(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;

        Span viewTitle = new Span("Create project");
        viewTitle.addClassName("view-title");

        name.setSizeFull();

        grid.setId("members");
        grid.setWidth("100%");
        grid.addColumn(User::getName).setHeader("Name");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(User::getRole).setHeader("Role");
        grid.setItems(this.userService.findAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        members = grid.asMultiSelect();

        Label membersLabel = new Label("Members");
        membersLabel.setFor(grid);

        Button create = new Button("Create", e -> create());
        create.getElement().setAttribute("theme", "primary");

        VerticalLayout formLayout = new VerticalLayout(viewTitle, name, membersLabel, grid, create);
        formLayout.setPadding(false);
        formLayout.setMargin(false);

        Div mainLayout = new Div(formLayout);
        mainLayout.setWidth("100%");

        getContent().add(mainLayout);
        getContent().setAlignSelf(FlexComponent.Alignment.END, create);
    }

    private void create() {
        try {
            Project project = new Project();
            BeanValidationBinder<Project> binder = new BeanValidationBinder<>(Project.class);
            binder.bindInstanceFields(this);
            binder.writeBean(project);
            projectService.saveOrUpdate(project);
            UI.getCurrent().navigate(ProjectsView.VIEW_NAME);

        } catch (ValidationException e) {
            Notification.show("Please fix the errors and try again.");
        }
    }

}
