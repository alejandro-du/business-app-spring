package com.example.issues.users.ui;

import com.example.api.domain.Role;
import com.example.api.domain.User;
import com.example.api.ui.MainLayout;
import com.example.issues.users.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Set;

@Route(value = UsersView.VIEW_NAME, layout = MainLayout.class)
@PageTitle("Users | Business Application")
public class UsersView extends Composite<VerticalLayout> {

    public static final String VIEW_NAME = "users";

    private TextField name = new TextField();
    private ComboBox<Role> role = new ComboBox();
    private Grid<User> grid = new Grid();

    private final UserService userService;

    public UsersView(UserService userService) {
        this.userService = userService;
        Span viewTitle = new Span("Users");
        viewTitle.addClassName("view-title");

        name.setHeight("100%");
        name.setPlaceholder("Name...");
        name.addValueChangeListener(e -> refreshGrid());

        role.setPlaceholder("Role...");
        role.setItems(Role.values());
        role.addValueChangeListener(e -> refreshGrid());

        Anchor createNew = new Anchor(CreateUserView.VIEW_NAME, "Create new");

        HorizontalLayout actionsLayout = new HorizontalLayout(createNew);
        actionsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        HorizontalLayout filterLayout = new HorizontalLayout(name, role, actionsLayout);
        filterLayout.setWidth("100%");
        filterLayout.setFlexGrow(1, actionsLayout);

        grid.addColumn(User::getName).setHeader("Name");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(User::getRole).setHeader("Role");
        grid.addComponentColumn(u -> new Button(VaadinIcon.EDIT.create(), e -> UI.getCurrent().navigate(EditUserView.getViewName(u.getId()))));

        getContent().add(viewTitle, filterLayout, grid);
        getContent().setSizeFull();

        refreshGrid();
    }

    private void refreshGrid() {
        Set<User> users = userService.find(name.getValue(), role.getValue());
        grid.setItems(users);
    }

}
