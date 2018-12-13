package com.example.webapp.ui;

import com.example.common.ui.MainLayout;
import com.example.common.ui.Messages;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@ParentLayout(MainLayout.class)
@HtmlImport("/frontend/styles/shared-styles.html")
public class InternalServerErrorView extends Composite<VerticalLayout> implements HasErrorParameter<Exception> {

    private static final Logger logger = LoggerFactory.getLogger(InternalServerErrorView.class);

    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<Exception> errorParameter) {
        LocalDateTime now = LocalDateTime.now();
        logger.error("Error-" + now, errorParameter.getException());

        UI.getCurrent()
                .getPage()
                .setTitle(Messages.get("com.example.webapp.error", "Error") + " | " +
                        Messages.get("com.example.appName", ""));

        H1 title = new H1(Messages.get("com.example.webapp.error", "Error"));
        title.addClassName("red");

        Div message = new Div();
        message.setText(Messages.get("com.example.webapp.errorMessage", ""));

        Span date = new Span(now.toString());
        date.getStyle().set("font-weight", "bold");

        HorizontalLayout info =
                new HorizontalLayout(new Span(Messages.get("com.example.webapp.timeStamp", "Timestamp:")), date);

        getContent().add(title, message, info);

        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

}
