package com.example.issues.issues.ui;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import com.example.common.service.AuthorizationService;
import com.example.common.ui.MainLayout;
import com.example.common.ui.Messages;
import com.example.issues.issues.Issue;
import com.example.issues.issues.IssueService;
import com.example.issues.users.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import java.util.Set;

@Route(value = "create-issue", layout = MainLayout.class)
public class CreateIssueView extends Composite<VerticalLayout> {

    private TextField title = new TextField(Messages.get("com.example.issues.title"));
    private TextArea description = new TextArea(Messages.get("com.example.issues.description"));
    private ComboBox<User> owner = new ComboBox<>();

    private final IssueService issueService;
    private final AuthorizationService authorizationService;

    public CreateIssueView(IssueService issueService,
                           UserService userService,
                           AuthorizationService authorizationService) {
        this.issueService = issueService;
        this.authorizationService = authorizationService;

        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.issues.createIssue") + " | " + Messages.get("com.example.appName"));

        Span viewTitle = new Span(Messages.get("com.example.issues.createIssue"));
        viewTitle.addClassName("view-title");

        title.setSizeFull();
        title.focus();

        description.setSizeFull();
        description.setHeight("20em");

        Set<User> users = userService.findByRole(Role.DEVELOPER);
        owner.setItems(users);
        owner.setItemLabelGenerator(u -> u.getName());
        owner.setPlaceholder(Messages.get("com.example.issues.owner"));

        Button create = new Button(Messages.get("com.example.issues.create"), e -> create());
        create.getElement().setAttribute("theme", "primary");

        VerticalLayout formLayout = new VerticalLayout(viewTitle,
                title,
                description,
                authorizationService.secureComponent(owner, Role.ADMIN, Role.DEVELOPER),
                create);
        formLayout.setPadding(false);
        formLayout.setMargin(false);
        formLayout.setAlignSelf(FlexComponent.Alignment.END, owner, create);

        Div mainLayout = new Div(formLayout);
        mainLayout.setWidth("100%");
        getContent().add(mainLayout);
    }

    private void create() {
        try {
            Issue issue = new Issue();
            BeanValidationBinder<Issue> binder = new BeanValidationBinder<>(Issue.class);
            binder.bindInstanceFields(this);
            binder.writeBean(issue);
            issueService.create(issue);
            UI.getCurrent().navigate(IssueView.class, issue.getId());

        } catch (ValidationException e) {
            Notification.show(Messages.get("com.example.issues.validationError"));
        }
    }

}
