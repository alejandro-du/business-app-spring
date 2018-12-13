package com.example.common.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;

public interface MainMenu {

    void addOption(Class<? extends Component> viewClass, String text, VaadinIcon icon);

    void addOption(MenuOption menuOption);

}
