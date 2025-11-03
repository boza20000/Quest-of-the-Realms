package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyFactory;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.map.Locations;

import java.util.Random;
import java.util.Scanner;

public class ExploreManager {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public void exploreStructure(Locations structure, Player player) {
        SlowPrinter.slowPrint("\n🏰 You arrive at the " + structure.getName() + ".");
        SlowPrinter.slowPrint(structure.getDescription());
        SlowPrinter.slowPrint("What would you like to do?");
        SlowPrinter.slowPrint("1. Enter carefully");
        SlowPrinter.slowPrint("2. Observe from outside");
        SlowPrinter.slowPrint("3. Leave it alone");
        int choice = getChoice(3);
        switch (choice) {
            case 1 -> enterStructure(player, structure);
            case 2 -> observeStructure(player, structure);
            default -> SlowPrinter.slowPrint("You decide it's best not to meddle here and move on...");
        }
    }


    private static void enterStructure(Player player, Locations structure) {
        SlowPrinter.slowPrint("You take a deep breath and step inside...");
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
            SlowPrinter.slowPrint("As you climb the stairs, a ghostly spirit materializes!");
            Enemy spirit = EnemyFactory.createEnemy(EnemyType.LOST_SPIRIT);
            spirit.interact(player);
        } else if (outcome < 60) {
            SlowPrinter.slowPrint("You find an old chest hidden under the stairs...");
            findLoot(player);
        } else {
            SlowPrinter.slowPrint("The tower is eerily silent. You find nothing but dust and echoes.");
        }
    }

    private static void exploreCave(Player player, int outcome) {
        if (outcome < 40) {
            SlowPrinter.slowPrint("You descend into darkness... glowing eyes appear in the shadows!");
            Enemy beast = EnemyFactory.createEnemy(EnemyType.WOLF);
            beast.interact(player);
        } else if (outcome < 70) {
            SlowPrinter.slowPrint("You find a strange crystal embedded in the rock. It hums with energy.");
            findLoot(player);
        } else {
            SlowPrinter.slowPrint("You wander the tunnels, but only find damp stone and silence.");
        }
    }

    private static void exploreRuins(Player player, int outcome) {
        if (outcome < 35) {
            SlowPrinter.slowPrint("You trigger an ancient trap! Arrows shoot from the walls!");
            player.getPlayerCharacter().takeDamage(10);
        } else if (outcome < 65) {
            SlowPrinter.slowPrint("Among the rubble, you discover a relic half-buried in moss...");
            findLoot(player);
        } else {
            SlowPrinter.slowPrint("The ruins whisper of old times... but offer no treasure today.");
        }
    }

    private static void exploreSacredPlace(Player player, int outcome) {
        Characters character = player.getPlayerCharacter();
        if (outcome < 25) {
            SlowPrinter.slowPrint("You anger the spirits of nature! Vines lash out at you!");
            character.takeDamage(8);
        } else if (outcome < 55) {
            SlowPrinter.slowPrint("A soothing aura fills the air. You feel healed.");
            character.setHealth(character.getHealth() + 10);
        } else {
            SlowPrinter.slowPrint("You find a glowing herb near the altar — it could be useful later.");
            findLoot(player);
        }
    }

    private static void exploreGeneric(Player player, int outcome) {
        if (outcome < 50) {
            SlowPrinter.slowPrint("You find something useful in the dust...");
            findLoot(player);
        } else {
            SlowPrinter.slowPrint("You explore for a while, but nothing stands out.");
        }
    }

    private static void observeStructure(Player player, Locations structure) {
        int roll = random.nextInt(100);
        if (roll < 30) {
            SlowPrinter.slowPrint("As you watch, something moves inside... you narrowly avoid being seen!");
        } else {
            SlowPrinter.slowPrint("You take some time to sketch and note your observations — perhaps it’ll help later.");
        }
    }

    private static void findLoot(Player player) {
        ItemDrop loot = Chest.generateRandomItem();
        player.getInventory().addItem(loot.item(), loot.quantity());
        SlowPrinter.slowPrint("🎁 You found a " + loot.item().getName() + "!");
    }

    private static int getChoice(int max) {
        System.out.print("> ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return Math.max(1, Math.min(choice, max));
        } catch (Exception e) {
            return 3; // default leave
        }
    }

    private static void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
