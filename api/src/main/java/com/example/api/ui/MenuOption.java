package com.example.api.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuOption {

    private Class<? extends Component> viewClass;

    private String text;

    private VaadinIcon icon;

}
