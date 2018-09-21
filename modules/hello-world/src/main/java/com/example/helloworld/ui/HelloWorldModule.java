package com.example.helloworld.ui;

import com.example.api.BusinessAppModule;
import com.example.api.ui.MainMenu;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class HelloWorldModule implements BusinessAppModule {

    private final MainMenu mainMenu;

    public HelloWorldModule(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    public void initialize() {
        mainMenu.addOption(HelloWorldView.VIEW_NAME, "Hello, World");
    }

}
