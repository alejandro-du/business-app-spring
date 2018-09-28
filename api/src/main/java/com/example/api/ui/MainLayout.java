package com.example.api.ui;

import com.example.api.service.AuthService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@HtmlImport("/frontend/styles/shared-styles.html")
@HtmlImport("/frontend/styles/main-menu-styles.html")
public class MainLayout extends Composite<VerticalLayout> implements RouterLayout {

    private VerticalLayout contentLayout = new VerticalLayout();

    public MainLayout(AuthService authService, UIConfiguration uiConfiguration) {
        Anchor signOut = new Anchor("", "Sign out");
        signOut.getElement().getStyle().set("padding-right", "1em");
        signOut.getElement().addEventListener("click", e -> authService.logout());

        Header header = new Header();

        uiConfiguration.getHeaderComponentSuppliers().stream()
                .map(SerializableSupplier::get)
                .forEach(header::add);

        if (authService.isAuthenticated()) {
            header.add(signOut);
        }

        MainMenu mainMenu = new MainMenu();
        uiConfiguration.getMenuOptions().stream()
                .forEach(mainMenu::addOption);

        contentLayout.setMargin(false);
        contentLayout.setPadding(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout(mainMenu, contentLayout);
        horizontalLayout.setSizeFull();
        horizontalLayout.setMargin(false);
        horizontalLayout.setPadding(false);
        horizontalLayout.setSpacing(false);

        getContent().add(header, horizontalLayout);
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, horizontalLayout);
        getContent().setSizeFull();
        getContent().setPadding(false);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentLayout.removeAll();
        contentLayout.getElement().appendChild(content.getElement());
    }

}
