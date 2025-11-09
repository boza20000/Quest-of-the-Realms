package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyFactory;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.exceptions.SleepException;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.map.Locations;
import com.questoftherealm.localization.MessageBundle;

import java.util.Random;
import java.util.Scanner;

public class ExploreManager {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public void exploreStructure(Locations structure, Player player) {
        SlowPrinter.slowPrint("\n" + MessageBundle.get("explore.arrive", structure.getName()));
        SlowPrinter.slowPrint(MessageBundle.get("explore.description", structure.getDescription()));
        SlowPrinter.slowPrint(MessageBundle.get("explore.choice.prompt"));
        SlowPrinter.slowPrint(MessageBundle.get("explore.choice.1"));
        SlowPrinter.slowPrint(MessageBundle.get("explore.choice.2"));
        SlowPrinter.slowPrint(MessageBundle.get("explore.choice.3"));
        int choice = getChoice(3);

        switch (choice) {
            case 1 -> enterStructure(player, structure);
            case 2 -> observeStructure(player, structure);
            default -> SlowPrinter.slowPrint(MessageBundle.get("explore.leave"));
        }
    }

    private static void enterStructure(Player player, Locations structure) {
        SlowPrinter.slowPrint(MessageBundle.get("explore.enter.start"));
        pause();
        int outcome = random.nextInt(100);

        switch (structure) {
            case ABANDONED_TOWER, MAGES_TOWER -> exploreTower(player, outcome);
            case SHADOW_CAVERN, IRON_MINE -> exploreCave(player, outcome);
            case BANDIT_CAMP, FORGOTTEN_RUINS -> exploreRuins(player, outcome);
            case SACRED_GROVE, ANCIENT_ALTAR -> exploreSacredPlace(player, outcome);
            default -> exploreGeneric(player, outcome);
        }
    }

    private static void exploreTower(Player player, int outcome) {
        if (outcome < 30) {
            SlowPrinter.slowPrint(MessageBundle.get("explore.tower.ghost"));
            Enemy spirit = EnemyFactory.createEnemy(EnemyType.LOST_SPIRIT);
            spirit.interact(player);
        } else if (outcome < 60) {
            SlowPrinter.slowPrint(MessageBundle.get("explore.tower.chest"));
            findLoot(player);
        } else {
            SlowPrinter.slowPrint(MessageBundle.get("explore.tower.empty"));
        }
    }

    private static void exploreCave(Player player, int outcome) {
        if (outcome < 40) {
            SlowPrinter.slowPrint(MessageBundle.get("explore.cave.beast"));
            Enemy beast = EnemyFactory.createEnemy(EnemyType.WOLF);
            beast.interact(player);
        } else if (outcome < 70) {
            SlowPrinter.slowPrint(MessageBundle.get("explore.cave.crystal"));
            findLoot(player);
        } else {
            SlowPrinter.slowPrint(MessageBundle.get("explore.cave.empty"));
        }
    }

    private static void exploreRuins(Player player, int outcome) {
        if (outcome < 35) {
            SlowPrinter.slowPrint(MessageBundle.get("explore.ruins.trap"));
            player.getPlayerCharacter().takeDamage(10);
        } else if (outcome < 65) {
            SlowPrinter.slowPrint(MessageBundle.get("explore.ruins.relic"));
            findLoot(player);
        } else {
            SlowPrinter.slowPrint(MessageBundle.get("explore.ruins.empty"));
        }
    }

    private static void exploreSacredPlace(Player player, int outcome) {
        Characters character = player.getPlayerCharacter();
        if (outcome < 25) {
            SlowPrinter.slowPrint(MessageBundle.get("explore.sacred.attack"));
            character.takeDamage(8);
        } else if (outcome < 55) {
            SlowPrinter.slowPrint(MessageBundle.get("explore.sacred.heal"));
            character.setHealth(character.getHealth() + 10);
        } else {
            SlowPrinter.slowPrint(MessageBundle.get("explore.sacred.herb"));
            findLoot(player);
        }
    }

    private static void exploreGeneric(Player player, int outcome) {
        if (outcome < 50) {
            SlowPrinter.slowPrint(MessageBundle.get("explore.generic.find"));
            findLoot(player);
        } else {
            SlowPrinter.slowPrint(MessageBundle.get("explore.generic.empty"));
        }
    }

    private static void observeStructure(Player player, Locations structure) {
        int roll = random.nextInt(100);
        if (roll < 30) {
            SlowPrinter.slowPrint(MessageBundle.get("explore.observe.spotted"));
        } else {
            SlowPrinter.slowPrint(MessageBundle.get("explore.observe.notes"));
        }
    }

    private static void findLoot(Player player) {
        ItemDrop loot = Chest.generateRandomItem();
        player.getInventory().addItem(loot.item(), loot.quantity());
        SlowPrinter.slowPrint(MessageBundle.get("explore.loot", loot.item().getName()));
    }

    private static int getChoice(int max) {
        System.out.print("> ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return Math.max(1, Math.min(choice, max));
        } catch (Exception e) {
            return 3;
        }
    }

    private static void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
            System.out.println("sleep failed");
        }
    }
}
