package com.example.issues.users.ui;

import com.example.api.domain.Role;
import com.example.api.domain.User;
import com.example.api.ui.MainLayout;
import com.example.issues.users.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

@Route(value = CreateUserView.VIEW_NAME, layout = MainLayout.class)
public class CreateUserView extends Composite<VerticalLayout> {

    public static final String VIEW_NAME = "create-user";

    private TextField name = new TextField("Name");
    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("Password");
    private ComboBox<Role> role = new ComboBox<>("Role", Role.values());

    private UserService userService;

    public CreateUserView(UserService userService) {
        this.userService = userService;
        Span viewTitle = new Span("Create user");
        viewTitle.addClassName("view-title");

        FormLayout formLayout = new FormLayout(name, email, password, role);
        formLayout.setWidth("100%");

        Button save = new Button("Save", e -> create());
        save.getElement().setAttribute("theme", "primary");

        getContent().removeAll();
        getContent().add(viewTitle, formLayout, save);
        getContent().setSizeFull();
        getContent().setAlignSelf(FlexComponent.Alignment.END, save);
    }

    private void create() {
        try {
            User user = new User();
            BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
            binder.bindInstanceFields(this);
            binder.writeBean(user);
            userService.saveOrUpdate(user);
            UI.getCurrent().navigate(UsersView.VIEW_NAME);

        } catch (ValidationException e) {
            Notification.show("Please fix the errors and try again.");
        }
    }

}
