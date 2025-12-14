package com.questoftherealm.map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Loot;
import com.questoftherealm.exceptions.RandomItemNotGenerated;
import com.questoftherealm.exceptions.StructureNotGenerated;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.TravelManger;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;

import java.util.*;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Tile {
    private final TileTypes type;
    private final String description;
    private final boolean walkable;
    private Locations structure;
    private List<Enemy> enemies = new ArrayList<>();
    private final List<ItemDrop> drops = new ArrayList<>();
    private boolean contentGenerated = false;
    private final Map<String, Npc> npcRegister = new HashMap<>();
    private TravelManger travelManger;
    private Output output;

    @JsonCreator
    public Tile(@JsonProperty("type") TileTypes type,
                @JsonProperty("description") String description,
                @JsonProperty("walkable") boolean walkable) {
        this.type = type;
        this.description = description;
        this.walkable = walkable;
    }

    public Locations getStructure() {
        return structure;
    }

    public boolean isContentGenerated() {
        return contentGenerated;
    }

    public TileTypes getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public List<ItemDrop> getDrops() {
        return drops;
    }

    public Enemy getEnemy(String name) {
        for (Enemy e : enemies) {
            if (e.getType().toString().equals(name.toUpperCase())) {
                return e;
            }
        }
        return null;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public ItemDrop pickItem(String name) {
        for (ItemDrop item : drops) {
            if (item.item().getName().equals(name)) {
                drops.remove(item);
                return item;
            }
        }
        return null;
    }

    void generateContent(GameState state) {
        travelManger = new TravelManger(state);
        try {
            output.println();
            if (structure == null) {
                this.structure = Locations.generateLocation(type);
            }
            this.enemies = Enemy.generateEnemies(type, state);
            this.drops.clear();
            generateItems(state);
        } catch (RandomItemNotGenerated e) {
            output.println(state.getMessages().getBundle().get("tile.error.items"));
        } catch (StructureNotGenerated e) {
            output.println(state.getMessages().getBundle().get("tile.error.structure"));
        } catch (Exception e) {
            output.println(state.getMessages().getBundle().get("tile.error.general"));
        }
        contentGenerated = true;
    }

    public void onEnter(Player player, GameState state) {
        this.output = state.getGameServices().getOutput();
        generateContent(state);
        listContent(state);
    }

    private void listContent(GameState state) {
        displayItems(state);
        displayLocation(state);
        displayEnemies(state);
        displayNpc(state);
    }

    private void displayNpc(GameState state) {
        if (!this.npcRegister.isEmpty()) {
            output.println(state.getMessages().getBundle().get("player.see.npc"));
            for (Npc n : npcRegister.values()) {
                output.println("-" + n.getType());
            }
        }
    }

    private void displayLocation(GameState state) {
        if (structure == null) return;
        output.println();
        String structureName = state.getMessages().getBundle().get(structure.getName());
        output.println(travelManger.getRandomSpotting(structureName));
        output.println(structureName + " " + state.getMessages().getBundle().get(structure.getDescription()));
    }

    private void displayItems(GameState state) {
        if (!drops.isEmpty()) {
            output.println(state.getMessages().getBundle().get("tile.items.found"));
            printAvailableItems(state);
        }
    }

    private void displayEnemies(GameState state) {
        if (enemies.isEmpty()) {
            output.println(state.getMessages().getBundle().get("tile.enemies.none"));
            return;
        }
        for (Enemy e : enemies) {
            output.println(state.getMessages().getBundle().get("tile.enemies.spotted", e.getClass().getSimpleName()));
        }
    }

    public void generateItems(GameState state) {
        int itemCount = ThreadLocalRandom.current().nextInt(GameConstants.MAX_ITEM_DROPS + 1);
        Chest chest = new Chest(state);
        for (int i = 0; i < itemCount; i++) {
            drops.add(chest.generateRandomItem());
        }
    }

    public void printAvailableItems(GameState state) {
        if (drops.isEmpty()) {
            output.println(state.getMessages().getBundle().get("tile.items.none"));
        }
        for (ItemDrop item : drops) {
            output.println("-" + item.quantity() + "x " + item.item().getName());
        }
    }

    public void removeDrop(Item drop, int quantity, GameState state) {
        if (drop == null || quantity <= 0) {
            throw new IllegalArgumentException(state.getMessages().getBundle().get("tile.items.invalid"));
        }
        for (int i = 0; i < drops.size(); i++) {
            ItemDrop tileItem = drops.get(i);
            if (tileItem.item().getName().equals(drop.getName())) {
                int newQty = tileItem.quantity() - quantity;
                if (newQty <= 0) {
                    drops.remove(i);
                } else {
                    drops.set(i, new ItemDrop(drop, newQty));
                }
                return;
            }
        }
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public boolean isEmpty() {
        return drops.isEmpty() && enemies.isEmpty();
    }

    public void registerNpc(Npc npc) {
        npcRegister.put(npc.getId(), npc);
    }

    public Npc getNpcById(String id) {
        return npcRegister.get(id);
    }

    public Npc getNpcByType(NpcType type) {
        return npcRegister.values()
                .stream()
                .filter(n -> n.getType() == type)
                .findFirst()
                .orElse(null);
    }

    public void addEnemyLoot(Enemy enemy, GameState state) {
        List<ItemDrop> enemyItems = new ArrayList<>();
        for (Loot l : enemy.getLoot()) {
            Random roll = state.getGameServices().getRandom().random();
            if (roll.nextDouble() > l.chance()) {
                int quantity = roll.nextInt(l.min(), l.max());
                enemyItems.add(new ItemDrop(l.item(), quantity));
            }
        }
        this.drops.addAll(enemyItems);
    }
}
