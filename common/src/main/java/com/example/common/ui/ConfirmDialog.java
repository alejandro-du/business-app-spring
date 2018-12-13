package com.example.common.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ConfirmDialog extends Composite<Dialog> {

    public ConfirmDialog(String question,
                         String yesCaption,
                         String noCaption,
                         ComponentEventListener<ClickEvent<Button>> confirmClickListener) {
        Text text = new Text(question);

        Button no = new Button(noCaption, e -> getContent().close());
        Button yes = new Button(yesCaption, e -> getContent().close());
        yes.getElement().setAttribute("theme", "primary");
        yes.addClickListener(confirmClickListener);

        HorizontalLayout options = new HorizontalLayout(no, yes);

        getContent().add(text, options);
    }

    public void open() {
        getContent().open();
    }

}
