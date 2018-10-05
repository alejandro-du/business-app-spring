package com.example.api.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainMenu extends Composite<VerticalLayout> {

    public MainMenu() {
        getContent().setSizeUndefined();
        getContent().addClassName("main-menu");
        getContent().setMargin(false);
    }

    public void addOption(Class<? extends Component> viewClass, String text, VaadinIcon icon) {
        HorizontalLayout optionLayout = new HorizontalLayout(icon.create(), new RouterLink(text, viewClass));
        optionLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().add(optionLayout);
    }

    public void addOption(MenuOption menuOption) {
        addOption(menuOption.getViewClass(), menuOption.getText(), menuOption.getIcon());
    }

}
