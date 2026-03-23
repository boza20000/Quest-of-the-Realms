package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.exceptions.NotEnoughManaException;
import com.questoftherealm.game.ConsoleOutput;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CharacterTest {

    private Characters testCharacter;
    private Player player;
    private Enemy mockEnemy;
    private GameState state;
    private LocalizationService mockMessages;
    private ItemRegistry mockItemRegistry;
    private MessageBundle mockBundle;
    private Output mockOutput;
    private GameServices mockServices;


    @BeforeEach
    void setUp() {
        testCharacter = new Characters(100, 50, 20, 10, 5, 3, 2, 10) {

            @Override
            public String getDefaultWeapon(GameState state) {
                return "";
            }

            @Override
            public int getBaseAttack() {
                return 20;
            }

            @Override
            public int getBaseDefence() {
                return 10;
            }

            @Override
            public int getMaxHealth() {
                return 100;
            }

            @Override
            public void activateAbility(Player player, Enemy enemy, GameState state) {
            }
        };



        // --- Refactored GameState Setup ---

        // 2. MOCK Localization and ItemRegistry (to satisfy GameState usage)
        mockMessages = mock(LocalizationService.class);
        mockItemRegistry = mock(ItemRegistry.class);
        mockBundle = mock(MessageBundle.class);

        when(mockMessages.getBundle()).thenReturn(mockBundle);
        when(mockBundle.get(anyString(), any())).thenReturn("TEST MESSAGE");
        when(mockBundle.get(anyString())).thenReturn("TEST MESSAGE");

        // 3. MOCK Output and Services
        mockOutput = mock(Output.class);
        mockServices = mock(GameServices.class);
        when(mockServices.getOutput()).thenReturn(mockOutput);

        // 4. MOCK GameState (to be used in test methods)
        state = mock(GameState.class);
        when(state.getGameServices()).thenReturn(mockServices);
        when(state.getMessages()).thenReturn(mockMessages);
        when(state.getItemRegistry()).thenReturn(mockItemRegistry);


        player = new Player("Test", PlayerTypes.Warrior, state);
        mockEnemy = mock(Enemy.class);
        state.setPlayer(player);

    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(100, testCharacter.getHealth());
        assertEquals(50, testCharacter.getMana());
        assertEquals(20, testCharacter.getAttack());
        assertEquals(10, testCharacter.getDefence());
        assertEquals(5, testCharacter.getArmor());
        assertEquals(3, testCharacter.getCharisma());
        assertEquals(2, testCharacter.getSpells());
        assertEquals(10, testCharacter.getIntelligence());
    }

    @Test
    void testSettersClampValues() {
        testCharacter.setHealth(200);
        assertEquals(100, testCharacter.getHealth()); // maxHealth = 100

        testCharacter.setMana(999);
        assertEquals(GameConstants.MAX_MANA, testCharacter.getMana());

        testCharacter.setAttack(-10);
        assertEquals(0, testCharacter.getAttack());
    }

    @Test
    void testTakeDamageReducesHealth() {
        int initialHealth = testCharacter.getHealth();
        // Since testCharacter.takeDamage(damage, state) calls state.getGameServices().getOutput(),
        // we use the 'state' mock set up in the @BeforeEach.
        testCharacter.takeDamage(50, state);
        assertTrue(testCharacter.getHealth() < initialHealth);
    }

    @Test
    void testIsDead() {
        testCharacter.setHealth(0);
        assertTrue(testCharacter.isDead());

        testCharacter.setHealth(10);
        assertFalse(testCharacter.isDead());
    }

    @Test
    void testUseManaReducesMana() {
        int initialMana = testCharacter.getMana();
        testCharacter.useMana(10);
        assertEquals(initialMana - 10, testCharacter.getMana());
        assertThrows(NotEnoughManaException.class,()->testCharacter.useMana(999));
    }

    @Test
    void testAttackAlreadyDeadTarget() {
        when(mockBundle.get(anyString(), any())).thenReturn("already dead");
        when(mockBundle.get(anyString())).thenReturn("already dead");

        // We use the mock objects established in @BeforeEach: state, mockServices, and mockOutput
        when(mockEnemy.isDead()).thenReturn(true);

        // Call the method using the mocked 'state'
        testCharacter.attack(mockEnemy, player, state);

        // Verify output using the mockOutput configured in the setup
        verify(mockOutput).println(contains("already dead"));
    }

    @Test
    void testToStringContainsStats() {
        Output output1 = new ConsoleOutput();
        GameServices gameServices1 = new GameServices(output1);
        GameState state1 = new GameState(player,gameServices1);
        String output = testCharacter.stats(state1);
        assertTrue(output.contains("100")); // health
        assertTrue(output.contains("50"));  // mana
        assertTrue(output.contains("20"));  // attack
    }
}