package com.questoftherealm.characters.player;

import com.questoftherealm.exceptions.IllegalPlayerType;
import com.questoftherealm.game.GameState;

public enum PlayerTypes {
    Mage,
    Warrior,
    Rogue,
    Orc;

    public static PlayerTypes fromInt(int choice, GameState state) {
        return switch (choice) {
            case 1 -> Warrior;
            case 2 -> Mage;
            case 3 -> Orc;
            case 4 -> Rogue;
            default -> throw new IllegalPlayerType(state.getMessages().getBundle().get("error.command.IllegalPlayerType",choice));
        };
    }
}
