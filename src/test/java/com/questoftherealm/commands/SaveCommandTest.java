package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SaveCommandTest {

    private Player player;
    private GameState state;
    private Output output;
    private Path singleSaveDir;
    private Path multiplayerSaveDir;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        GameServices services = mock(GameServices.class);
        when(services.getOutput()).thenReturn(output);

        state = new GameState("test-room-" + UUID.randomUUID(), services);
        state.setPrivate(true);
        player = new Player("TestHero", PlayerTypes.Warrior, state);
        state.addPlayer(player);

        singleSaveDir = tempDir.resolve("saves");
        multiplayerSaveDir = tempDir.resolve("server_saves");
    }

    @Test
    void givenSingleplayerValidSaveName_whenSaveExecutes_thenFileIsCreatedInSavesFolder() {
        SaveCommand cmd = new SaveCommand(new SaveGame(singleSaveDir.toString(), multiplayerSaveDir.toString()));

        String saveName = "slot-" + UUID.randomUUID();
        state.setPrivate(true);

        cmd.execute(new String[]{"save", saveName}, player, state);

        Path saveFile = singleSaveDir.resolve(saveName + ".json");
        assertTrue(Files.exists(saveFile), "Save file should be created");
    }

    @Test
    void givenSingleplayerMissingSaveName_whenSaveExecutes_thenUsageIsPrinted() {
        SaveCommand cmd = new SaveCommand(new SaveGame(singleSaveDir.toString(), multiplayerSaveDir.toString()));
        state.setPrivate(true);

        cmd.execute(new String[]{"save"}, player, state);

        verify(output).println(contains("Usage"));
    }

    @Test
    void givenMultiplayerSaveWithoutName_whenSaveExecutes_thenFileIsCreatedInRoomFolderWithUsername() {
        SaveCommand cmd = new SaveCommand(new SaveGame(singleSaveDir.toString(), multiplayerSaveDir.toString()));
        state.setPrivate(false);

        cmd.execute(new String[]{"save"}, player, state);

        Path roomDir = multiplayerSaveDir.resolve(state.getName());
        Path saveFile = roomDir.resolve(player.getName() + ".json");

        assertTrue(Files.exists(saveFile), "Multiplayer save file should be created under room/username.json");
    }

    @Test
    void givenMultiplayerSaveWithExtraArg_whenSaveExecutes_thenUsageIsPrinted() {
        SaveCommand cmd = new SaveCommand(new SaveGame(singleSaveDir.toString(), multiplayerSaveDir.toString()));
        state.setPrivate(false);

        cmd.execute(new String[]{"save", "slot1"}, player, state);

        verify(output).println(contains("Usage"));
    }
}
