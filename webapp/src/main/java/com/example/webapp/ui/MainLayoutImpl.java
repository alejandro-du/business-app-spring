package com.example.webapp.ui;

import com.example.common.ui.Header;
import com.example.common.ui.MainLayout;
import com.example.common.ui.MainMenu;
import com.example.common.ui.UIConfiguration;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Primary;

import java.util.Locale;

@SpringComponent
@UIScope
@Primary
@HtmlImport("/frontend/styles/shared-styles.html")
@HtmlImport("/frontend/styles/main-layout-styles.html")
@Push
public class MainLayoutImpl extends Composite<VerticalLayout> implements MainLayout {

    private final UIConfiguration uiConfiguration;
    private VerticalLayout contentLayout = new VerticalLayout();

    public MainLayoutImpl(Header header, MainMenu mainMenu, UIConfiguration uiConfiguration) {
        this.uiConfiguration = uiConfiguration;

        Locale locale = new Locale(VaadinService.getCurrentRequest().getLocale().getLanguage());
        UI.getCurrent().setLocale(locale);

        if (!this.uiConfiguration.getHeaderComponentSuppliers().isEmpty()) {
            this.uiConfiguration.getHeaderComponentSuppliers()
                    .stream()
                    .map(SerializableSupplier::get)
                    .forEach(header::add);
        }

        this.uiConfiguration.getMenuOptions().stream().forEach(mainMenu::addOption);

        contentLayout.setMargin(false);
        contentLayout.setPadding(false);
        contentLayout.setSpacing(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout((Component) mainMenu, contentLayout);
        horizontalLayout.setSizeFull();
        horizontalLayout.setMargin(false);
        horizontalLayout.setPadding(false);
        horizontalLayout.setSpacing(false);

        getContent().add((Component) header, horizontalLayout);
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
