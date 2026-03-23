package com.questoftherealm.characters.player;

import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.characters.playerCharacters.*;
import com.questoftherealm.exceptions.InvalidPlayerType;

public class PlayerFactory {
    public static Characters createPlayer(PlayerTypes type){
        return switch (type) {
            case Warrior -> new Warrior();
            case Mage -> new Mage();
            case Orc -> new Orc();
            case Rogue -> new Rogue();
        };
    }

}
