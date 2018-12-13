package com.example.common.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.Objects;

public class MenuOption {

    private Class<? extends Component> viewClass;

    private String text;

    private VaadinIcon icon;

    public MenuOption(Class<? extends Component> viewClass, String text, VaadinIcon icon) {
        this.viewClass = viewClass;
        this.text = text;
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuOption that = (MenuOption) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    public Class<? extends Component> getViewClass() {
        return viewClass;
    }

    public void setViewClass(Class<? extends Component> viewClass) {
        this.viewClass = viewClass;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public VaadinIcon getIcon() {
        return icon;
    }

    public void setIcon(VaadinIcon icon) {
        this.icon = icon;
    }

}
