package com.questoftherealm.characters;

import com.questoftherealm.Exceptions.InvalidPlayerType;

public class PlayerFactory {

    public static Characters createPlayer(PlayerTypes type){
        switch(type){
            case Warrior:
                return new Warrior(90,20,5,14,27,7,3,6);
            case  Mage:
                return new Mage(90,20,5,14,27,7,3,6);
            case Orc:
                return new Orc(90,20,5,14,27,7,3,6);
            case Rouge:
                return new Rogue(90,20,5,14,27,7,3,6);
            default:
                throw new InvalidPlayerType("player type doesn't exist");
        }
    }

}
