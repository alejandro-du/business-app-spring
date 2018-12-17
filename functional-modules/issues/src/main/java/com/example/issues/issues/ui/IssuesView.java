package com.example.issues.issues.ui;

import com.example.common.domain.Role;
import com.example.common.service.AuthorizationService;
import com.example.common.ui.MainLayout;
import com.example.common.ui.Messages;
import com.example.issues.issues.Issue;
import com.example.issues.issues.IssueService;
import com.example.issues.issues.Status;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.Set;

@Route(value = "issues", layout = MainLayout.class)
public class IssuesView extends Composite<VerticalLayout> {

    private TextField title = new TextField();
    private TextField reporter = new TextField();
    private TextField owner = new TextField();
    private ComboBox<Status> status = new ComboBox<>(null, Status.values());
    private DatePicker date = new DatePicker();
    private Grid<Issue> grid = new Grid<>();

    private final IssueService issueService;

    public IssuesView(IssueService issueService, AuthorizationService authorizationService) {
        this.issueService = issueService;

        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.issues.issues") + " | " + Messages.get("com.example.appName"));

        Span viewTitle = new Span(Messages.get("com.example.issues.issues"));
        viewTitle.addClassName("view-title");

        title.setPlaceholder(Messages.get("com.example.issues.title"));
        title.setWidth("100%");
        title.addValueChangeListener(e -> refreshGrid());
        title.setSizeFull();

        reporter.setPlaceholder(Messages.get("com.example.issues.reporter"));
        reporter.setWidth("100%");
        reporter.addValueChangeListener(e -> refreshGrid());
        reporter.setSizeFull();

        owner.setPlaceholder(Messages.get("com.example.issues.owner"));
        owner.setWidth("100%");
        owner.addValueChangeListener(e -> refreshGrid());
        owner.setSizeFull();

        status.setValue(Status.OPEN);
        status.setPlaceholder(Messages.get("com.example.issues.status"));
        status.setWidth("100%");
        status.addValueChangeListener(e -> refreshGrid());
        status.setItemLabelGenerator(status -> Messages.get(status.getNameProperty()));

        date.setPlaceholder(Messages.get("com.example.issues.date"));
        date.setWidth("100%");

        HorizontalLayout filterLayout = new HorizontalLayout(title, owner, reporter, status, date);
        filterLayout.setWidth("100%");
        grid.addColumn(i -> "#" + i.getId()).setFlexGrow(0);
        grid.addColumn(Issue::getTitle).setHeader(Messages.get("com.example.issues.title")).setFlexGrow(1);
        grid.addColumn(i -> i.getOwner() != null ? i.getOwner().getName() : "")
                .setHeader(Messages.get("com.example.issues.owner"))
                .setFlexGrow(0);
        grid.addColumn(i -> i.getReporter() != null ? i.getReporter().getName() : "")
                .setHeader(Messages.get("com.example.issues.reporter"))
                .setFlexGrow(0);
        grid.addColumn(issue -> Messages.get(issue.getStatus().getNameProperty()))
                .setHeader(Messages.get("com.example.issues.status"))
                .setFlexGrow(0);
        grid.addColumn(Issue::getDate)
                .setHeader(Messages.get("com.example.issues.date"))
                .setFlexGrow(0)
                .setWidth("10em");
        grid.addComponentColumn(i -> new HorizontalLayout(new Button(VaadinIcon.EYE.create(),
                e -> UI.getCurrent().navigate(IssueView.class, i.getId())),
                authorizationService.secureComponent(new Button(VaadinIcon.EDIT.create(),
                        e -> UI.getCurrent().navigate(EditIssueView.class, i.getId())), Role.ADMIN, Role.DEVELOPER)))
                .setFlexGrow(0)
                .setWidth("10em");
        grid.setSizeFull();

        getContent().add(viewTitle, filterLayout, grid);
        getContent().expand(grid);
        getContent().setSizeFull();

        refreshGrid();
    }

    private void refreshGrid() {
        Set<Issue> issues = issueService.find(title.getValue(),
                owner.getValue(),
                reporter.getValue(),
                status.getValue(),
                date.getValue());
        grid.setItems(issues);
    }

}
