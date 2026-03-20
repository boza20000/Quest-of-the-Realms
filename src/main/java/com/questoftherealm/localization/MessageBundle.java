package com.questoftherealm.localization;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.questoftherealm.server.ServerLogger;

public final class MessageBundle {
    private static final String BASE_NAME = "messages";
    private ResourceBundle BUNDLE;

    public MessageBundle(Locale locale) {
         BUNDLE = ResourceBundle.getBundle(BASE_NAME, locale);
    }
    public MessageBundle(){
        BUNDLE = ResourceBundle.getBundle(BASE_NAME, java.util.Locale.ENGLISH);
    }


    public String get(String key, Object... args) {
        if (key == null || key.isBlank()) {
            return "???null???";
        }
        try {
            String message = BUNDLE.getString(key);
            return (args == null || args.length == 0)
                    ? message
                    : MessageFormat.format(message, args);
        } catch (MissingResourceException e) {
            ServerLogger.get().warn("Missing translation key: " + key, e);
            return "???" + key + "???";
        }
    }
}
