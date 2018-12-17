package com.example.issues.issues.ui;

import com.example.common.ui.MainLayout;
import com.example.common.ui.Messages;
import com.example.issues.issues.Issue;
import com.example.issues.issues.IssueService;
import com.example.issues.issues.Status;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route(value = "issue", layout = MainLayout.class)
public class IssueView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    final IssueService issueService;

    public IssueView(IssueService issueService) {
        this.issueService = issueService;

        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.issues.issue") + " | " + Messages.get("com.example.appName"));
    }

    @Override
    public void setParameter(BeforeEvent event, Long issueId) {
        Optional<Issue> issue = issueService.findById(issueId);
        if (!issue.isPresent()) {
            UI.getCurrent().navigate(IssuesView.class);
            UI.getCurrent().getPage().executeJavaScript("location.reload()");
        } else {
            showIssue(issue.get());
        }
    }

    private void showIssue(Issue issue) {
        Span viewTitle = new Span("#" + issue.getId() + " - " + issue.getTitle());
        viewTitle.addClassName("view-title");

        Span status = new Span(
                Messages.get("com.example.issues.status") + ": " + Messages.get(issue.getStatus().getNameProperty()));
        status.addClassName("issue-view-status");
        if (Status.OPEN.equals(issue.getStatus())) {
            status.addClassName("red");
        } else {
            status.addClassName("green");
        }

        Span owner = new Span(Messages.get("com.example.issues.owner") + ": " +
                (issue.getOwner() != null ? issue.getOwner().getName() : "?"));
        owner.addClassName("issue-view-owner");
        if (issue.getOwner() != null) {
            owner.addClassName("blue");
        } else {
            owner.addClassName("red");
        }

        Span date = new Span(Messages.get("com.example.issues.date") + ": " + issue.getDate());
        date.addClassName("issue-view-date");

        HorizontalLayout infoLayout = new HorizontalLayout(status, owner, date);
        infoLayout.setSpacing(true);

        Span description = new Span(issue.getDescription());

        getContent().removeAll();
        getContent().add(viewTitle, infoLayout, description);
        getContent().setSizeFull();
    }

}
