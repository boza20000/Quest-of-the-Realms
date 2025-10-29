package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.SaveGame;
import com.questoftherealm.game.LoadGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

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
        try (MockedConstruction<SaveGame> mocked = mockConstruction(SaveGame.class)) {
            SaveCommand command = new SaveCommand();
            command.execute(new String[]{"save", "mySave"});
            SaveGame saveGameMock = mocked.constructed().get(0);
            verify(saveGameMock).createSave("mySave");
        }
    }

    @Test
    void testSaveGameInvalidInput() {
        try (MockedConstruction<SaveGame> mocked = mockConstruction(SaveGame.class)) {
            SaveCommand command = new SaveCommand();
            command.execute(new String[]{"save"});
            verifyNoInteractions(mocked.constructed().isEmpty() ? mock(SaveGame.class) : mocked.constructed().get(0));
        }
    }

    @Test
    void testLoadGameValidInput() {
        try (MockedConstruction<LoadGame> mocked = mockConstruction(LoadGame.class)) {
            LoadCommand command = new LoadCommand();
            command.execute(new String[]{"load", "mySave"});
            LoadGame loadGameMock = mocked.constructed().get(0);
            verify(loadGameMock).loadGameSave("mySave");
        }
    }

    @Test
    void testLoadGameInvalidInput() {
        try (MockedConstruction<LoadGame> mocked = mockConstruction(LoadGame.class)) {
            LoadCommand command = new LoadCommand();
            command.execute(new String[]{"load"});
            verifyNoInteractions(mocked.constructed().isEmpty() ? mock(LoadGame.class) : mocked.constructed().get(0));
        }
    }
}
