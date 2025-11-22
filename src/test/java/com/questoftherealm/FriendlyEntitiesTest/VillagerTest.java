package com.questoftherealm.FriendlyEntitiesTest;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.expeditions.missions.Investigate_Northern_Villages;
import com.questoftherealm.expeditions.missions.Travel_North;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.*;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class VillagerTest {
    private Villager villager;
    private GameState state;
    private Player player;
    private Output output;
    private GameServices services;
    private Map gameMap;

    private static final String VILLAGER_SEARCH_FIRST = "We're too scared to talk! Search the area for signs of danger first!";
    private static final String VILLAGER_DEFEAT_ENEMIES = "Clear the area to talk to the villager.";
    private static final String VILLAGER_EXPLORED1 = "✅ The Villager sadly nods. \"We saw them march through... they took everything and headed North.\"";
    private static final String VILLAGER_EXPLORED2 = "✅ Another survivor whispers, \"They were Goblins. They were looking for something.\"";
    private static final String VILLAGER_AVOIDS = "The second survivor avoids eye contact. \"I cannot help you further.\"";
    private static final String VILLAGER_NOT_KNOWN = "The villager doesn’t seem to know you.";
    private static final String VILLAGER_HAS_NOTHING = "The villager has nothing to say right now.";

    @BeforeEach
    void setup() {
        Player realPlayer = new Player("Test", PlayerTypes.Mage);
        player = spy(realPlayer);
        output = mock(ConsoleOutput.class);
        services = new GameServices(output);
        state = new GameState(player, output, services);
        gameMap = new Map();
        state.setMap(gameMap);
        NpcInitializer initializer = new NpcInitializer();
        initializer.registerAll(state);
    }

    @Test
    void talkingWithVillagerProperQuestNotCurrent() {
        player.setPosition(GameConstants.NorthVillage_1);
        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);
        verify(output).println(VILLAGER_NOT_KNOWN);
    }

    @Test
    void talkingWithVillagerOutsideOfVillage() {
        player.setPosition(GameConstants.NorthVillage_1);
        when(player.getCurQuest()).thenReturn(new NorthExploration());
        when(player.getCurMission()).thenReturn(new Travel_North());

        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);
        verify(output).println(VILLAGER_HAS_NOTHING);
    }

    @Test
    void talkingWithNorthVillager1Valid() {
        player.setPosition(GameConstants.NorthVillage_1);
        NorthExploration q = new NorthExploration();
        when(player.getCurQuest()).thenReturn(q);
        when(player.getCurMission()).thenReturn(new Investigate_Northern_Villages());
        q.setSearchedVillage1(true);

        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);
        verify(output).println(VILLAGER_EXPLORED1);
    }

    @Test
    void talkingWithNorthVillager2NotSearched() {
        player.setPosition(GameConstants.NorthVillage_2);
        NorthExploration quest = new NorthExploration();
        when(player.getCurQuest()).thenReturn(quest);
        when(player.getCurMission()).thenReturn(new Investigate_Northern_Villages());

        quest.setSearchedVillage2(false);

        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);

        verify(output).println(VILLAGER_SEARCH_FIRST);
    }

    @Test
    void talkingWithNorthVillager2EnemiesPresent() {
        player.setPosition(GameConstants.NorthVillage_2);
        NorthExploration quest = new NorthExploration();
        when(player.getCurQuest()).thenReturn(quest);
        when(player.getCurMission()).thenReturn(new Investigate_Northern_Villages());

        quest.setSearchedVillage2(true);

        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        curTile.getEnemies().add(mock(Enemy.class));

        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);

        verify(output).println(VILLAGER_DEFEAT_ENEMIES);
    }

    @Test
    void talkingWithNorthVillager2ValidFirstTime() {
        player.setPosition(GameConstants.NorthVillage_2);
        NorthExploration quest = new NorthExploration();
        when(player.getCurQuest()).thenReturn(quest);
        when(player.getCurMission()).thenReturn(new Investigate_Northern_Villages());

        quest.setSearchedVillage2(true);
        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        curTile.getEnemies().clear();

        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);

        verify(output).println(VILLAGER_EXPLORED2);
    }

    @Test
    void talkingWithNorthVillager2AlreadyTalked() {
        player.setPosition(GameConstants.NorthVillage_2);

        NorthExploration q = new NorthExploration();
        q.setSearchedVillage2(true);
        q.setTalkedToVillager2(true);

        when(player.getCurQuest()).thenReturn(q);
        when(player.getCurMission()).thenReturn(new Investigate_Northern_Villages());

        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);

        villager.talk(state, player, true);

        verify(output).println(VILLAGER_AVOIDS);
    }
}
