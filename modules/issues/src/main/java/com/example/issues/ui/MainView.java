package com.example.issues.ui;

import com.example.api.ui.MainLayout;
import com.example.issues.domain.User;
import com.example.issues.service.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.Set;

@Route(layout = MainLayout.class)
public class MainView extends Composite<VerticalLayout> {

    public MainView(UserService userService) {
        Grid<User> grid = new Grid<>(User.class);
        getContent().add(grid);

        Set<User> users = userService.find("", null);
        grid.setItems(users);
    }

}
