package com.example.webapp.ui;

import com.example.common.service.AuthenticationService;
import com.example.common.ui.Header;
import com.example.common.ui.Messages;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class HeaderImpl extends Composite<HorizontalLayout> implements Header {

    private final AuthenticationService authenticationService;

    private HorizontalLayout contentLayout = new HorizontalLayout();
    private HorizontalLayout signOutLayout = new HorizontalLayout();

    public HeaderImpl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

        Image logo = new Image("/frontend/images/app-logo.png", "App logo");
        logo.addClassName("header-logo");
        Span appName = new Span(Messages.get("com.example.appName"));
        appName.addClassName("header-app-name");

        contentLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        contentLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        getContent().add(logo, appName, contentLayout, signOutLayout);
        getContent().setFlexGrow(1, contentLayout);
        getContent().setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, signOutLayout);
        getContent().addClassName("header");
        getContent().setWidth("100%");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, appName);
    }

    @PostConstruct
    public void addSignOutOption() {
        if (this.authenticationService.isAuthenticated()) {
            Anchor signOut = new Anchor("/logout", Messages.get("com.example.webapp.signOut"));
            signOut.addClassName("header-signout");
            signOutLayout.add(signOut);
        }
    }

    @Override
    public void add(Component component) {
        contentLayout.add(component);
    }

}
