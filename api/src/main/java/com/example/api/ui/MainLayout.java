package com.example.api.ui;

import com.example.api.service.AuthenticationService;
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
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@UIScope
@HtmlImport("/frontend/styles/shared-styles.html")
@HtmlImport("/frontend/styles/main-menu-styles.html")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainLayout extends Composite<VerticalLayout> implements RouterLayout {

    private VerticalLayout contentLayout = new VerticalLayout();

    public MainLayout(AuthenticationService authenticationService, UIConfiguration uiConfiguration) {
        Anchor signOut = new Anchor("/logout", Messages.get("com.example.webapp.signOut"));
        signOut.addClassName("header-signout");

        Header header = new Header(Messages.get("com.example.appName"));

        if (!uiConfiguration.getHeaderComponentSuppliers().isEmpty()) {
            uiConfiguration.getHeaderComponentSuppliers().stream()
                    .map(SerializableSupplier::get)
                    .forEach(header::add);
        }

        if (authenticationService.isAuthenticated()) {
            header.add(signOut);
        }

        MainMenu mainMenu = new MainMenu();
        uiConfiguration.getMenuOptions().stream()
                .forEach(mainMenu::addOption);

        contentLayout.setMargin(false);
        contentLayout.setPadding(false);
        contentLayout.setSpacing(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout(mainMenu, contentLayout);
        horizontalLayout.setSizeFull();
        horizontalLayout.setMargin(false);
        horizontalLayout.setPadding(false);
        horizontalLayout.setSpacing(false);

        getContent().add(header, horizontalLayout);
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, horizontalLayout);
        getContent().setSizeFull();
        getContent().setPadding(false);
        getContent().setSpacing(false);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentLayout.removeAll();
        contentLayout.getElement().appendChild(content.getElement());
    }

}
