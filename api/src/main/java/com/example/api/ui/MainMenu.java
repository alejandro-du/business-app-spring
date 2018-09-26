package com.example.api.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
@HtmlImport("/frontend/styles/main-menu-styles.html")
public class MainMenu extends Composite<VerticalLayout> {

    public MainMenu() {
        getContent().setSizeUndefined();
        getContent().addClassName("main-menu");
    }

    public void addOption(String href, String text) {
        getContent().add(new Anchor(href, text));
    }

}
