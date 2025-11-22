package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.SaveGame;
import com.questoftherealm.game.LoadGame;
import com.questoftherealm.game.interfaces.Output;

import org.junit.jupiter.api.*;
import java.io.File;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveLoadCommandTest {

    private Player player;
    private GameState state;
    private GameServices services;
    private Output output;

    private File savesDir;

    @BeforeEach
    void setup() {
        // Create fresh "saves" directory for each test
        savesDir = new File("savesTest");
        if (savesDir.exists()) {
            for (File f : savesDir.listFiles()) f.delete();
        } else {
            savesDir.mkdirs();
        }

        player = new Player("TestHero", PlayerTypes.Warrior);

        output = mock(Output.class);
        services = mock(GameServices.class);
        state = mock(GameState.class);

        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
    }

    @AfterEach
    void teardown() {
        for (File f : savesDir.listFiles()) f.delete();
        savesDir.delete();
    }

    @Test
    void testSaveGame_ValidInput_CreatesFile() {
        SaveCommand cmd = new SaveCommand();

        cmd.execute(new String[]{"save", "slot1"}, player, state);

        File saveFile = new File("saves/slot1.json");
        assertTrue(saveFile.exists(), "Save file should be created");
    }

    @Test
    void testLoadGame_ValidInput_LoadsFile() throws Exception {
        SaveGame saver = new SaveGame();
        saver.createSave("slot1", player, state);
        LoadCommand cmd = new LoadCommand();
        cmd.execute(new String[]{"load", "slot1"}, player, state);
        verify(state).setPlayer(any(Player.class));
    }

    @Test
    void testSaveGame_InvalidInput_ShowsError() {
        SaveCommand cmd = new SaveCommand();
        cmd.execute(new String[]{"save"}, player, state);
        verify(output).println(contains("Usage"));
    }


    @Test
    void testLoadGame_InvalidInput_ShowsError() {
        LoadCommand cmd = new LoadCommand();
        cmd.execute(new String[]{"load"}, player, state);
        verify(output).println(contains("Usage"));
    }
}
