package com.questoftherealm.interaction;

import static com.questoftherealm.game.GameConstants.*;
import com.questoftherealm.localization.MessageBundle;

public class Story {
    private final String story = MessageBundle.get("story.intro").formatted(
            YELLOW, RESET,   // Eldoria
            YELLOW, RESET,   // Eldoria again
            BLUE, RESET,     // Humans
            RED, RESET,      // Orcs
            MAGENTA, RESET,  // Mages
            GREEN, RESET     // Enter
    );

    public String getStory() {
        return story;
    }
}
