package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.BattleFactory;
import com.questoftherealm.enemyEntities.entities.Goblin;
import com.questoftherealm.expeditions.missions.*;
import com.questoftherealm.friendlyEntities.Entities.King;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.localization.MessageBundle;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import static com.questoftherealm.expeditions.missions.Escape_to_Safety.*;

public class MissionInteractions {

    private static Random random() {
        return ThreadLocalRandom.current();
    }

    public static void worldStart() {
        Player player = Game.getPlayer();
        if (player.getCurMission() instanceof Meet_the_Elder && !player.getCurMission().isCompleted()) {
            SlowPrinter.slowPrint(MessageBundle.get("mission.start.elder"));
        } else {
            System.out.println(MessageBundle.get("mission.start.generic").formatted(player.getCurrentZone()));
        }
    }

    public static void elderDialogue(String name, ItemDrop weapon, ItemDrop helmet, ItemDrop chestplate, ItemDrop boots) {
        SlowPrinter.slowPrint(MessageBundle.get("mission.elder.dialogue").formatted(name));
        SlowPrinter.slowPrint(MessageBundle.get("mission.elder.weapon")
                .formatted(weapon.item().getEffect(), weapon.item().getName()));
        SlowPrinter.slowPrint(MessageBundle.get("mission.elder.armor")
                .formatted(helmet.item().getName(), chestplate.item().getName(), boots.item().getName()));
        SlowPrinter.slowPrint(MessageBundle.get("mission.elder.rations"));
    }

    public static void villagerDialogue(Player player, int villageId) {
        if (villageId == 1) {
            SlowPrinter.slowPrint(MessageBundle.get("mission.village.dialogue1").formatted(player.getName()));
        } else {
            SlowPrinter.slowPrint(MessageBundle.get("mission.village.dialogue2"));
        }
    }

    public static void villageIntro_1() {
        SlowPrinter.slowPrint(MessageBundle.get("mission.village.intro1"));
    }

    public static void villageIntro_2() {
        SlowPrinter.slowPrint(MessageBundle.get("mission.village.intro2"));
    }

    public static void goblinCampSpotted() {
        SlowPrinter.slowPrint(MessageBundle.get("mission.goblin.camp.spotted"));
    }

    public static void goblinsTalkingOverheard() {
        SlowPrinter.slowPrint(MessageBundle.get("mission.goblin.overheard"));
    }

    public static void makeDecision(Player player) {
        System.out.println(MessageBundle.get("mission.goblin.encounter"));
        System.out.println(MessageBundle.get("mission.goblin.choices"));

        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> resolveEscape(player);
            case 2 -> resolveFight(player);
            case 3 -> resolveTalk(player);
            default -> {
                System.out.println(MessageBundle.get("mission.goblin.freeze"));
                resolveFight(player);
            }
        }
    }

    private static void resolveEscape(Player player) {
        int outcome = random().nextInt(3);
        switch (outcome) {
            case 0 -> {
                System.out.println(MessageBundle.get("mission.escape.success"));
                playerEscapedAmbush = true;
            }
            case 1 -> {
                System.out.println(MessageBundle.get("mission.escape.partial"));
                player.getPlayerCharacter().takeDamage(10);
                playerEscapedAmbush = true;
            }
            case 2 -> {
                System.out.println(MessageBundle.get("mission.escape.fail"));
                BattleFactory.createBattle(player, new Goblin()).simulate();
            }
        }
    }

    private static void resolveFight(Player player) {
        int outcome = random().nextInt(3);
        switch (outcome) {
            case 0 -> {
                System.out.println(MessageBundle.get("mission.fight.success"));
                playerEscapedAmbush = true;
            }
            case 1 -> {
                System.out.println(MessageBundle.get("mission.fight.partial"));
                player.getPlayerCharacter().takeDamage(15);
                playerEscapedAmbush = true;
            }
            case 2 -> {
                System.out.println(MessageBundle.get("mission.fight.fail"));
                BattleFactory.createBattle(player, new Goblin()).simulate();
            }
        }
    }

    private static void resolveTalk(Player player) {
        int outcome = random().nextInt(3);
        switch (outcome) {
            case 0 -> {
                System.out.println(MessageBundle.get("mission.talk.success"));
                playerEscapedAmbush = true;
            }
            case 1 -> {
                System.out.println(MessageBundle.get("mission.talk.partial"));
                playerEscapedAmbush = true;
            }
            case 2 -> {
                System.out.println(MessageBundle.get("mission.talk.fail"));
                BattleFactory.createBattle(player, new Goblin()).simulate();
            }
        }
    }

    public static void reportToKing() {
        SlowPrinter.slowPrint("🏰 " + MessageBundle.get("mission.report.king.intro1"));
        SlowPrinter.slowPrint("🧝‍♂️ " + MessageBundle.get("mission.report.king.intro2"));
        SlowPrinter.slowPrint("👑 " + MessageBundle.get("mission.report.king.line1"));
        SlowPrinter.slowPrint("🧝‍♂️ " + MessageBundle.get("mission.report.king.line2"));
        SlowPrinter.slowPrint("👑 " + MessageBundle.get("mission.report.king.line3"));
        SlowPrinter.slowPrint("🧝‍♂️ " + MessageBundle.get("mission.report.king.line4"));
        SlowPrinter.slowPrint("👑 " + MessageBundle.get("mission.report.king.line5"));
        SlowPrinter.slowPrint("👑 " + MessageBundle.get("mission.report.king.line6"));
        SlowPrinter.slowPrint("⚔️ " + MessageBundle.get("mission.report.king.closure"));

        King.hasTalkedToTheKing = true;
    }
}
