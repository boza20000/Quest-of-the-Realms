package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.missions.Mission;
import com.questoftherealm.expeditions.missions.Missions;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class MissionInteractionsTest {

    private GameState state;
    private Player player;
    private SlowPrinter slowPrinter;
    private MissionInteractions interactions;
    private Output output = mock(Output.class);
    private Input input = mock(Input.class);

    @BeforeEach
    void setup() {
        GameServices services = new GameServices(output, input, new RandomService());
        state = new GameState("Realm", services);
        
        // Mock slowPrinter to verify calls but avoid sleeps
        slowPrinter = mock(SlowPrinter.class);
        
        interactions = new MissionInteractions(state, slowPrinter);
        player = new Player("Hero", PlayerTypes.Warrior, state);
    }

    @Test
    void worldStartWithElderMission() {
        Mission mission = new Mission(Missions.MEET_ELDER, player, state);
        player.setCurMission(mission);
        
        interactions.worldStart(player);
        
        verify(slowPrinter).slowPrint(anyString());
        verify(output, never()).println(anyString());
    }

    @Test
    void worldStartWithGenericMission() {
        Mission mission = new Mission(Missions.GATHER_SUPPLIES, player, state);
        player.setCurMission(mission);
        
        interactions.worldStart(player);
        
        verify(output).println(anyString());
        verify(slowPrinter, never()).slowPrint(anyString());
    }
    
    @Test
    void villagerDialogues() {
        interactions.villagerDialogue(player, 1);
        verify(slowPrinter).slowPrint(anyString());
        
        reset(slowPrinter);
        interactions.villagerDialogue(player, 2);
        verify(slowPrinter).slowPrint(anyString());
    }

    @Test
    void villageIntros() {
        interactions.villageIntro_1();
        verify(slowPrinter).slowPrint(anyString());
        
        reset(slowPrinter);
        interactions.villageIntro_2();
        verify(slowPrinter).slowPrint(anyString());
    }
    
    @Test
    void goblinEncounters() {
        interactions.goblinCampSpotted();
        verify(slowPrinter).slowPrint(anyString());

        reset(slowPrinter);
        interactions.goblinsTalkingOverheard();
        verify(slowPrinter).slowPrint(anyString());
    }
    
    @Test
    void elderDialogueFlow() {
        Item item = new Item("Sword", ItemType.WEAPON, false, 10, 100, 0, null, null);
        ItemDrop drop = new ItemDrop(item, 1);
        
        interactions.elderDialogue("Elder", drop, drop, drop, drop);
        
        // Expect 4 prints (intro + 3 gifts)
        verify(slowPrinter, times(4)).slowPrint(anyString());
    }
}
