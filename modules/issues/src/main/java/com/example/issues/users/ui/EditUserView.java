package com.example.issues.users.ui;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import com.example.common.service.ValidationService;
import com.example.common.ui.ConfirmDialog;
import com.example.common.ui.MainLayout;
import com.example.common.ui.Messages;
import com.example.issues.issues.Session;
import com.example.issues.users.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route(value = "edit-user", layout = MainLayout.class)
public class EditUserView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    private TextField name = new TextField(Messages.get("com.example.issues.name"));
    private TextField email = new TextField(Messages.get("com.example.issues.email"));
    private PasswordField password = new PasswordField(Messages.get("com.example.issues.password"));
    private ComboBox<Role> role = new ComboBox<>(Messages.get("com.example.issues.role"), Role.values());

    private final UserService userService;
    private final Session session;
    private final ValidationService validationService;

    private BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
    private String originalPassword;

    public EditUserView(UserService userService, Session session, ValidationService validationService) {
        this.userService = userService;
        this.session = session;
        this.validationService = validationService;

        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.issues.editUser") + " | " + Messages.get("com.example.appName"));
    }

    @Override
    public void setParameter(BeforeEvent event, Long userId) {
        Optional<User> user = userService.findById(userId);
        if (!user.isPresent()) {
            UI.getCurrent().navigate(UsersView.class);
        } else {
            editUser(user.get());
        }
    }

    private void editUser(User user) {
        Span viewTitle = new Span(Messages.get("com.example.issues.editUser"));
        viewTitle.addClassName("view-title");

        name.focus();

        role.setItemLabelGenerator(role -> Messages.get(role.getNameProperty()));

        FormLayout formLayout = new FormLayout(name, email, password, role);
        formLayout.setWidth("100%");

        Button delete = new Button(Messages.get("com.example.issues.delete"), e -> delete(user));
        delete.getElement().setAttribute("theme", "error");

        Button save = new Button(Messages.get("com.example.issues.save"), e -> save(user));
        save.getElement().setAttribute("theme", "primary");

        HorizontalLayout actionsLayout = new HorizontalLayout(delete, save);

        getContent().removeAll();
        getContent().add(viewTitle, formLayout, actionsLayout);
        getContent().setSizeFull();
        getContent().setAlignSelf(FlexComponent.Alignment.END, actionsLayout);


        originalPassword = user.getPassword();
        user.setPassword(null);

        binder.bindInstanceFields(this);
        binder.removeBinding(password);
        binder.setBean(user);
    }

    private void delete(User user) {
        new ConfirmDialog(Messages.get("com.example.issues.deleteUserConfirmation"),
                Messages.get("com.example.issues.yes"),
                Messages.get("com.example.issues.no"),
                e -> {
                    Long userId = user.getId();
                    userService.delete(userService.findById(userId).get());
                    if (session.getUserId().equals(userId)) {
                        UI.getCurrent().getPage().executeJavaScript("window.location='/logout'");
                    } else {
                        UI.getCurrent().navigate(UsersView.class);
                    }
                }).open();
    }

    private void save(User user) {
        boolean newPassword = !password.getValue().isEmpty();
        try {
            if (!newPassword) {
                password.setValue(originalPassword);
            }

            binder.writeBean(user);
            user.setPassword(password.getValue());
            validationService.validateProperty(user, "password", password);

            userService.update(user, newPassword);
            UI.getCurrent().navigate(UsersView.class);

        } catch (ValidationException | javax.validation.ValidationException e) {
            if (!newPassword) {
                user.setPassword(null);
                password.clear();
                password.setErrorMessage(null);
                password.setInvalid(false);
            }

            Notification.show(Messages.get("com.example.issues.validationError"));
        }
    }

}
