package com.example.issues.issues.ui;

import com.example.api.ui.MainLayout;
import com.example.issues.issues.Issue;
import com.example.issues.issues.IssueService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route(value = IssueView.VIEW_NAME, layout = MainLayout.class)
public class IssueView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    public static final String VIEW_NAME = "issue";

    public static String getViewName(Long issueId) {
        return VIEW_NAME + "/" + issueId;
    }

    final IssueService issueService;

    public IssueView(IssueService issueService) {
        this.issueService = issueService;
    }

    @Override
    public void setParameter(BeforeEvent event, Long issueId) {
        Optional<Issue> issue = issueService.findById(issueId);
        if (!issue.isPresent()) {
            UI.getCurrent().navigate(IssuesView.VIEW_NAME);
            UI.getCurrent().getPage().executeJavaScript("location.reload()");
        } else {
            showIssue(issue.get());
        }
    }

    private void showIssue(Issue issue) {
        Span viewTitle = new Span(issue.getTitle());
        viewTitle.addClassName("view-title");

        Span status = new Span("Status: " + issue.getStatus().toString());
        Span date = new Span("Date: " + issue.getDate());
        Span owner = new Span("Owner: " + (issue.getOwner() != null ? issue.getOwner().getName() : ""));

        HorizontalLayout infoLayout = new HorizontalLayout(status, date, owner);
        infoLayout.setSpacing(true);

        Span description = new Span(issue.getDescription());

        getContent().removeAll();
        getContent().add(viewTitle, infoLayout, description);
        getContent().setSizeFull();
    }

}
