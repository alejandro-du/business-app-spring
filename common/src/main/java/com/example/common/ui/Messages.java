package com.example.common.ui;

import com.vaadin.flow.server.VaadinService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Messages {

    private static MessageSource messageSource = null;

    public Messages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static String get(String code, Object... args) {
        Locale locale = new Locale(VaadinService.getCurrentRequest().getLocale().getLanguage());
        return messageSource.getMessage(code, args, locale);
    }

    public static String get(String code, String defaultMessage, Object... args) {
        return messageSource.getMessage(code, args, defaultMessage, VaadinService.getCurrentRequest().getLocale());
    }

}
