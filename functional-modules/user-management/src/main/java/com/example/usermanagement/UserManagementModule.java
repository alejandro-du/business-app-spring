package com.example.usermanagement;

import com.example.common.BusinessAppModule;
import com.example.common.ui.Messages;
import com.example.common.ui.UIConfiguration;
import com.example.usermanagement.ui.UsersView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;

@Component
@VaadinSessionScope
public class UserManagementModule implements BusinessAppModule {

    private final UIConfiguration uiConfiguration;

    public UserManagementModule(UIConfiguration uiConfiguration) {
        this.uiConfiguration = uiConfiguration;
    }

    @Override
    public void initialize() {
        uiConfiguration.addMenuOption(UsersView.class, Messages.get("com.example.issues.users"), VaadinIcon.USERS);
    }

}
