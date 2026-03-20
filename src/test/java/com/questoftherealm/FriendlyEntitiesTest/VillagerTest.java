package com.questoftherealm.FriendlyEntitiesTest;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.expeditions.missions.MissionFactory;
import com.questoftherealm.expeditions.missions.Missions;
import com.questoftherealm.expeditions.quest.quests.NorthExploration;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.*;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.WorldMap;
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
    private WorldMap gameMap;
    private Input input;

    private static final String VILLAGER_SEARCH_FIRST = "We're too scared to talk! Search the area for signs of danger first!";
    private static final String VILLAGER_DEFEAT_ENEMIES = "Clear the area to talk to the villager.";
    private static final String VILLAGER_EXPLORED1 = "✅ The Villager sadly nods. \"We saw them march through... they took everything and headed North.\"";
    private static final String VILLAGER_EXPLORED2 = "✅ Another survivor whispers, \"They were Goblins. They were looking for something.\"";
    private static final String VILLAGER_AVOIDS = "The second survivor avoids eye contact. \"I cannot help you further.\"";
    private static final String VILLAGER_NOT_KNOWN = "The villager doesn't seem to know you.";
    private static final String VILLAGER_HAS_NOTHING = "The villager has nothing to say right now.";

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        input = mock(Input.class);
        services = new GameServices(output,input);
        state = new GameState("Test", services);
        gameMap = new WorldMap(state);
        state.setMap(gameMap);
        NpcInitializer initializer = new NpcInitializer();
        initializer.registerAll(state);
        Player realPlayer = new Player("Test", PlayerTypes.Mage, state);
        player = spy(realPlayer);
    }

    @Test
    void givenQuestNotKnownToVillager_whenTalk_thenPrintsNotKnownMessage() {
        player.setPosition(GameConstants.NorthVillage_1);
        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);
        verify(output).println(VILLAGER_NOT_KNOWN);
    }

    @Test
    void
    givenWrongMissionContext_whenTalk_thenPrintsNoDialogueMessage() {
        player.setPosition(GameConstants.NorthVillage_1);
        when(player.getCurQuest()).thenReturn(new NorthExploration());
        when(player.getCurMission()).thenReturn(MissionFactory.createMission(Missions.TRAVEL_NORTH,player,state));
        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);
        verify(output).println(VILLAGER_HAS_NOTHING);
    }

    @Test
    void givenVillageOneSearched_whenTalk_thenPrintsVillageOneIntel() {
        player.setPosition(GameConstants.NorthVillage_1);
        NorthExploration q = new NorthExploration();
        when(player.getCurQuest()).thenReturn(q);
        when(player.getCurMission()).thenReturn(MissionFactory.createMission(Missions.INVESTIGATE_VILLAGES,player,state));
        q.setSearchedVillage1(true);

        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);
        verify(output).println(VILLAGER_EXPLORED1);
    }

    @Test
    void givenVillageTwoNotSearched_whenTalk_thenPrintsSearchFirstWarning() {
        player.setPosition(GameConstants.NorthVillage_2);
        NorthExploration quest = new NorthExploration();
        when(player.getCurQuest()).thenReturn(quest);
        when(player.getCurMission()).thenReturn(MissionFactory.createMission(Missions.INVESTIGATE_VILLAGES,player,state));

        quest.setSearchedVillage2(false);

        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);

        verify(output).println(VILLAGER_SEARCH_FIRST);
    }

    @Test
    void givenVillageTwoHasEnemies_whenTalk_thenPrintsDefeatEnemiesWarning() {
        player.setPosition(GameConstants.NorthVillage_2);
        NorthExploration quest = new NorthExploration();
        when(player.getCurQuest()).thenReturn(quest);
        when(player.getCurMission()).thenReturn(MissionFactory.createMission(Missions.INVESTIGATE_VILLAGES,player,state));
        quest.setSearchedVillage2(true);

        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        Enemy mockEnemy = mock(Enemy.class);
        curTile.addEnemy(mockEnemy);

        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);

        verify(output).println(VILLAGER_DEFEAT_ENEMIES);
    }

    @Test
    void givenVillageTwoClearedFirstConversation_whenTalk_thenPrintsVillageTwoIntel() {
        player.setPosition(GameConstants.NorthVillage_2);
        NorthExploration quest = new NorthExploration();
        when(player.getCurQuest()).thenReturn(quest);
        when(player.getCurMission()).thenReturn(MissionFactory.createMission(Missions.INVESTIGATE_VILLAGES,player,state));
        quest.setSearchedVillage2(true);
        Tile curTile = state.getMap().curZone(player.getX(), player.getY());

        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);
        villager.talk(state, player, true);

        verify(output).println(VILLAGER_EXPLORED2);
    }

    @Test
    void givenVillageTwoAlreadyTalked_whenTalk_thenPrintsAvoidanceLine() {
        player.setPosition(GameConstants.NorthVillage_2);

        NorthExploration q = new NorthExploration();
        q.setSearchedVillage2(true);
        q.setTalkedToVillager2(true);

        when(player.getCurQuest()).thenReturn(q);
        when(player.getCurMission()).thenReturn(MissionFactory.createMission(Missions.INVESTIGATE_VILLAGES,player,state));

        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        villager = (Villager) curTile.getNpcByType(NpcType.VILLAGER);

        villager.talk(state, player, true);

        verify(output).println(VILLAGER_AVOIDS);
    }
}

