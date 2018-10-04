package com.example.webapp.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Value;

@Route
@PageTitle("Business Application")
public class MainView extends VerticalLayout {

    private final String navigateTo;

    public MainView(@Value("${webapp.mainview.navigateTo}") String navigateTo) {
        this.navigateTo = navigateTo;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI.getCurrent().navigate(navigateTo);
    }

}
