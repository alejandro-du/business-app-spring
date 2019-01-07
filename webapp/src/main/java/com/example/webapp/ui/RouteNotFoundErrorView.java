package com.example.webapp.ui;

import com.example.common.ui.Messages;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

@HtmlImport("/frontend/styles/shared-styles.html")
public class RouteNotFoundErrorView extends Composite<VerticalLayout>
        implements HasErrorParameter<NotFoundException>, HasDynamicTitle {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        H1 title = new H1(Messages.get("com.example.webapp.pageNotFound", "Page not found"));

        Div message = new Div();
        message.setText(Messages.get("com.example.webapp.pageNotFoundMessage", ""));

        getContent().add(title, message);
        getContent().setSizeFull();
        getContent().getElement().setAttribute("theme", "dark");
        getContent().setPadding(true);

        return HttpServletResponse.SC_NOT_FOUND;
    }

    @Override
    public String getPageTitle() {
        return Messages.getPageTitle("com.example.webapp.pageNotFound");
    }

}
