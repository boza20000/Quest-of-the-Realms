package com.questoftherealm.map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.RandomItemNotGenerated;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.questoftherealm.items.Chest.generateRandomItem;

public class Tile {
    private final TileTypes type;
    private final String description;
    private final boolean walkable;
    private Locations structure;
    private List<Enemy> enemies = new ArrayList<>();
    private Event event;
    private List<ItemDrop> drops = new ArrayList<>();
    private boolean contentGenerated = false;

    @JsonCreator
    public Tile(@JsonProperty("type") TileTypes type, @JsonProperty("description") String description, @JsonProperty("walkable") boolean walkable) {
        this.type = type;
        this.description = description;
        this.walkable = walkable;
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

    void generateContent() {
        this.structure = Locations.generateLocation(type);
        this.enemies = Enemy.generateEnemies(type);
        this.event = Event.generateEvent(type);
        try {
            generateItems();
        } catch (RandomItemNotGenerated e) {
            System.out.println(e.getMessage());
        }
        contentGenerated = true;
    }

    public void onEnter(Player player) {
        generateContent();
        listContent();
    }

    private void listContent() {
        displayLocation();
        displayItems();
        displayEnemies();
    }

    private void displayLocation() {
        System.out.println("You have entered: " + structure.getName());
        System.out.println(structure.getDescription());

    }

    private void displayItems() {
        if (!drops.isEmpty()) {
            System.out.println("You see some items here:");
            printAvailableItems();
        }
    }

    private void displayEnemies() {
        if (enemies.isEmpty()) {
            System.out.println("No enemies spotted");
            return;
        }
        for (Enemy e : enemies) {
            System.out.println("You spot in the distance " + e.getClass().getSimpleName() + e.getDescription());
        }
    }

    public void generateItems() {
        int itemCount = ThreadLocalRandom.current().nextInt(GameConstants.MAX_ITEM_DROPS + 1);
        for (int i = 0; i < itemCount; i++) {
            drops.add(generateRandomItem());
        }
    }

    public void printAvailableItems() {
        if (drops.isEmpty()) {
            System.out.println("No items found");
        }
        for (ItemDrop item : drops) {
            System.out.println("-" + item.quantity() + "x" + item.item().getName());
        }
    }

    public void removeDrop(Item drop, int quantity) {
        if (drop == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid item or quantity");
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
        //System.out.println("No such item in this zone");
    }
}
