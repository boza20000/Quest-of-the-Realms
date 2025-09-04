package com.questoftherealm.game;

public class Story {
    private String story = """
            Welcome to Eldoria!
            
            For centuries, the four kingdoms — Humans, Orcs, Mages, and Elari —
            have lived in harmony. Recently, however, strange signs have appeared in the northern
            lands: villages abandoned, forests unusually quiet, and rumors of creatures
            moving in the shadows.
            
            You are a wanderer, sent on a mission to investigate the northern region of your kingdom.
            The council suspects that something is brewing, but no one knows what.
            Your task is clear: travel north, visit the villages, explore the area, report back,
            and uncover the truth behind these disturbances.
            
            The kingdom trusts your skill and judgment to report back with any findings.
            
            Your journey begins now. Pack your gear, steel your courage, and take your
            first step into the unknown. Stay vigilant—shadows hide more than they reveal,
            and danger could strike from anywhere. Trust no one,
            and remember some threats lurk where you least expect.
            
            Press Enter to begin your adventure...
            """;


    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
