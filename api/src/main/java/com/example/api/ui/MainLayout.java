package com.example.api.ui;

import com.example.api.BusinessAppModule;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.Arrays;

@SpringComponent
@UIScope
@HtmlImport("/frontend/styles/shared-styles.html")
public class MainLayout extends Composite<VerticalLayout> implements RouterLayout {

    private VerticalLayout contentLayout = new VerticalLayout();

    public MainLayout(Header header, MainMenu mainMenu, BusinessAppModule[] modules) {
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

        Arrays.stream(modules).forEach(BusinessAppModule::initialize);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentLayout.getElement().appendChild(content.getElement());
    }

}
