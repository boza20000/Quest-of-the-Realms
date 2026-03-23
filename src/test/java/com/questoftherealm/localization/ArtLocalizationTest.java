package com.questoftherealm.localization;

import com.questoftherealm.exceptions.ArtException;
import com.questoftherealm.game.ConsoleInput;
import com.questoftherealm.game.ConsoleOutput;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ArtLocalizationTest {

    private GameState state;
    private GameServices services;
    private Input input;
    private Output output;
    private ArtLocalization artLocalization;

    @BeforeEach
    void setup() {
        output = new ConsoleOutput();
        input = new ConsoleInput();
        services = new GameServices(output, input);
        state = new GameState("test-room", services);
    }

    @Test
    void givenValidArtTxt_whenGetArt_thenReturnsContent() throws IOException {
        artLocalization = new ArtLocalization(state);
        String startMenu = artLocalization.getArt("start_menu");
        assertNotNull(startMenu);
        assertFalse(startMenu.isEmpty());

        String gameOver = artLocalization.getArt("game_over");
        assertNotNull(gameOver);
    }

    @Test
    void givenMissingFile_whenGetArt_thenThrowsArtException() throws FileNotFoundException {
        artLocalization = new ArtLocalization(state, "missing_art.txt");
        assertThrows(ArtException.class, () -> artLocalization.getArt("start_menu"));
    }
    
    @Test
    void givenUnknownKey_whenGetArt_thenReturnsNull() throws IOException {
        artLocalization = new ArtLocalization(state);
        String result = artLocalization.getArt("unknown_key");
        assertNull(result);
    }
}

