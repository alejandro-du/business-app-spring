package com.example.webapp.ui;

import com.example.common.ui.Messages;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;

import javax.servlet.http.HttpServletResponse;

@HtmlImport("/frontend/styles/shared-styles.html")
public class RouteNotFoundErrorView extends Composite<VerticalLayout> implements HasErrorParameter<NotFoundException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.webapp.pageNotFound", "Not found") + " | " +
                        Messages.get("com.example.appName", ""));

        H1 title = new H1(Messages.get("com.example.webapp.pageNotFound", "Page not found"));

        Div message = new Div();
        message.setText(Messages.get("com.example.webapp.pageNotFoundMessage", ""));

        getContent().add(title, message);
        getContent().setSizeFull();
        getContent().getElement().setAttribute("theme", "dark");
        getContent().setPadding(true);

        return HttpServletResponse.SC_NOT_FOUND;
    }
}
