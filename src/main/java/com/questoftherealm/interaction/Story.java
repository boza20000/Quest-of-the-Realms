package com.questoftherealm.interaction;

import static com.questoftherealm.game.GameConstants.*;

import com.questoftherealm.game.GameState;

public class Story {


    public String getStory(GameState state) {
        return state.getMessages().getBundle().get("story.intro").formatted(
                YELLOW, RESET,   // Eldoria
                YELLOW, RESET,   // Eldoria again
                BLUE, RESET,     // Humans
                RED, RESET,      // Orcs
                MAGENTA, RESET,  // Mages
                GREEN, RESET     // Enter
        );
    }
}
