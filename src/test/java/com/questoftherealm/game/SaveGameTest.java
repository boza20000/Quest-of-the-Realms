package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SaveGameTest {

    @TempDir
    Path tempDir;

    private SaveGame saveGame;
    private File singleSaveDir;
    private File multiSaveDir;

    // Real objects - MUST BE REAL (Jackson cannot serialize mocks!)
    private GameState gameState;
    private GameServices gameServices;
    private Player player;

    // Only mock I/O
    @Mock
    private Output output;
    @Mock
    private Input input;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        singleSaveDir = tempDir.resolve("saves").toFile();
        multiSaveDir = tempDir.resolve("server_saves").toFile();

        saveGame = new SaveGame(singleSaveDir.getAbsolutePath(), multiSaveDir.getAbsolutePath());

        // Setup real GameServices with mocked I/O
        gameServices = new GameServices(output, input);

        // Setup real GameState (creates LocalizationService internally)
        gameState = new GameState("TestRoom", gameServices);
        gameState.setSimulation(true);

        // Create REAL player (NOT mocked) - CRITICAL for Jackson serialization
        player = new Player("TestPlayer", PlayerTypes.Warrior, gameState);
        // Set valid playtime: 1h 30m in milliseconds
        player.setPlayTime(90 * 60 * 1000);
        gameState.addPlayer(player);
    }

    // ========== SINGLE PLAYER SAVE TESTS ==========

    @Test
    void givenValidSave_whenCreateSave_thenSaveSuccessful() {
        // Given
        String saveName = "TestSave";

        // When
        saveGame.createSave(saveName, player, gameState);

        // Then
        File savedFile = new File(singleSaveDir, saveName + ".json");
        assertTrue(savedFile.exists(), "Save file should exist");
    }

    @Test
    void givenNullPlayer_whenCreateSave_thenThrowSaveError() {
        // When & Then
        Exception exception = assertThrows(Exception.class, () ->
            saveGame.createSave("TestSave", null, gameState)
        );
        assertNotNull(exception, "An exception should be thrown");
    }

    @Test
    void givenBlankSaveName_whenCreateSave_thenThrowSaveError() {
        // When & Then
        Exception exception = assertThrows(Exception.class, () ->
            saveGame.createSave("   ", player, gameState)
        );
        assertNotNull(exception, "An exception should be thrown");
    }

    @Test
    void givenZeroPlaytime_whenCreateSinglePlayerSave_thenThrowSaveError() {
        // Given
        Player zeroTimePlayer = new Player("ZeroPlayer", PlayerTypes.Warrior, gameState);

        // When & Then
        Exception exception = assertThrows(Exception.class, () ->
            saveGame.createSave("TestSave", zeroTimePlayer, gameState)
        );
        assertNotNull(exception, "An exception should be thrown");
    }

    @Test
    void givenSaveNameNull_whenCreateSave_thenThrowSaveError() {
        // When & Then
        Exception exception = assertThrows(Exception.class, () ->
            saveGame.createSave(null, player, gameState)
        );
        assertNotNull(exception, "An exception should be thrown");
    }

    // ========== MULTIPLAYER SAVE TESTS ==========

    @Test
    void givenValidMultiplayerSave_whenCreateSave_thenSaveSuccessful() {
        // When
        saveGame.createSave(player, gameState);

        // Then
        File playerSaveDir = new File(multiSaveDir, "TestRoom");
        File savedFile = new File(playerSaveDir, player.getName() + ".json");
        assertTrue(savedFile.exists(), "Multiplayer save file should exist");
    }

    @Test
    void givenNullPlayerForMultiplayer_whenCreateSave_thenThrowSaveError() {
        // When & Then
        Exception exception = assertThrows(Exception.class, () ->
            saveGame.createSave(null, gameState)
        );
        assertNotNull(exception, "An exception should be thrown");
    }

    @Test
    void givenZeroPlaytimeForMultiplayer_whenCreateSave_thenSilentlySkip() {

        Player zeroTimePlayer = new Player("ZeroPlayer", PlayerTypes.Warrior, gameState);
        saveGame.createSave(zeroTimePlayer, gameState);
        File playerSaveDir = new File(multiSaveDir, "TestRoom");
        File savedFile = new File(playerSaveDir, zeroTimePlayer.getName() + ".json");
        assertFalse(savedFile.exists(), "Save should be skipped for zero playtime");
    }

    // ========== EDGE CASES ==========

    @Test
    void givenMinimalPlaytime_whenCreateSave_thenSuccessful() {
        // Given
        Player minimalPlayer = new Player("MinimalPlayer", PlayerTypes.Warrior, gameState);
        minimalPlayer.setPlayTime(60 * 1000); // 1 minute

        // When
        saveGame.createSave("TestSave", minimalPlayer, gameState);

        // Then
        File savedFile = new File(singleSaveDir, "TestSave.json");
        assertTrue(savedFile.exists());
    }

    @Test
    void givenValidMultiplayerSaveWithDifferentPlayers_whenCreateMultipleSaves_thenBothSaved() {
        // Given
        Player player2 = new Player("Player2", PlayerTypes.Mage, gameState);
        player2.setPlayTime(120 * 60 * 1000); // 2 hours

        // When
        saveGame.createSave(player, gameState);
        saveGame.createSave(player2, gameState);

        // Then
        File file1 = new File(multiSaveDir.getAbsolutePath() + "/TestRoom", player.getName() + ".json");
        File file2 = new File(multiSaveDir.getAbsolutePath() + "/TestRoom", player2.getName() + ".json");
        assertTrue(file1.exists(), "First player save should exist");
        assertTrue(file2.exists(), "Second player save should exist");
    }

}







