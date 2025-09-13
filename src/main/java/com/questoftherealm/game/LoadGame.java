package com.questoftherealm.game;

import com.questoftherealm.exceptions.FileNotLoaded;

public class LoadGame {
    public static void loadGameSave(String filename) {
        try {

        } catch (Exception e) {
            throw new FileNotLoaded("File not loaded");
        }

    }

    public static void printSaves() {
    }
}
