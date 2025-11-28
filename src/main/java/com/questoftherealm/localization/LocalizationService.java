package com.questoftherealm.localization;

import java.util.Locale;

public class LocalizationService {
    private MessageBundle bundle;
    
    public LocalizationService(Locale locale) {
        bundle = new MessageBundle(locale);
    }

    public LocalizationService() {
        bundle = new MessageBundle();
    }

    public MessageBundle getBundle() {
        return bundle;
    }
}
