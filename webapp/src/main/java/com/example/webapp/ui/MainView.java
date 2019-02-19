package com.example.webapp.ui;

import com.example.common.ui.Messages;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Value;

@Route
public class MainView extends VerticalLayout implements HasDynamicTitle {

    private final String navigateTo;

    public MainView(@Value("${webapp.mainview.navigateTo}") String navigateTo) {
        this.navigateTo = navigateTo;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI.getCurrent().navigate(navigateTo);
    }

    @Override
    public String getPageTitle() {
        return Messages.get("com.example.appName");
    }

}
