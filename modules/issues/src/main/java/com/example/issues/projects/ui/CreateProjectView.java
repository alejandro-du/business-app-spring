package com.example.issues.projects.ui;

import com.example.common.domain.User;
import com.example.common.ui.MainLayout;
import com.example.common.ui.Messages;
import com.example.issues.IssuesModule;
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

@Route(value = "create-project", layout = MainLayout.class)
public class CreateProjectView extends Composite<VerticalLayout> {

    private TextField name = new TextField(Messages.get("com.example.issues.name"));
    private Grid<User> grid = new Grid<>();
    private MultiSelect<Grid<User>, User> members;

    private final ProjectService projectService;
    private final UserService userService;
    private final IssuesModule issuesModule;

    public CreateProjectView(ProjectService projectService, UserService userService, IssuesModule issuesModule) {
        this.projectService = projectService;
        this.userService = userService;
        this.issuesModule = issuesModule;

        UI.getCurrent()
                .getPage()
                .setTitle(
                        Messages.get("com.example.issues.createProject") + " | " + Messages.get("com.example.appName"));

        Span viewTitle = new Span(Messages.get("com.example.issues.createProject"));
        viewTitle.addClassName("view-title");

        name.setSizeFull();
        name.focus();

        grid.setId("members");
        grid.setWidth("100%");
        grid.addColumn(User::getName).setHeader(Messages.get("com.example.issues.name"));
        grid.addColumn(User::getEmail).setHeader(Messages.get("com.example.issues.email"));
        grid.addColumn(user -> Messages.get(user.getRole().getNameProperty()))
                .setHeader(Messages.get("com.example.issues.role"));
        grid.setItems(this.userService.findAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        members = grid.asMultiSelect();

        Label membersLabel = new Label(Messages.get("com.example.issues.members"));
        membersLabel.setFor(grid);

        Button create = new Button(Messages.get("com.example.issues.create"), e -> create());
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
            issuesModule.updateProjectsSelector();
            UI.getCurrent().navigate(ProjectsView.class);

        } catch (ValidationException e) {
            Notification.show(Messages.get("com.example.issues.validationError"));
        }
    }

}
