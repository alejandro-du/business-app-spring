package com.example.api.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class Header extends Composite<HorizontalLayout> {

    public Header() {
        Image logo = new Image("/frontend/images/app-logo.png", "App logo");
        Span appName = new Span("App name");
        appName.addClassNames("header-app-name");

        getContent().add(logo, appName);
        getContent().addClassName("header");
        getContent().setWidth("100%");
        getContent().setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, appName);
    }

}
