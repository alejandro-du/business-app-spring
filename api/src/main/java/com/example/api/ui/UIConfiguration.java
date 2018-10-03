package com.example.api.ui;

import com.example.api.service.AuthorizationService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@VaadinSessionScope
public class UIConfiguration {

    private final AuthorizationService authorizationService;

    private List<MenuOption> menuOptions = new ArrayList<>();
    private List<SerializableSupplier<Component>> headerComponentSuppliers = new ArrayList<>();

    public UIConfiguration(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public void addHeaderComponent(SerializableSupplier<Component> componentSupplier) {
        headerComponentSuppliers.add(componentSupplier);
    }

    public boolean addMenuOption(String href, String text) {
        if (authorizationService.userCanAccess(href)) {
            menuOptions.add(new MenuOption(href, text));
            return true;
        }

        return false;
    }

    public List<MenuOption> getMenuOptions() {
        return menuOptions;
    }

    public List<SerializableSupplier<Component>> getHeaderComponentSuppliers() {
        return headerComponentSuppliers;
    }

}
