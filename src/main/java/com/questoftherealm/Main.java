package com.questoftherealm;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.PlayTime;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.interaction.Console;

public class Main {
    public static void main(String[] args) {
      Game game = new Game();
      game.start();
//        Player player = new Player(
//                "TestHero",
//                PlayerTypes.Warrior,
//                1, 0, 0,
//                GameConstants.PLAYER_START.x(),
//                GameConstants.PLAYER_START.y(),
//                "Spawn",
//                null,
//                null,
//                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
//                null,
//                null,  // mission
//                new PlayTime(4,12)
//        );
//        Console console = new Console();
//        console.displayEnd(player);
    }
}
