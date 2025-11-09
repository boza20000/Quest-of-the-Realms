package com.questoftherealm.localization;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class MessageBundle {
    private static final String BASE_NAME = "messages";
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BASE_NAME, java.util.Locale.ENGLISH);

    private MessageBundle() {}

    public static String get(String key, Object... args) {
        if (key == null || key.isBlank()) {
            return "???null???";
        }
        try {
            String message = BUNDLE.getString(key);
            return (args == null || args.length == 0)
                    ? message
                    : MessageFormat.format(message, args);
        } catch (MissingResourceException e) {
            return "???" + key + "???";
        }
    }
}
