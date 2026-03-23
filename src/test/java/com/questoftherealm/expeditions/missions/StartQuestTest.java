package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.quest.QuestFactory;
import com.questoftherealm.expeditions.quest.quests.NorthExploration;
import com.questoftherealm.expeditions.quest.quests.StartQuest;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StartQuestTest {

    private Player player;
    private StartQuest startQuest;
    private GameState state;
    private Output output;

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        GameServices services = new GameServices(output);

        player = new Player(
                "Hero",
                PlayerTypes.Warrior,
                1, 0, 0,
                0, 0, "Zone",
                null, null,
                new Inventory(50),
                null,
                null,
                false
        );

        state = new GameState(player, services);

        startQuest = new StartQuest(player, state);
        player.setCurQuest(startQuest);
        player.setCurMission(startQuest.getMissions().get(0));

        for (var m : startQuest.getMissions()) {
            m.setState(state);
        }
    }


    @Test
    @DisplayName("Meet_the_Elder completes ONLY after ElderHasTalked is true")
    void meetTheElderCompletion() {
        Mission mission = startQuest.getMissions().stream().filter(m -> m.getMissionType() == Missions.MEET_ELDER).toList().getFirst();
        mission.checkCompletion();
        assertFalse(mission.isCompleted());

        startQuest.setElderHasTalked(true);
        mission.checkCompletion();
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Meet_the_Elder does not complete when player is not on StartQuest")
    void elderMissionFailsIfWrongQuest() {
        Mission mission = startQuest.getMissions().stream().filter(m -> m.getMissionType() == Missions.MEET_ELDER).toList().getFirst();
        startQuest.setElderHasTalked(true);
        player.setCurQuest(mock(NorthExploration.class));
        mission.checkCompletion();
        assertFalse(mission.isCompleted());
    }


    @Test
    @DisplayName("Gather Supplies requires >=1 potion AND >=5 food")
    void gatherSuppliesLogic() {
        ItemRegistry itemRegistry = new ItemRegistry(new LocalizationService());
        Mission mission = startQuest.getMissions().stream().filter(m -> m.getMissionType() == Missions.GATHER_SUPPLIES).toList().getFirst();
        var inv = player.getInventory();
        inv.clear();

        mission.checkCompletion();
        assertFalse(mission.isCompleted());
        inv.addItem(itemRegistry.getItem("Health Potion"), 1, state);
        mission.checkCompletion();
        assertFalse(mission.isCompleted());
        inv.clear();

        inv.addItem(itemRegistry.getItem("Dried Meat"), 4, state);
        mission.checkCompletion();
        assertFalse(mission.isCompleted());
        inv.clear();

        inv.addItem(itemRegistry.getItem("Dried Meat"), 5, state);
        mission.checkCompletion();
        assertFalse(mission.isCompleted());
        inv.addItem(itemRegistry.getItem("Health Potion"), 1, state);
        mission.checkCompletion();
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("StartQuest completes when ALL missions completed and updateStatus is called")
    void questCompletesWhenAllMissionsDone() {
        QuestFactory q = new QuestFactory(player, state);
        player.setQuestFactory(q);
        startQuest =(StartQuest) q.getQuests().peek();
        assertNotNull(startQuest);
        for (var m : startQuest.getMissions()) {
            m.setCompleted(true);
        }

        assertFalse(startQuest.isCompleted());
        assertNotNull(player.getQuestFactory());
        startQuest.updateStatus(state);

        assertTrue(startQuest.isCompleted());
        verify(output).println("You have completed Start Journey successfully");
    }
}
