package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleTest {

    private Player player;
    private Characters playerChar;
    private Enemy enemy;
    private GameState state;
    private GameServices services;
    private Output output;
    private Input input;

    private RandomService random;


    @BeforeEach
    void setup() {
        output = mock(Output.class);
        input = mock(Input.class);

        random = mock(RandomService.class);
        when(random.randomDouble(1)).thenReturn(0.9);
        when(random.randomInt(anyInt(), anyInt())).thenReturn(1);

        services = mock(GameServices.class);
        when(services.getOutput()).thenReturn(output);
        when(services.getInput()).thenReturn(input);
        when(services.getRandom()).thenReturn(random);

        state = new GameState("test-room", services);

        player = mock(Player.class);
        playerChar = mock(Characters.class);
        when(player.getPlayerCharacter()).thenReturn(playerChar);

        enemy = mock(Enemy.class);
        when(enemy.getType()).thenReturn(EnemyType.GOBLIN);
        when(enemy.getLoot()).thenReturn(List.of());
        when(enemy.getXpReward()).thenReturn(0);
        when(enemy.getGoldReward()).thenReturn(0);
        when(enemy.getHealth()).thenReturn(0);
        when(enemy.getBaseAttack()).thenReturn(0);
    }

    @Test
    void givenPlayerSurvivesAndEnemyDies_whenSimulate_thenReturnsWin() {
        when(playerChar.isDead()).thenReturn(false, false);

        when(enemy.isAlive()).thenReturn(true, false, false);
        when(input.nextLine()).thenReturn("1");
        when(enemy.getXpReward()).thenReturn(25);
        when(enemy.getGoldReward()).thenReturn(12);

        Battle battle = new Battle(player, enemy, state);
        boolean result = battle.simulate();

        assertTrue(result, "Player should win automatically");
        verify(player).addExp(25);
        verify(player).addMoney(12, state);
    }

    @Test
    void givenPlayerDies_whenSimulate_thenReturnsLoss() {

        when(playerChar.isDead()).thenReturn(false, true, true);
        when(enemy.isAlive()).thenReturn(true, true);
        when(input.nextLine()).thenReturn("1");

        Battle battle = new Battle(player, enemy, state);
        boolean result = battle.simulate();

        assertFalse(result, "Player should lose automatically");
        verify(player).setDead();
    }

    @Test
    void givenOptionAttack_whenSimulate_thenPlayerAttackIsExecuted() {
        when(playerChar.isDead()).thenReturn(false, false);
        when(playerChar.getIntelligence()).thenReturn(0);
        when(playerChar.getAttack()).thenReturn(10);
        when(enemy.isAlive()).thenReturn(true, false, false);
        when(input.nextLine()).thenReturn("1");

        Battle battle = new Battle(player, enemy, state);
        battle.simulate();

        verify(playerChar).attack(enemy, player, state);
    }

    @Test
    void givenOptionBlock_whenSimulate_thenIncomingDamageIsReducedAndApplied() {
        when(playerChar.isDead()).thenReturn(false, false, false);
        when(playerChar.getDefence()).thenReturn(2);
        when(enemy.getBaseAttack()).thenReturn(10);
        when(enemy.isAlive()).thenReturn(true, true, false, false);
        when(input.nextLine()).thenReturn("2");

        Battle battle = new Battle(player, enemy, state);
        battle.simulate();

        verify(playerChar, atLeastOnce()).takeDamage(anyInt(), eq(state), eq(player));
    }

    @Test
    void givenOptionUseItem_whenSimulate_thenInventoryFlowIsTriggered() {
        when(playerChar.isDead()).thenReturn(false, false, false);
        when(enemy.isAlive()).thenReturn(true, true, false, false);
        when(input.nextLine()).thenReturn("3", "NotARealItem");

        Battle battle = new Battle(player, enemy, state);
        battle.simulate();

        verify(player).openInventory(state);
    }

    @Test
    void givenOptionFleeWithSuccessfulRoll_whenSimulate_thenReturnsFalseImmediately() {
        when(playerChar.isDead()).thenReturn(false);
        when(enemy.isAlive()).thenReturn(true);
        when(input.nextLine()).thenReturn("4");
        when(random.randomDouble(1)).thenReturn(0.05);

        Battle battle = new Battle(player, enemy, state);
        boolean result = battle.simulate();

        assertFalse(result);
    }

    @Test
    void givenInvalidOption_whenSimulate_thenPrintsInvalidChoiceAndContinues() {
        when(playerChar.isDead()).thenReturn(false, false);
        when(playerChar.getIntelligence()).thenReturn(0);
        when(playerChar.getAttack()).thenReturn(10);
        when(enemy.isAlive()).thenReturn(true, true, false, false);
        when(input.nextLine()).thenReturn("9", "1");

        Battle battle = new Battle(player, enemy, state);
        battle.simulate();

        verify(output).println(contains("Invalid"));
        verify(playerChar).attack(enemy, player, state);
    }
}
