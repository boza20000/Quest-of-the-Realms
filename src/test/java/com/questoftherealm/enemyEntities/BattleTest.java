package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.InputService;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.LocalizationService;

import com.questoftherealm.map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleTest {

    private Player player;
    private Characters playerChar;
    private Enemy enemy;
    private GameState state;
    private GameServices services;
    private Output output;
    private InputService input;

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        input = new InputService();
        services = mock(GameServices.class);
        when(services.getOutput()).thenReturn(output);
        when(services.getInput()).thenReturn(input);
        state = mock(GameState.class);
        when(state.getGameServices()).thenReturn(services);

        // Mock player & character
        player = mock(Player.class);
        playerChar = mock(Characters.class);
        when(player.getPlayerCharacter()).thenReturn(playerChar);

        // Mock enemy
        enemy = mock(Enemy.class);
        when(enemy.getType()).thenReturn(EnemyType.GOBLIN);
        when(state.getMessages()).thenReturn(new LocalizationService());

        Map map = new Map(state);
        when(state.getMap()).thenReturn(map);
    }

    @Test
    void testPlayerWinsAutomatically() {
        // Player survives, enemy dies
        when(playerChar.isDead()).thenReturn(false, false, false);
        when(enemy.isAlive()).thenReturn(true, true, false);

        // Simulate input: always attack "1"
        String simulatedInput = "1\n1\n1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Replace input in services with one reading from System.in
        when(services.getInput()).thenReturn(new InputService());

        Battle battle = new Battle(player, enemy, state);
        boolean result = battle.simulate();

        assertTrue(result, "Player should win automatically");
    }

    @Test
    void testPlayerLosesAutomatically() {
        // Player dies
        when(playerChar.isDead()).thenReturn(false, true);
        when(enemy.isAlive()).thenReturn(true, true);

        // Simulate input: always attack "1"
        String simulatedInput = "1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        when(services.getInput()).thenReturn(new InputService());

        Battle battle = new Battle(player, enemy, state);
        boolean result = battle.simulate();

        assertFalse(result, "Player should lose automatically");
    }
}
