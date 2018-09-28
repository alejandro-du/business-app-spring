package com.example.api.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@VaadinSessionScope
public class UIConfiguration {

    @Data
    @AllArgsConstructor
    public class MenuOption {
        private String href;
        private String text;
    }

    private List<MenuOption> menuOptions = new ArrayList<>();
    private List<SerializableSupplier<Component>> headerComponentSuppliers = new ArrayList<>();

    public void addHeaderComponent(SerializableSupplier<Component> componentSupplier) {
        headerComponentSuppliers.add(componentSupplier);
    }

    public void addMenuOption(String href, String text) {
        menuOptions.add(new MenuOption(href, text));
    }

    public List<MenuOption> getMenuOptions() {
        return menuOptions;
    }

    public List<SerializableSupplier<Component>> getHeaderComponentSuppliers() {
        return headerComponentSuppliers;
    }

}
