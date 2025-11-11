package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.BattleFactory;
import com.questoftherealm.enemyEntities.entities.Goblin;
import com.questoftherealm.expeditions.missions.*;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.localization.MessageBundle;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class MissionInteractions {

    private static Random random() {
        return ThreadLocalRandom.current();
    }

    public static void worldStart() {
        Player player = Game.getPlayer();
        if (player.getCurMission() instanceof Meet_the_Elder && !player.getCurMission().isCompleted()) {
            SlowPrinter.slowPrint(MessageBundle.get("mission.start.elder"));
        } else {
            System.out.println(MessageBundle.get("mission.start.generic",player.getCurrentZone()));
        }
    }

    public static void elderDialogue(String name, ItemDrop weapon, ItemDrop helmet, ItemDrop chestplate, ItemDrop boots) {
        SlowPrinter.slowPrint(MessageBundle.get("mission.elder.dialogue",name));
        SlowPrinter.slowPrint(MessageBundle.get("mission.elder.weapon",weapon.item().getEffect(), weapon.item().getName()));
        SlowPrinter.slowPrint(MessageBundle.get("mission.elder.armor",helmet.item().getName(), chestplate.item().getName(), boots.item().getName()));
        SlowPrinter.slowPrint(MessageBundle.get("mission.elder.rations"));
    }

    public static void villagerDialogue(Player player, int villageId) {
        if (villageId == 1) {
            SlowPrinter.slowPrint(MessageBundle.get("mission.village.dialogue1",player.getName()));
        } else {
            SlowPrinter.slowPrint(MessageBundle.get("mission.village.dialogue2"));
        }
    }

    public void villageIntro_1() {
        SlowPrinter.slowPrint(MessageBundle.get("mission.village.intro1"));
    }

    public void villageIntro_2() {
        SlowPrinter.slowPrint(MessageBundle.get("mission.village.intro2"));
    }

    public void goblinCampSpotted() {
        SlowPrinter.slowPrint(MessageBundle.get("mission.goblin.camp.spotted"));
    }

    public void goblinsTalkingOverheard() {
        SlowPrinter.slowPrint(MessageBundle.get("mission.goblin.overheard"));
    }

    public void makeDecision(Player player) {
        System.out.println(MessageBundle.get("mission.goblin.encounter"));
        System.out.println(MessageBundle.get("mission.goblin.choices"));

        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        int choice = scanner.nextInt();
        if (!(player.getCurQuest() instanceof GoblinAmbush)) {
            return;
        }
        GoblinAmbush q = (GoblinAmbush) player.getCurQuest();
        switch (choice) {
            case 1 -> resolveEscape(player, q);
            case 2 -> resolveFight(player, q);
            case 3 -> resolveTalk(player, q);
            default -> {
                System.out.println(MessageBundle.get("mission.goblin.freeze"));
                resolveFight(player, q);
            }
        }
    }

    private void resolveEscape(Player player, GoblinAmbush q) {
        int outcome = random().nextInt(3);
        switch (outcome) {
            case 0 -> {
                System.out.println(MessageBundle.get("mission.escape.success"));
                q.setPlayerEscapedAmbush(true);
            }
            case 1 -> {
                System.out.println(MessageBundle.get("mission.escape.partial"));
                player.getPlayerCharacter().takeDamage(10);
                q.setPlayerEscapedAmbush(true);
            }
            case 2 -> {
                System.out.println(MessageBundle.get("mission.escape.fail"));
                BattleFactory.createBattle(player, new Goblin()).simulate();
            }
        }
    }

    private void resolveFight(Player player, GoblinAmbush q) {
        int outcome = random().nextInt(3);
        switch (outcome) {
            case 0 -> {
                System.out.println(MessageBundle.get("mission.fight.success"));
                q.setPlayerEscapedAmbush(true);
            }
            case 1 -> {
                System.out.println(MessageBundle.get("mission.fight.partial"));
                player.getPlayerCharacter().takeDamage(15);
                q.setPlayerEscapedAmbush(true);
            }
            case 2 -> {
                System.out.println(MessageBundle.get("mission.fight.fail"));
                BattleFactory.createBattle(player, new Goblin()).simulate();
            }
        }
    }

    private void resolveTalk(Player player, GoblinAmbush q) {
        int outcome = random().nextInt(3);
        switch (outcome) {
            case 0 -> {
                System.out.println(MessageBundle.get("mission.talk.success"));
                q.setPlayerEscapedAmbush(true);
            }
            case 1 -> {
                System.out.println(MessageBundle.get("mission.talk.partial"));
                q.setPlayerEscapedAmbush(true);
            }
            case 2 -> {
                System.out.println(MessageBundle.get("mission.talk.fail"));
                BattleFactory.createBattle(player, new Goblin()).simulate();
            }
        }
    }

}
