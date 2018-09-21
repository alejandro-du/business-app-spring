package com.example.helloworld.ui;

import com.example.api.ui.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = HelloWorldView.VIEW_NAME, layout = MainLayout.class)
public class HelloWorldView extends Composite<VerticalLayout> {

    public static final String VIEW_NAME = "hello-world";

    public HelloWorldView() {
        getContent().add(new Text("It works!"));
    }

}
