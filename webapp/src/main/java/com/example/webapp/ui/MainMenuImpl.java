package com.example.webapp.ui;

import com.example.common.ui.MainMenu;
import com.example.common.ui.MenuOption;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class MainMenuImpl extends Composite<VerticalLayout> implements MainMenu {

    public MainMenuImpl() {
        getContent().setSizeUndefined();
        getContent().addClassName("main-menu");
    }

    @Override
    public void addOption(Class<? extends Component> viewClass, String text, VaadinIcon icon) {
        addOption(new MenuOption(viewClass, text, icon));
    }

    @Override
    public void addOption(MenuOption option) {
        HorizontalLayout optionLayout = new HorizontalLayout(
                option.getIcon().create(),
                new RouterLink(option.getText(), option.getViewClass())
        );
        optionLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().add(optionLayout);
    }

}
