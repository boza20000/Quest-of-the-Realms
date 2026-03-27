package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.characters.player.PlayTime;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.SaveGame;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

class SaveCommandTest {

    private Player player;
    private GameState state;
    private Output output;
    private Path singleSaveDir;
    private Path multiplayerSaveDir;
    private SaveGame saveGame;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        GameServices services = mock(GameServices.class);
        when(services.getOutput()).thenReturn(output);

        state = new GameState("test-room-" + UUID.randomUUID(), services);
        state.setPrivate(true);
        
        player = spy(new Player("TestHero", PlayerTypes.Warrior, state));
        setPlayerValidPlaytime();
        state.addPlayer(player);

        singleSaveDir = tempDir.resolve("saves");
        multiplayerSaveDir = tempDir.resolve("server_saves");
        
        saveGame = new SaveGame(singleSaveDir.toString(), multiplayerSaveDir.toString());
    }
    
    private void setPlayerValidPlaytime() {
        when(player.getPlayTime()).thenReturn(new PlayTime(1, 30));
    }

    @Test
    void givenSingleplayerValidSaveName_whenSaveExecutes_thenFileIsCreatedInSavesFolder() {
        SaveCommand cmd = new SaveCommand(saveGame);
        String saveName = "slot-" + UUID.randomUUID();
        state.setPrivate(true);
        cmd.execute(new String[]{"save", saveName}, player, state);
        Path saveFile = singleSaveDir.resolve(saveName + ".json");
        assertTrue(Files.exists(saveFile), "Save file should exist at: " + saveFile.toAbsolutePath());
    }

    @Test
    void givenSingleplayerMissingSaveName_whenSaveExecutes_thenUsageIsPrinted() {
        SaveCommand cmd = new SaveCommand(saveGame);
        state.setPrivate(true);
        cmd.execute(new String[]{"save"}, player, state);
        verify(output).println(contains("Usage"));
    }

    @Test
    void givenSingleplayerZeroPlaytime_whenSaveExecutes_thenErrorMessagePrinted() {
        SaveCommand cmd = new SaveCommand(saveGame);
        when(player.getPlayTime()).thenReturn(new PlayTime(0, 0));
        state.setPrivate(true);
        cmd.execute(new String[]{"save", "test"}, player, state);
        verify(output).println(contains("playtime"));
    }

    @Test
    void givenMultiplayerSaveWithoutName_whenSaveExecutes_thenFileIsCreatedInRoomFolderWithUsername() {
        SaveCommand cmd = new SaveCommand(saveGame);
        state.setPrivate(false);
        cmd.execute(new String[]{"save"}, player, state);
        Path roomDir = multiplayerSaveDir.resolve(state.getName());
        Path saveFile = roomDir.resolve(player.getName() + ".json");
        assertTrue(Files.exists(saveFile), "Multiplayer save file should exist at: " + saveFile.toAbsolutePath());
    }

    @Test
    void givenMultiplayerSaveWithExtraArg_whenSaveExecutes_thenUsageIsPrinted() {
        SaveCommand cmd = new SaveCommand(saveGame);
        state.setPrivate(false);
        cmd.execute(new String[]{"save", "slot1"}, player, state);
        verify(output).println(contains("Usage"));
    }

    @Test
    void givenMultiplayerZeroPlaytime_whenSaveExecutes_thenSilentlySkips() {
        SaveCommand cmd = new SaveCommand(saveGame);
        when(player.getPlayTime()).thenReturn(new PlayTime(0, 0));
        state.setPrivate(false);
        cmd.execute(new String[]{"save"}, player, state);

        Path roomDir = multiplayerSaveDir.resolve(state.getName());
        Path saveFile = roomDir.resolve(player.getName() + ".json");
        assertFalse(
            Files.exists(saveFile),
            "Multiplayer save should not create file with zero playtime"
        );
    }
}
