package com.example.helloworld.ui;

import com.example.common.ui.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "hello-world", layout = MainLayout.class)
public class HelloWorldView extends Composite<VerticalLayout> {

    public HelloWorldView() {
        getContent().add(new Text("Hello, World!"));
    }

}
