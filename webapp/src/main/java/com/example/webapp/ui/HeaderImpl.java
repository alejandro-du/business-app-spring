package com.example.webapp.ui;

import com.example.common.service.AuthenticationService;
import com.example.common.ui.Header;
import com.example.common.ui.Messages;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class HeaderImpl extends Composite<HorizontalLayout> implements Header {



    private HorizontalLayout contentLayout = new HorizontalLayout();

    public HeaderImpl(AuthenticationService authenticationService) {
        Image logo = new Image("/frontend/images/app-logo.png", "App logo");
        Span appName = new Span(Messages.get("com.example.appName"));

        contentLayout.addClassName("header-content");
        contentLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        contentLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        getContent().add(logo, appName, contentLayout);
        getContent().setFlexGrow(1, contentLayout);
        getContent().setWidth("100%");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, appName);
        getContent().setSpacing(false);

        getContent().addClassName("header");
        logo.addClassName("header-logo");
        appName.addClassName("header-app-name");
    }


    @Override
    public void add(Component component) {
        contentLayout.add(component);
    }

}
