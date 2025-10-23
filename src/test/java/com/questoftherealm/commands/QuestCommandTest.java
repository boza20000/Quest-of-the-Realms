package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.missions.Meet_the_Elder;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class QuestCommandTest {
    private Player player;
    private QuestCommand questCommand;
    private ByteArrayOutputStream output;
    private Queue<Quest> quests;

    @BeforeEach
    void setup() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        player = new Player("TestHero", PlayerTypes.Warrior, 1, 0, 0, 0, 0,
                "Spawn", null, null, new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, null);
        Game.setPlayer(player);
        quests = null;
        QuestFactory.getQuests().clear();
        new QuestFactory();
        quests = QuestFactory.getQuests();
        Game.setGameQuests(quests);
        questCommand = new QuestCommand();
    }

    @Test
    void testQuestPrintsTasks() {
        player.setCurQuest(new StartQuest());
        player.setCurMission(new Meet_the_Elder());
        player.updateQuestStatus();
        questCommand.execute(new String[]{"quest"});
        String out = output.toString();
        assertTrue(out.contains("Speak with the castle elder about strange rumors from the north."));
    }

    @Test
    void testNoQuestLoaded() {
        player.setCurQuest(null);
        player.setCurMission(null);
        questCommand.execute(new String[]{"quest"});
        assertTrue(output.toString().contains("Error: No quest loaded"));
    }

    @Test
    void testMissingArgs() {
        questCommand.execute(new String[]{"quest", "extra"});
        assertTrue(output.toString().contains("Usage"));
    }
}
