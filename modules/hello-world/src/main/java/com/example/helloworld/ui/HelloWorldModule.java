package com.example.helloworld.ui;

import com.example.api.BusinessAppModule;
import com.example.api.ui.UIConfiguration;
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
        uiConfiguration.addMenuOption(HelloWorldView.VIEW_NAME, "Hello, World");
    }

}
