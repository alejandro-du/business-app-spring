package com.example.issues.issues.ui;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import com.example.common.ui.ConfirmDialog;
import com.example.common.ui.MainLayout;
import com.example.common.ui.Messages;
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
import com.vaadin.flow.router.Route;

import java.util.Optional;
import java.util.Set;

@Route(value = "edit-issue", layout = MainLayout.class)
public class EditIssueView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    private TextField title = new TextField(Messages.get("com.example.issues.title"));
    private TextArea description = new TextArea(Messages.get("com.example.issues.description"));
    private ComboBox<User> owner = new ComboBox<>(Messages.get("com.example.issues.owner"));
    private ComboBox<Status> status = new ComboBox<>(Messages.get("com.example.issues.status"), Status.values());

    private final IssueService issueService;
    private final UserService userService;
    private BeanValidationBinder<Issue> binder = new BeanValidationBinder<>(Issue.class);

    public EditIssueView(IssueService issueService, UserService userService) {
        this.issueService = issueService;
        this.userService = userService;

        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.issues.editIssue") + " | " + Messages.get("com.example.appName"));
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
        Span viewTitle = new Span(Messages.get("com.example.issues.editIssue"));
        viewTitle.addClassName("view-title");

        title.setWidth("100%");
        title.focus();

        description.setWidth("100%");

        Set<User> users = userService.find("", Role.DEVELOPER);
        owner.setItems(users);
        owner.setItemLabelGenerator(u -> u.getName());

        status.setItemLabelGenerator(status -> Messages.get(status.getNameProperty()));

        HorizontalLayout infoLayout = new HorizontalLayout(owner, status);
        infoLayout.setWidth("100%");

        Button delete = new Button(Messages.get("com.example.issues.delete"), e -> delete(issue));
        delete.getElement().setAttribute("theme", "error");

        Button save = new Button(Messages.get("com.example.issues.save"), e -> save(issue));
        save.getElement().setAttribute("theme", "primary");

        HorizontalLayout actionsLayout = new HorizontalLayout(delete, save);

        getContent().removeAll();
        getContent().add(viewTitle, title, description, infoLayout, actionsLayout);
        getContent().setAlignSelf(FlexComponent.Alignment.END, actionsLayout);

        binder.bindInstanceFields(this);
        binder.setBean(issue);
    }

    private void delete(Issue issue) {
        new ConfirmDialog(Messages.get("com.example.issues.deleteIssueConfirmation"),
                Messages.get("com.example.issues.yes"),
                Messages.get("com.example.issues.no"),
                e -> {
                    issueService.delete(issue);
                    UI.getCurrent().navigate(IssuesView.class);
                }).open();
    }

    private void save(Issue issue) {
        if (binder.validate().hasErrors()) {
            Notification.show(Messages.get("com.example.issues.validationError"));
        } else {
            issueService.update(issue);
            UI.getCurrent().navigate(IssuesView.class);
        }
    }

}
