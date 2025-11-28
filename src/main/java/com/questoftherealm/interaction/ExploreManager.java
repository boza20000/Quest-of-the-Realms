package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyFactory;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.map.Locations;

public class ExploreManager {
    private GameState state;
    private Output output;
    private SlowPrinter slowPrinter;

    public void exploreStructure(Locations structure, Player player, GameState state) {
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this.slowPrinter = new SlowPrinter(state);
        slowPrinter.slowPrint("\n" + state.getMessages().getBundle().get("explore.arrive", structure.getName()));
        slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.description", structure.getDescription()));
        slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.choice.prompt"));
        slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.choice.1"));
        slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.choice.2"));
        slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.choice.3"));
        int choice = getChoice(3);

        switch (choice) {
            case 1 -> enterStructure(player, structure);
            case 2 -> observeStructure(player, structure);
            default -> slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.leave"));
        }
    }

    private void enterStructure(Player player, Locations structure) {
        slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.enter.start"));
        pause();
        int outcome = state.getGameServices().getRandom().randomInt(100);

        switch (structure) {
            case ABANDONED_TOWER, MAGES_TOWER -> exploreTower(player, outcome);
            case SHADOW_CAVERN, IRON_MINE -> exploreCave(player, outcome);
            case BANDIT_CAMP, FORGOTTEN_RUINS -> exploreRuins(player, outcome);
            case SACRED_GROVE, ANCIENT_ALTAR -> exploreSacredPlace(player, outcome);
            default -> exploreGeneric(player, outcome);
        }
    }

    private void exploreTower(Player player, int outcome) {
        if (outcome < 30) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.tower.ghost"));
            Enemy spirit = EnemyFactory.createEnemy(EnemyType.LOST_SPIRIT, state);
            spirit.interact(player, state);
        } else if (outcome < 60) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.tower.chest"));
            findLoot(player);
        } else {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.tower.empty"));
        }
    }

    private void exploreCave(Player player, int outcome) {
        if (outcome < 40) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.cave.beast"));
            Enemy beast = EnemyFactory.createEnemy(EnemyType.WOLF, state);
            beast.interact(player, state);
        } else if (outcome < 70) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.cave.crystal"));
            findLoot(player);
        } else {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.cave.empty"));
        }
    }

    private void exploreRuins(Player player, int outcome) {
        if (outcome < 35) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.ruins.trap"));
            player.getPlayerCharacter().takeDamage(10, state);
        } else if (outcome < 65) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.ruins.relic"));
            findLoot(player);
        } else {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.ruins.empty"));
        }
    }

    private void exploreSacredPlace(Player player, int outcome) {
        Characters character = player.getPlayerCharacter();
        if (outcome < 25) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.sacred.attack"));
            character.takeDamage(8, state);
        } else if (outcome < 55) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.sacred.heal"));
            character.setHealth(character.getHealth() + 10);
        } else {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.sacred.herb"));
            findLoot(player);
        }
    }

    private void exploreGeneric(Player player, int outcome) {
        if (outcome < 50) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.generic.find"));
            findLoot(player);
        } else {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.generic.empty"));
        }
    }

    private void observeStructure(Player player, Locations structure) {
        int roll = state.getGameServices().getRandom().randomInt(100);
        if (roll < 30) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.observe.spotted"));
        } else {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.observe.notes"));
        }
    }

    private void findLoot(Player player) {
        Chest chest = new Chest(state);
        ItemDrop loot = chest.generateRandomItem();
        player.getInventory().addItem(loot.item(), loot.quantity(), state);
        slowPrinter.slowPrint(state.getMessages().getBundle().get("explore.loot", loot.item().getName()));
    }

    private int getChoice(int max) {
        output.print("> ");
        try {
            int choice = Integer.parseInt(state.getGameServices().getInput().nextLine());
            return Math.max(1, Math.min(choice, max));
        } catch (Exception e) {
            return 3;
        }
    }

    private void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
            output.println("sleep failed");
        }
    }
}
