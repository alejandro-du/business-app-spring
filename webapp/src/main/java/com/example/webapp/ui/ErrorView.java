package com.example.webapp.ui;

import com.example.api.ui.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@ParentLayout(MainLayout.class)
@PageTitle("Error | Business Application")
public class ErrorView extends Composite<VerticalLayout> implements HasErrorParameter<Exception> {

    private static final Logger logger = LoggerFactory.getLogger(ErrorView.class);

    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<Exception> errorParameter) {
        LocalDateTime now = LocalDateTime.now();
        logger.error("Internal Error-" + now, errorParameter.getException());

        H1 title = new H1("Internal error");
        title.addClassName("red");

        Div message = new Div();
        message.setText("Please report the error to the system administrators providing the following details:");

        Span date = new Span(now.toString());
        date.getStyle().set("font-weight", "bold");

        HorizontalLayout info = new HorizontalLayout(new Span("Timestamp: "), date);

        getContent().add(title, message, info);

        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

}
