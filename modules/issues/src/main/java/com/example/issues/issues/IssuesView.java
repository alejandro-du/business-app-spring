package com.example.issues.issues;

import com.example.api.ui.MainLayout;
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

@Route(value = IssuesView.VIEW_NAME, layout = MainLayout.class)
public class IssuesView extends Composite<VerticalLayout> {

    public static final String VIEW_NAME = "issues";

    private TextField title = new TextField();
    private TextField reporter = new TextField();
    private TextField owner = new TextField();
    private ComboBox<Status> status = new ComboBox<>(null, Status.values());
    private DatePicker date = new DatePicker();
    private Grid<Issue> grid = new Grid<>();

    private final IssueService issueService;

    public IssuesView(IssueService issueService) {
        this.issueService = issueService;
        Span viewTitle = new Span("Issues");
        viewTitle.addClassName("view-title");

        title.setPlaceholder("Title...");
        title.setWidth("100%");
        title.addValueChangeListener(e -> refreshGrid());
        title.setSizeFull();

        reporter.setPlaceholder("Reporter...");
        reporter.setWidth("100%");
        reporter.addValueChangeListener(e -> refreshGrid());
        reporter.setSizeFull();

        owner.setPlaceholder("Owner...");
        owner.setWidth("100%");
        owner.addValueChangeListener(e -> refreshGrid());
        owner.setSizeFull();

        status.setValue(Status.OPEN);
        status.setPlaceholder("Status...");
        status.setWidth("100%");
        status.addValueChangeListener(e -> refreshGrid());

        date.setPlaceholder("Date...");
        date.setWidth("100%");
        date.addValueChangeListener(e -> refreshGrid());

        HorizontalLayout filterLayout = new HorizontalLayout(title, owner, reporter, status, date);
        filterLayout.setWidth("100%");

        grid.addColumn(Issue::getTitle).setHeader("Title");
        grid.addColumn(i -> i.getOwner() != null ? i.getOwner().getName() : "").setHeader("Owner");
        grid.addColumn(i -> i.getReporter() != null ? i.getReporter().getName() : "").setHeader("Reporter");
        grid.addColumn(Issue::getStatus).setHeader("Status");
        grid.addColumn(Issue::getDate).setHeader("Date");
        grid.addComponentColumn(i -> new HorizontalLayout(
                new Button(VaadinIcon.EYE.create(), e -> UI.getCurrent().navigate(IssueView.getViewName(i.getId()))),
                new Button(VaadinIcon.EDIT.create(), e -> UI.getCurrent().navigate(EditIssueView.getViewName(i.getId())))
        ));
        grid.setSizeFull();

        getContent().add(viewTitle, filterLayout, grid);
        getContent().expand(grid);
        getContent().setSizeFull();

        refreshGrid();
    }

    private void refreshGrid() {
        Set<Issue> issues = issueService.find(
                title.getValue(), owner.getValue(), reporter.getValue(), status.getValue(), date.getValue());
        grid.setItems(issues);
    }

}
