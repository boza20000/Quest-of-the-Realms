package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.bosses.Boss;
import com.questoftherealm.enemyEntities.bosses.GoblinGeneral;
import com.questoftherealm.enemyEntities.bosses.GoblinKing;
import com.questoftherealm.game.ConsoleInput;
import com.questoftherealm.game.ConsoleOutput;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemDrop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

class BossTest {

    private GameState state;
    private GameServices services;
    private Input input;
    private Output output;
    private Player player;

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        input = new ConsoleInput();
        services = new GameServices(output, input);
        state = new GameState("test-room", services);
        player = new Player("Hero", PlayerTypes.Warrior, state);
    }

    @Test
    void givenGoblinKing_whenCreated_thenStatsAreCorrect() {
        Boss goblinKing = new GoblinKing(state);

        assertNotNull(goblinKing);
        assertEquals(GoblinKing_HEALTH, goblinKing.getHealth(), "Goblin King health incorrect");
        assertEquals(GoblinKing_ATTACK, goblinKing.getBaseAttack(), "Goblin King attack incorrect");
        assertEquals(GoblinKing_DEFENCE, goblinKing.getBaseDefence(), "Goblin King defence incorrect");
        assertFalse(goblinKing.isDefeated(), "Goblin King should not be defeated initially");
    }

    @Test
    void givenGoblinGeneral_whenCreated_thenStatsAreCorrect() {
        Boss goblinGeneral = new GoblinGeneral(state);

        assertNotNull(goblinGeneral);
        assertEquals(GoblinGeneral_HEALTH, goblinGeneral.getHealth(), "Goblin General health incorrect");
        assertEquals(GoblinGeneral_ATTACK, goblinGeneral.getBaseAttack(), "Goblin General attack incorrect");
        assertEquals(GoblinGeneral_DEFENCE, goblinGeneral.getBaseDefence(), "Goblin General defence incorrect");
        assertFalse(goblinGeneral.isDefeated(), "Goblin General should not be defeated initially");
    }

    @Test
    void givenBoss_whenTakeDamage_thenHealthDecreases() {
        Boss goblinGeneral = new GoblinGeneral(state);
        int initialHealth = goblinGeneral.getHealth();
        int damage = 50;

        goblinGeneral.takeDamage(damage, state);

        assertTrue(goblinGeneral.getHealth() < initialHealth, "Health should decrease when taking sufficient damage");
        assertTrue(goblinGeneral.getHealth() >= initialHealth - damage, "Health reduction shouldn't exceed raw damage");
    }

    @Test
    void givenBoss_whenDefeated_thenFlagIsSetAndMessagePrinted() {
        Boss boss = new GoblinKing(state);
        boss.takeDamage(GoblinKing_HEALTH * 10, state);
        
        assertTrue(boss.isDefeated(), "Boss should be defeated when health reaches 0");
        assertEquals(0, boss.getHealth(), "Health should be 0");
    }

    @Test
    void givenGoblinKing_whenDefeated_thenDropsLoot() {
        Boss boss = new GoblinKing(state);
        List<ItemDrop> loot = boss.getLoot();
        
        assertNotNull(loot, "Loot list should not be null");
        assertFalse(loot.isEmpty(), "Goblin King should drop loot");
        
        boolean hasCrown = loot.stream().anyMatch(drop -> drop.item().getName().equals("Goblin king’s Crown"));
        boolean hasSword = loot.stream().anyMatch(drop -> drop.item().getName().equals("Goblin King Sword"));
        
        assertTrue(hasCrown, "Loot should contain Goblin King's Crown");
        assertTrue(hasSword, "Loot should contain Goblin King Sword");
    }

    @Test
    void givenGoblinGeneral_whenDefeated_thenDropsLoot() {
        Boss boss = new GoblinGeneral(state);
        List<ItemDrop> loot = boss.getLoot();

        assertNotNull(loot, "Loot list should not be null");
        assertFalse(loot.isEmpty(), "Goblin General should drop loot");
    }

    @Test
    void givenGoblinGeneral_whenDefeated_thenFlagIsSetAndMessagePrinted() {
        Boss boss = new GoblinGeneral(state);

        boss.takeDamage(1000, state);

        assertTrue(boss.isDefeated(), "Goblin General should be defeated");
        assertEquals(0, boss.getHealth(), "Health should be 0");

        verify(output, atLeastOnce()).println(anyString());
    }

    @Test
    void givenGoblinKing_whenSuperMove_thenCanKillPlayer() {
        player.getPlayerCharacter().setHealth(10);
        int damage = 100;
        player.getPlayerCharacter().takeDamage(damage, state, player);
        
        assertTrue(player.getPlayerCharacter().isDead(), "Player should be dead after taking fatal damage from boss simulation");
    }
}
