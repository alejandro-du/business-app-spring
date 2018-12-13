package com.example.issues.users.ui;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import com.example.common.ui.MainLayout;
import com.example.common.ui.Messages;
import com.example.issues.users.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.Set;

@Route(value = "users", layout = MainLayout.class)
public class UsersView extends Composite<VerticalLayout> {

    private TextField name = new TextField();
    private ComboBox<Role> role = new ComboBox();
    private Grid<User> grid = new Grid();

    private final UserService userService;

    public UsersView(UserService userService) {
        this.userService = userService;

        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.issues.users") + " | " + Messages.get("com.example.appName"));

        Span viewTitle = new Span(Messages.get("com.example.issues.users"));
        viewTitle.addClassName("view-title");

        name.setHeight("100%");
        name.setPlaceholder(Messages.get("com.example.issues.name"));
        name.addValueChangeListener(e -> refreshGrid());

        role.setPlaceholder(Messages.get("com.example.issues.role"));
        role.setItems(Role.values());
        role.setItemLabelGenerator(role -> Messages.get(role.getNameProperty()));
        role.addValueChangeListener(e -> refreshGrid());

        RouterLink createNew = new RouterLink(Messages.get("com.example.issues.create"), CreateUserView.class);

        HorizontalLayout actionsLayout = new HorizontalLayout(VaadinIcon.PLUS.create(), createNew);
        actionsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        HorizontalLayout filterLayout = new HorizontalLayout(name, role, actionsLayout);
        filterLayout.setWidth("100%");
        filterLayout.setFlexGrow(1, actionsLayout);

        grid.addColumn(User::getName).setHeader(Messages.get("com.example.issues.name"));
        grid.addColumn(User::getEmail).setHeader(Messages.get("com.example.issues.email"));
        grid.addColumn(user -> Messages.get(user.getRole().getNameProperty()))
                .setHeader(Messages.get("com.example.issues.role"));
        grid.addComponentColumn(u -> new Button(VaadinIcon.EDIT.create(),
                e -> UI.getCurrent().navigate(EditUserView.class, u.getId())));

        getContent().add(viewTitle, filterLayout, grid);
        getContent().setSizeFull();

        refreshGrid();
    }

    private void refreshGrid() {
        Set<User> users = userService.find(name.getValue(), role.getValue());
        grid.setItems(users);
    }

}
