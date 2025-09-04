package com.questoftherealm.player;

import com.questoftherealm.characters.Characters;

public class Player {
    private String name;
    private Characters playerCharacter;

    public Player(String name, PlayerTypes playerCharacter) {
        setName(name);
        setPlayerCharacter(PlayerFactory.createPlayer(playerCharacter));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Characters getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter(Characters playerCharacter) {
        this.playerCharacter = playerCharacter;
    }
}
