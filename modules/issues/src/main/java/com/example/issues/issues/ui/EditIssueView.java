package com.example.issues.issues.ui;

import com.example.api.domain.Role;
import com.example.api.domain.User;
import com.example.api.ui.ConfirmDialog;
import com.example.api.ui.MainLayout;
import com.example.issues.issues.Issue;
import com.example.issues.issues.IssueService;
import com.example.issues.issues.Status;
import com.example.issues.users.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Optional;
import java.util.Set;

@Route(value = "edit-issue", layout = MainLayout.class)
@PageTitle("Edit issue | Business Application")
public class EditIssueView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    private TextField title = new TextField("Title");
    private TextArea description = new TextArea("Description");
    private ComboBox<User> owner = new ComboBox<>();
    private ComboBox<Status> status = new ComboBox<>(null, Status.values());

    private final IssueService issueService;
    private final UserService userService;
    private BeanValidationBinder<Issue> binder = new BeanValidationBinder<>(Issue.class);

    public EditIssueView(IssueService issueService, UserService userService) {
        this.issueService = issueService;
        this.userService = userService;
    }

    @Override
    public void setParameter(BeforeEvent event, Long issueId) {
        Optional<Issue> issue = issueService.findById(issueId);
        if (!issue.isPresent()) {
            UI.getCurrent().navigate(IssuesView.class);
        } else {
            editIssue(issue.get());
        }
    }

    private void editIssue(Issue issue) {
        Span viewTitle = new Span("Edit issue");
        viewTitle.addClassName("view-title");

        title.setWidth("100%");
        title.focus();

        description.setWidth("100%");

        Set<User> users = userService.find("", Role.DEVELOPER);
        owner.setItems(users);
        owner.setItemLabelGenerator(u -> u.getName());

        HorizontalLayout infoLayout = new HorizontalLayout(owner, status);
        infoLayout.setWidth("100%");

        Button delete = new Button("Delete...", e -> delete(issue));
        delete.getElement().setAttribute("theme", "error");

        Button save = new Button("Save", e -> save(issue));
        save.getElement().setAttribute("theme", "primary");

        HorizontalLayout actionsLayout = new HorizontalLayout(delete, save);

        getContent().removeAll();
        getContent().add(viewTitle, title, description, infoLayout, actionsLayout);
        getContent().setAlignSelf(FlexComponent.Alignment.END, actionsLayout);

        binder.bindInstanceFields(this);
        binder.setBean(issue);
    }

    private void delete(Issue issue) {
        new ConfirmDialog("Do you want to delete this issue?", e -> {
            issueService.delete(issue);
            UI.getCurrent().navigate(IssuesView.class);
        }).open();
    }

    private void save(Issue issue) {
        if (binder.validate().hasErrors()) {
            Notification.show("Please fix the errors and try again.");
        } else {
            issueService.update(issue);
            UI.getCurrent().navigate(IssuesView.class);
        }
    }

}
