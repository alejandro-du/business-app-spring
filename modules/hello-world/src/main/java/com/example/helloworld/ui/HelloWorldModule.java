package com.example.helloworld.ui;

import com.example.common.BusinessAppModule;
import com.example.common.ui.UIConfiguration;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

@SpringComponent
@VaadinSessionScope
public class HelloWorldModule implements BusinessAppModule {

    private final UIConfiguration uiConfiguration;

    public HelloWorldModule(UIConfiguration uiConfiguration) {
        this.uiConfiguration = uiConfiguration;
    }

    @Override
    public void initialize() {
        uiConfiguration.addMenuOption(HelloWorldView.class, "Hello, World", VaadinIcon.GLOBE_WIRE);
    }

}
