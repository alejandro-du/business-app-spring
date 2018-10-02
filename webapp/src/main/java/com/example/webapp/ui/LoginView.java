package com.example.webapp.ui;

import com.example.api.service.AuthenticationService;
import com.example.api.ui.Header;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = LoginView.VIEW_NAME)
@HtmlImport("/frontend/styles/login-view-styles.html")
@HtmlImport("/frontend/styles/shared-styles.html")
public class LoginView extends Composite<VerticalLayout> {

    public static final String VIEW_NAME = "login";

    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("Password");

    private final AuthenticationService authenticationService;

    public LoginView(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

        Header header = new Header();

        H2 title = new H2("Sign in");

        email.setWidth("100%");
        email.setValue("marcus@vaadin.com");

        password.setWidth("100%");
        password.setValue("password1");

        Button signIn = new Button("Sign in", e -> signInClicked());
        signIn.getElement().setAttribute("theme", "primary");

        VerticalLayout formLayout = new VerticalLayout(title, email, password, signIn);
        formLayout.setSizeUndefined();
        formLayout.setAlignSelf(FlexComponent.Alignment.END, signIn);
        formLayout.addClassName("login-view-form-layout");

        HorizontalLayout contentLayout = new HorizontalLayout(formLayout);
        contentLayout.setWidth(null);
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        contentLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, formLayout);

        getContent().add(header, contentLayout);
        getContent().setSizeFull();
        getContent().setFlexGrow(1, contentLayout);
        getContent().setPadding(false);
        getContent().setMargin(false);
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, contentLayout);

        email.focus();
        password.addKeyPressListener(Key.ENTER, e -> signIn.click());
    }

    private void signInClicked() {
        String username = email.getValue();
        String password = this.password.getValue();

        if (authenticationService.authenticate(username, password)) {
            UI.getCurrent().navigate("");
        } else {
            Notification.show("Bad credentials", 5000, Notification.Position.MIDDLE);
        }
    }

}
