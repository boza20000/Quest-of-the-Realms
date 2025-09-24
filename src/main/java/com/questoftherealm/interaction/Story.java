package com.questoftherealm.interaction;

import static com.questoftherealm.game.GameConstants.*;

public class Story {
    private final String story = """
            Welcome to %sEldoria%s!
            
            For centuries, the four kingdoms —
            %sHumans%s, %sOrcs%s, %sMages%s, and %sElari%s —
            have lived in harmony.
            
            Recently, however, strange signs have appeared in the northern lands:
            villages abandoned, forests unusually quiet, and rumors of %screatures%s
            moving in the %sshadows%s.
            
            You are a wanderer, sent on a mission to investigate the northern region
            of your kingdom. The council suspects that something is brewing,
            but no one knows what.
            
            Your task is clear: travel north, visit the villages, explore the area,
            report back, and uncover the truth behind these disturbances.
            The kingdom trusts your skill and judgment to report back with any findings.
            
            Your journey begins now. Pack your gear, steel your courage, and take your
            first step into the unknown.
            
            Stay vigilant — %sshadows%s hide more than they reveal,
            and %sdanger%s could strike from anywhere. Trust no one,
            and remember some threats lurk where you least expect.
            
            Press %sEnter%s to begin your adventure...
            """.formatted(
            YELLOW, RESET,   // Eldoria
            BLUE, RESET,     // Humans
            RED, RESET,      // Orcs
            MAGENTA, RESET,  // Mages
            CYAN, RESET,     // Elari
            RED, RESET,      // creatures
            RED, RESET,      // shadows
            RED, RESET,      // shadows
            RED, RESET,      // danger
            GREEN, RESET     // Enter
    );


    public String getStory() {
        return story;
    }

}
