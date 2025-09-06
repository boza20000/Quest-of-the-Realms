package com.questoftherealm.characters.player;

public enum PlayerTypes {
    Mage,
    Warrior,
    Rogue,
    Orc;

    public static PlayerTypes fromInt(int choice) {
        return switch (choice) {
            case 1 -> Warrior;
            case 2 -> Mage;
            case 3 -> Orc;
            case 4 -> Rogue;
            default -> throw new IllegalArgumentException("Invalid option: " + choice);
        };
    }
}
