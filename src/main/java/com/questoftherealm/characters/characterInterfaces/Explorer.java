package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.game.GameState;

public interface Explorer {
    void exploreStructure(String structure, GameState state);

    void look(GameState state);
}