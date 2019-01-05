package com.example.webapp.ui;

import com.example.common.service.AuthenticationService;
import com.example.common.ui.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import java.util.Locale;

@SpringComponent
@UIScope
@Primary
@HtmlImport("/frontend/styles/shared-styles.html")
@HtmlImport("/frontend/styles/main-layout-styles.html")
@Push
public class MainLayoutImpl extends Composite<VerticalLayout> implements MainLayout {

    private final Header header;
    private final UIConfiguration uiConfiguration;
    private final AuthenticationService authenticationService;
    private VerticalLayout contentLayout = new VerticalLayout();

    public MainLayoutImpl(Header header,
                          MainMenu mainMenu,
                          UIConfiguration uiConfiguration,
                          AuthenticationService authenticationService) {
        this.header = header;
        this.uiConfiguration = uiConfiguration;
        this.authenticationService = authenticationService;

        Locale locale = new Locale(VaadinService.getCurrentRequest().getLocale().getLanguage());
        UI.getCurrent().setLocale(locale);

        Element mainMenuElement = ((Component) mainMenu).getElement();

        Button toggleMenu = new Button(VaadinIcon.MENU.create(), event -> {
            Style mainMenuStyle = mainMenuElement.getStyle();
            Style headerStyle = ((Component) header).getElement().getStyle();

            String currentDisplay = mainMenuStyle.get("display");
            if (currentDisplay == null) {
                mainMenuStyle.set("display", "block");
                headerStyle.set("display", "block");

            } else {
                mainMenuStyle.remove("display");
                headerStyle.remove("display");
            }
        });

        toggleMenu.addClassName("toggle-menu-button");

        mainMenuElement.addEventListener("click", e -> {
            if (toggleMenu.isVisible()) {
                mainMenuElement.getStyle().remove("display");
                ((Component) header).getElement().getStyle().remove("display");
            }
        });

        if (!this.uiConfiguration.getHeaderComponentSuppliers().isEmpty()) {
            this.uiConfiguration.getHeaderComponentSuppliers()
                    .stream()
                    .map(SerializableSupplier::get)
                    .forEach(header::add);
        }

        this.uiConfiguration.getMenuOptions().stream().forEach(mainMenu::addOption);

        contentLayout.setPadding(false);
        contentLayout.setSpacing(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout((Component) mainMenu, contentLayout);
        horizontalLayout.setSizeFull();
        horizontalLayout.setPadding(false);
        horizontalLayout.setSpacing(false);

        getContent().add(toggleMenu, (Component) header, horizontalLayout);
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, horizontalLayout);
        getContent().setSizeFull();
        getContent().setPadding(false);
        getContent().setSpacing(false);
    }

    @PostConstruct
    public void addSignOutOption() {
        if (this.authenticationService.isAuthenticated()) {
            HorizontalLayout signOutLayout = new HorizontalLayout();
            signOutLayout.addClassName("header-sign-out");
            Anchor signOut = new Anchor("/logout", Messages.get("com.example.webapp.signOut"));
            signOut.addClassName("header-signout");
            signOutLayout.add(signOut);
            header.add(signOutLayout);
        }
    }


    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentLayout.removeAll();
        contentLayout.getElement().appendChild(content.getElement());
    }

}
