package com.questoftherealm.interaction;

import static com.questoftherealm.game.GameConstants.*;

public class Story {
    private final String story = """
            Welcome to %sEldoria%s!
            For centuries, the tree kingdoms of %sEldoria%s — %sHumans%s, %sOrcs%s and %sMages%s have lived in harmony.
            Recently, however, strange signs have appeared in the northern lands:
            villages abandoned, forests unusually quiet, and rumors of creatures moving in the shadows.
            
            Your task is clear: travel north, visit the villages, explore the area,
            report back, and uncover the truth behind these disturbances.
            The kingdom trusts your skill and judgment to report back with any findings.
            
            Stay vigilant — shadows hide more than they reveal, danger could strike from anywhere.
            Trust no one,
            and remember some threats lurk where you least expect.
            
            Press %sEnter%s to begin your adventure...
            """.formatted(
            YELLOW, RESET,// Eldoria
            YELLOW, RESET,  // Eldoria
            BLUE, RESET,     // Humans
            RED, RESET,      // Orcs
            MAGENTA, RESET,  // Mages
            GREEN, RESET     // Enter
    );


    public String getStory() {
        return story;
    }

}
