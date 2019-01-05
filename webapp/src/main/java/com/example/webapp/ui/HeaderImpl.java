package com.example.webapp.ui;

import com.example.common.ui.Header;
import com.example.common.ui.MainMenu;
import com.example.common.ui.Messages;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class HeaderImpl extends Composite<HorizontalLayout> implements Header {

    private HorizontalLayout contentLayout = new HorizontalLayout();

    public HeaderImpl(MainMenu mainMenu) {
        Image logo = new Image("/frontend/images/app-logo.png", "App logo");
        Span appName = new Span(Messages.get("com.example.appName"));

        contentLayout.addClassName("header-content");
        contentLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        contentLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Element mainMenuElement = ((Component) mainMenu).getElement();

        Style mainMenuStyle = mainMenuElement.getStyle();
        Style contentStyle = contentLayout.getElement().getStyle();

        Button toggleMenuButton = new Button(VaadinIcon.MENU.create(), event -> {
            String currentDisplay = mainMenuStyle.get("display");

            if (currentDisplay == null) {
                mainMenuStyle.set("display", "block");
                contentStyle.set("display", "flex");

            } else {
                mainMenuStyle.remove("display");
                contentStyle.remove("display");
            }
        });

        toggleMenuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        toggleMenuButton.addClassName("toggle-menu-button");

        mainMenuElement.addEventListener("click", e -> {
            if (toggleMenuButton.isVisible()) {
                mainMenuStyle.remove("display");
                contentStyle.remove("display");
            }
        });

        getContent().add(toggleMenuButton, logo, appName, contentLayout);
        getContent().setFlexGrow(1, contentLayout);
        getContent().setWidth("100%");
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
