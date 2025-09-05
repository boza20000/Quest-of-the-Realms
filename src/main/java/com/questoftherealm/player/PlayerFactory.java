package com.questoftherealm.player;

import com.questoftherealm.exceptions.InvalidPlayerType;
import com.questoftherealm.characters.*;

public class PlayerFactory {

    public static Characters createPlayer(PlayerTypes type){
        return switch (type) {
            case Warrior -> new Warrior(45, 5, 6, 8, 25, 5, 3, 4);
            case Mage -> new Mage(28, 30, 3, 2, 8, 6, 10, 10);
            case Orc -> new Orc(50, 3, 9, 6, 20, 2, 1, 2);
            case Rogue -> new Rogue(35, 10, 7, 4, 13, 9, 3, 7);
            default -> throw new InvalidPlayerType("player type doesn't exist");
        };
    }

}
