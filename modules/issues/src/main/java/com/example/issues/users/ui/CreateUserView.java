package com.example.issues.users.ui;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import com.example.common.service.ValidationService;
import com.example.common.ui.MainLayout;
import com.example.common.ui.Messages;
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

@Route(value = "create-user", layout = MainLayout.class)
public class CreateUserView extends Composite<VerticalLayout> {

    private TextField name = new TextField(Messages.get("com.example.issues.name"));
    private TextField email = new TextField(Messages.get("com.example.issues.email"));
    private PasswordField password = new PasswordField(Messages.get("com.example.issues.password"));
    private ComboBox<Role> role = new ComboBox<>(Messages.get("com.example.issues.role"), Role.values());

    private final UserService userService;
    private final ValidationService validationService;

    public CreateUserView(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;

        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.issues.createUser") + " | " + Messages.get("com.example.appName"));

        Span viewTitle = new Span(Messages.get("com.example.issues.createUser"));
        viewTitle.addClassName("view-title");

        name.focus();

        role.setItemLabelGenerator(role -> Messages.get(role.getNameProperty()));

        FormLayout formLayout = new FormLayout(name, email, password, role);
        formLayout.setWidth("100%");

        Button save = new Button(Messages.get("com.example.issues.save"), e -> create());
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
            binder.removeBinding(password);
            binder.writeBean(user);
            user.setPassword(password.getValue());
            validationService.validateProperty(user, "password", password);

            userService.save(user);
            UI.getCurrent().navigate(UsersView.class);

        } catch (ValidationException | javax.validation.ValidationException e) {
            Notification.show(Messages.get("com.example.issues.validationError"));
        }
    }

}
