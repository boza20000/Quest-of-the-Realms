package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.EnemyFactory;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.expeditions.missions.Missions;
import com.questoftherealm.expeditions.quest.quests.GoblinAmbush;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemDrop;

public class MissionInteractions {
    private GameState state;
    private Output output;
    private SlowPrinter slowPrinter;

    public MissionInteractions(GameState state) {
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this.slowPrinter = new SlowPrinter(state);
    }

    public void worldStart(Player player) {
        if (player.getCurMission().getMissionType().equals(Missions.MEET_ELDER) && !player.getCurMission().isCompleted()) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.start.elder"));
        } else {
            output.println(state.getMessages().getBundle().get("mission.start.generic", player.getCurrentZone()));
        }
    }

    public void elderDialogue(String name, ItemDrop weapon, ItemDrop helmet, ItemDrop chestplate, ItemDrop boots) {
        slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.elder.dialogue", name));
        slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.elder.weapon", weapon.item().getEffect(), weapon.item().getName()));
        slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.elder.armor", helmet.item().getName(), chestplate.item().getName(), boots.item().getName()));
        slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.elder.rations"));
    }

    public void villagerDialogue(Player player, int villageId) {
        if (villageId == 1) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.village.dialogue1", player.getName()));
        } else {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.village.dialogue2"));
        }
    }

    public void villageIntro_1() {
        slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.village.intro1"));
    }

    public void villageIntro_2() {
        slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.village.intro2"));
    }

    public void goblinCampSpotted() {
        slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.goblin.camp.spotted"));
    }

    public void goblinsTalkingOverheard() {
        slowPrinter.slowPrint(state.getMessages().getBundle().get("mission.goblin.overheard"));
    }

    public void makeDecision(Player player) {
        output.println(state.getMessages().getBundle().get("mission.goblin.encounter"));
        output.println(state.getMessages().getBundle().get("mission.goblin.choices"));

        output.print("> ");
        int choice = state.getGameServices().getInput().nextInt();
        if (!(player.getCurQuest() instanceof GoblinAmbush)) {
            return;
        }
        GoblinAmbush q = (GoblinAmbush) player.getCurQuest();
        switch (choice) {
            case 1 -> resolveEscape(player, q, state);
            case 2 -> resolveFight(player, q, state);
            case 3 -> resolveTalk(player, q, state);
            default -> {
                output.println(state.getMessages().getBundle().get("mission.goblin.freeze"));
                resolveFight(player, q, state);
            }
        }
    }

    private void resolveEscape(Player player, GoblinAmbush q, GameState state) {
        int outcome = state.getGameServices().getRandom().randomInt(3);
        switch (outcome) {
            case 0 -> {
                output.println(state.getMessages().getBundle().get("mission.escape.success"));
                q.setPlayerEscapedAmbush(true);
            }
            case 1 -> {
                output.println(state.getMessages().getBundle().get("mission.escape.partial"));
                player.getPlayerCharacter().takeDamage(10, state,player);
                q.setPlayerEscapedAmbush(true);
            }
            case 2 -> {
                output.println(state.getMessages().getBundle().get("mission.escape.fail"));
                EnemyFactory.createEnemy(EnemyType.GOBLIN, state).interact(player,state);
            }
        }
    }

    private void resolveFight(Player player, GoblinAmbush q, GameState state) {
        int outcome = state.getGameServices().getRandom().randomInt(3);
        switch (outcome) {
            case 0 -> {
                output.println(state.getMessages().getBundle().get("mission.fight.success"));
                q.setPlayerEscapedAmbush(true);
            }
            case 1 -> {
                output.println(state.getMessages().getBundle().get("mission.fight.partial"));
                player.getPlayerCharacter().takeDamage(15, state,player);
                q.setPlayerEscapedAmbush(true);
            }
            case 2 -> {
                output.println(state.getMessages().getBundle().get("mission.fight.fail"));
                EnemyFactory.createEnemy(EnemyType.GOBLIN, state).interact(player,state);
            }
        }
    }

    private void resolveTalk(Player player, GoblinAmbush q, GameState state) {
        int outcome = state.getGameServices().getRandom().randomInt(3);
        switch (outcome) {
            case 0 -> {
                output.println(state.getMessages().getBundle().get("mission.talk.success"));
                q.setPlayerEscapedAmbush(true);
            }
            case 1 -> {
                output.println(state.getMessages().getBundle().get("mission.talk.partial"));
                q.setPlayerEscapedAmbush(true);
            }
            case 2 -> {
                output.println(state.getMessages().getBundle().get("mission.talk.fail"));
                EnemyFactory.createEnemy(EnemyType.GOBLIN, state).interact(player,state);
            }
        }
    }

}
