package com.example.webapp.ui;

import com.example.common.ui.Messages;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Value;

@Route
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends VerticalLayout {

    private final String navigateTo;

    public MainView(@Value("${webapp.mainview.navigateTo}") String navigateTo) {
        this.navigateTo = navigateTo;
        UI.getCurrent().getPage().setTitle(Messages.get("com.example.appName"));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI.getCurrent().navigate(navigateTo);
    }

}
