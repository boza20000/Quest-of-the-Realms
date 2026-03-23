package com.questoftherealm.friendlyEntities.FriendInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;

public interface Friendly {
     void talk(GameState state, Player player, boolean isSimulation);
}
