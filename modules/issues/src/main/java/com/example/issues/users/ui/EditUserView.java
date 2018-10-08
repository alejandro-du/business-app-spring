package com.example.issues.users.ui;

import com.example.api.domain.Role;
import com.example.api.domain.User;
import com.example.api.ui.ConfirmDialog;
import com.example.api.ui.MainLayout;
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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

@Route(value = "edit-user", layout = MainLayout.class)
@PageTitle("Edit user | Business Application")
public class EditUserView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    private TextField name = new TextField("Name");
    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("Password");
    private ComboBox<Role> role = new ComboBox<>("Role", Role.values());

    private final UserService userService;
    private final Session session;

    private BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
    private @NotNull @NotEmpty @Size(min = 6, max = 255) @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", message = "must have a digit and a letter") String originalPassword;

    public EditUserView(UserService userService, Session session) {
        this.userService = userService;
        this.session = session;
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
        Span viewTitle = new Span("Edit user");
        viewTitle.addClassName("view-title");

        name.focus();

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


        originalPassword = user.getPassword();
        user.setPassword(null);

        binder.bindInstanceFields(this);
        binder.setBean(user);
    }

    private void delete(User user) {
        new ConfirmDialog("Do you want to delete this user and their reported and owned issues?", e -> {
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

        if (!newPassword) {
            password.setValue(originalPassword);
        }

        if (binder.validate().hasErrors()) {
            Notification.show("Please fix the errors and try again.");
        } else {
            userService.update(user, newPassword);
            UI.getCurrent().navigate(UsersView.class);
        }
    }

}
