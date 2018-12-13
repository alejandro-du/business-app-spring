package com.example.issues.projects.ui;

import com.example.common.domain.User;
import com.example.common.ui.ConfirmDialog;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route(value = "edit-project", layout = MainLayout.class)
public class EditProjectView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    private TextField name = new TextField(Messages.get("com.example.issues.name"));
    private Grid<User> grid = new Grid<>();
    private MultiSelect<Grid<User>, User> members;

    private final ProjectService projectService;
    private final UserService userService;
    private final IssuesModule issuesModule;
    private BeanValidationBinder<Project> binder = new BeanValidationBinder<>(Project.class);

    public EditProjectView(ProjectService projectService, UserService userService, IssuesModule issuesModule) {
        this.projectService = projectService;
        this.userService = userService;
        this.issuesModule = issuesModule;

        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.issues.editProject") + " | " + Messages.get("com.example.appName"));
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
        Span viewTitle = new Span(Messages.get("com.example.issues.editProject"));
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

        Button delete = new Button(Messages.get("com.example.issues.delete"), e -> delete(project));
        delete.getElement().setAttribute("theme", "error");

        Button save = new Button(Messages.get("com.example.issues.save"), e -> save(project));
        save.getElement().setAttribute("theme", "primary");

        HorizontalLayout actionsLayout = new HorizontalLayout(delete, save);

        VerticalLayout formLayout = new VerticalLayout(viewTitle, name, membersLabel, grid, actionsLayout);
        formLayout.setPadding(false);

        Div mainLayout = new Div(formLayout);
        mainLayout.setWidth("100%");

        getContent().add(mainLayout);
        getContent().setAlignSelf(FlexComponent.Alignment.END, actionsLayout);

        binder.bindInstanceFields(this);
        binder.setBean(project);
    }

    private void delete(Project project) {
        new ConfirmDialog(Messages.get("com.example.issues.deleteProjectConfirmation"),
                Messages.get("com.example.issues.yes"),
                Messages.get("com.example.issues.no"),
                e -> {
                    projectService.delete(project);
                    issuesModule.updateProjectsSelector();
                    UI.getCurrent().navigate(ProjectsView.class);
                }).open();
    }

    private void save(Project project) {
        if (binder.validate().hasErrors()) {
            Notification.show(Messages.get("com.example.issues.validationError"));
        } else {
            projectService.saveOrUpdate(project);
            issuesModule.updateProjectsSelector();
            UI.getCurrent().navigate(ProjectsView.class);
        }
    }

}
