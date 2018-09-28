package com.example.api.ui;

import com.example.api.service.AuthService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class Header extends Composite<HorizontalLayout> {

    private final AuthService authService;

    public Header(AuthService authService) {
        this.authService = authService;

        Image logo = new Image("/frontend/images/app-logo.png", "App logo");
        Span appName = new Span("App name");
        appName.addClassNames("header-app-name");

        Anchor signOut = new Anchor("", "Sign out");
        signOut.getElement().addEventListener("click", e -> authService.logout());

        HorizontalLayout signOutLayout = new HorizontalLayout(signOut);
        signOutLayout.setPadding(true);
        signOutLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        signOutLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        getContent().add(logo, appName, signOutLayout);
        getContent().setFlexGrow(1, signOutLayout);
        getContent().addClassName("header");
        getContent().setWidth("100%");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, appName);

        if (!authService.isAuthenticated()) {
            signOut.setVisible(false);
        }
    }

}
