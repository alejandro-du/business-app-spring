package com.example.issues.users;

import com.example.api.domain.Role;
import com.example.api.domain.User;
import com.example.api.service.UserService;
import com.example.api.ui.ConfirmDialog;
import com.example.api.ui.MainLayout;
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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route(value = EditUserView.VIEW_NAME, layout = MainLayout.class)
public class EditUserView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    public static final String VIEW_NAME = "edit-user";

    public static String getViewName(Long userId) {
        return VIEW_NAME + "/" + userId;
    }

    private TextField name = new TextField("Name");
    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("Password");
    private ComboBox<Role> role = new ComboBox<>("Role", Role.values());

    private final UserService userService;
    private BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    public EditUserView(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void setParameter(BeforeEvent event, Long userId) {
        Optional<User> user = userService.findById(userId);
        if (!user.isPresent()) {
            UI.getCurrent().navigate(UsersView.VIEW_NAME);
        } else {
            editUser(user.get());
        }
    }

    private void editUser(User user) {
        Span viewTitle = new Span("Edit user");
        viewTitle.addClassName("view-title");

        FormLayout formLayout = new FormLayout(name, email, password, role);
        formLayout.setWidth("100%");

        Button delete = new Button("Delete...", e -> delete(user));
        delete.getElement().setAttribute("theme", "error");

        Button save = new Button("Save", e -> save(user));
        save.getElement().setAttribute("theme", "primary");

        HorizontalLayout actionsLayout = new HorizontalLayout(delete, save);

        getContent().removeAll();
        getContent().add(viewTitle, formLayout, actionsLayout);
        getContent().setSizeFull();
        getContent().setAlignSelf(FlexComponent.Alignment.END, actionsLayout);

        binder.bindInstanceFields(this);
        binder.setBean(user);
    }

    private void delete(User user) {
        new ConfirmDialog("Do you want to delete this user?", e -> {
            userService.delete(user);
            UI.getCurrent().navigate(UsersView.VIEW_NAME);
        }).open();
    }

    private void save(User user) {
        if (binder.validate().hasErrors()) {
            Notification.show("Please fix the errors and try again.");
        } else {
            userService.saveOrUpdate(user);
            UI.getCurrent().navigate(UsersView.VIEW_NAME);
        }
    }

}
