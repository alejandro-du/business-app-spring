package com.example.api.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MainMenu extends Composite<VerticalLayout> {

    public MainMenu() {
        getContent().setSizeUndefined();
        getContent().addClassName("main-menu");
        getContent().add(new Button("navigateTo", e -> UI.getCurrent().navigate("users")));
    }

    public void addOption(String href, String text) {
        getContent().add(new Anchor(href, text));
    }

    public void addOption(MenuOption menuOption) {
        addOption(menuOption.getHref(), menuOption.getText());
    }

}
