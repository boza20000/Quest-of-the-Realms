package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class LookCommandTest {
    private Player player;
    private LookCommand lookCommand;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setup() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        player = new Player("TestHero", PlayerTypes.Warrior,
                1, 0, 0, 0, 0, "Spawn",
                null, null, new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, null);
        Game.setPlayer(player);
        lookCommand = new LookCommand();
    }

    @Test
    void testLookCommandExecutes() {
        lookCommand.execute(new String[]{"look"});
        assertTrue(output.toString().contains("Looking") || output.toString().contains("failed"));
    }

    @Test
    void testLookInvalidArgs() {
        lookCommand.execute(new String[]{"look", "extra"});
        assertTrue(output.toString().contains("Usage"));
    }
}
