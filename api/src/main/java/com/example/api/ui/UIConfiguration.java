package com.example.api.ui;

import com.example.api.service.AuthorizationService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
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

    public boolean addMenuOption(Class<? extends Component> viewClass, String text, VaadinIcon icon) {
        if (authorizationService.userCanAccess(viewClass)) {
            menuOptions.add(new MenuOption(viewClass, text, icon));
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
