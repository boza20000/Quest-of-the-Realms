package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.CorruptedFileException;
import com.questoftherealm.exceptions.FileNotLoaded;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoadGameTest {

    @TempDir
    Path tempDir;

    private LoadGame loadGame;
    private File localSaveDir;
    private File serverSaveDir;

    @Mock
    private GameState state;
    @Mock
    private GameServices services;
    @Mock
    private Output output;
    @Mock
    private LocalizationService localizationService;
    @Mock
    private MessageBundle messageBundle;
    @Mock
    private ItemRegistry itemRegistry;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        // Define temp directories for single and multiplayer saves
        localSaveDir = tempDir.resolve("saves").toFile();
        serverSaveDir = tempDir.resolve("server_saves").toFile();
        
        // Ensure directories exist if needed by tests, though LoadGame checks nicely
        // For constructor, just passing strings
        loadGame = new LoadGame(localSaveDir.getAbsolutePath(), serverSaveDir.getAbsolutePath());

        // Setup common mocks
        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        when(state.getMessages()).thenReturn(localizationService);
        when(localizationService.getBundle()).thenReturn(messageBundle);
        when(state.getItemRegistry()).thenReturn(itemRegistry);
        
        // Bundle default behavior
        when(messageBundle.get(anyString())).thenAnswer(inv -> inv.getArgument(0));

        // State active players default
        when(state.getActivePlayers()).thenReturn(Collections.emptyList());
    }

    @Test
    void givenMissingFile_whenLoadGameInformation_thenThrowFileNotLoaded() {
        // Arrange
        String filename = "nonExistentSave";

        // Act & Assert
        // We expect FileNotLoaded
        // MessageBundle mock returns key name, so "loadGame.file.notFound"
        FileNotLoaded exception = assertThrows(FileNotLoaded.class, () -> {
            loadGame.loadGameInformation(filename, state);
        });

        assertEquals("loadGame.file.notFound", exception.getMessage());
    }

    @Test
    void givenCorruptedFile_whenLoadGameInformation_thenThrowCorruptedFileException() throws IOException {
        // Arrange
        if (!localSaveDir.exists()) localSaveDir.mkdirs();
        File corruptedFile = new File(localSaveDir, "badSave.json");
        Files.writeString(corruptedFile.toPath(), "{ \"invalid\": json structure ");

        // Act & Assert
        CorruptedFileException exception = assertThrows(CorruptedFileException.class, () -> {
            loadGame.loadGameInformation("badSave", state);
        });

        assertEquals("loadGame.file.corrupted", exception.getMessage());
    }

    @Test
    void givenEmptySaveDirectory_whenPrintSaves_thenPrintNoneMessage() {
        // Arrange
        if (!localSaveDir.exists()) localSaveDir.mkdirs();
        // Dir exists but empty

        // Act
        loadGame.printSaves(state);

        // Assert
        verify(output).println("loadGame.saves.header");
        verify(output).println("loadGame.saves.none");
        verify(output, never()).println("loadGame.saves.footer");
    }

    @Test
    void givenSaveDirectoryWithFiles_whenPrintSaves_thenListFiles() throws IOException {
        // Arrange
        if (!localSaveDir.exists()) localSaveDir.mkdirs();
        File save1 = new File(localSaveDir, "hero.json");
        File save2 = new File(localSaveDir, "mage.json");
        Files.writeString(save1.toPath(), "{}");
        Files.writeString(save2.toPath(), "{}");

        // Act
        loadGame.printSaves(state);

        // Assert
        verify(output).println("loadGame.saves.header");
        verify(output).println("hero.json");
        verify(output).println("mage.json");
        verify(output).println("loadGame.saves.footer");
    }

    @Test
    void givenMissingSaveDirectory_whenPrintSaves_thenPrintDirMissing() {
        // Arrange
        // localSaveDir is inside tempDir but we haven't created it via mkdirs() yet
        // so it should not exist.
        
        // Act
        loadGame.printSaves(state);

        // Assert
        verify(output).println("loadGame.saves.dirMissing");
    }
}

