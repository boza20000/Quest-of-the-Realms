package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.SaveGame;
import com.questoftherealm.game.LoadGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class SaveLoadCommandTest {

    private Player player;

    @BeforeEach
    void setup() {
        player = mock(Player.class);
        Game.setPlayer(player);
    }

    @Test
    void testSaveGameValidInput() {
        SaveGame saveGame = mock(SaveGame.class);
        SaveCommand command = new SaveCommand();
        command.execute(new String[]{"save", "mySave"});
        verify(saveGame).createSave("mySave");
    }

    @Test
    void testSaveGameInvalidInput() {
        SaveGame saveGame = mock(SaveGame.class);
        SaveCommand command = new SaveCommand();

        command.execute(new String[]{"save"}); // invalid, missing name

        verifyNoInteractions(saveGame);
    }

    @Test
    void testLoadGameValidInput() {
        LoadGame loadGame = mock(LoadGame.class);
        LoadCommand command = new LoadCommand();

        command.execute(new String[]{"load", "mySave"});

        verify(loadGame).loadGameSave("mySave");
    }

    @Test
    void testLoadGameInvalidInput() {
        LoadGame loadGame = mock(LoadGame.class);
        LoadCommand command = new LoadCommand();

        command.execute(new String[]{"load"}); // invalid

        verifyNoInteractions(loadGame);
    }
}
