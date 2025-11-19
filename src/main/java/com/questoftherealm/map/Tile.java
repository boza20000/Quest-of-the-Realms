package com.questoftherealm.map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.characters.player.Player;
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
import com.questoftherealm.localization.MessageBundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Tile {
    private final TileTypes type;
    private final String description;
    private final boolean walkable;
    private Locations structure;
    private List<Enemy> enemies = new ArrayList<>();
    private List<ItemDrop> drops = new ArrayList<>();
    private boolean contentGenerated = false;
    private final Map<String, Npc> npcRegister = new HashMap<>();
    private TravelManger travelManger ;
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
            this.enemies = Enemy.generateEnemies(type);
            this.drops.clear();
            generateItems(state);
        } catch (RandomItemNotGenerated e) {
            output.println(MessageBundle.get("tile.error.items"));
        } catch (StructureNotGenerated e) {
            output.println(MessageBundle.get("tile.error.structure"));
        } catch (Exception e) {
            output.println(MessageBundle.get("tile.error.general"));
        }
        contentGenerated = true;
    }

    public void onEnter(Player player,GameState state) {
        this.output = state.getGameServices().getOutput();
        generateContent(state);
        listContent();
    }

    private void listContent() {
        displayLocation();
        displayItems();
        displayEnemies();
        displayNpc();
    }

    private void displayNpc() {
        if(!this.npcRegister.isEmpty()){
            output.println("You see: ");
            for(Npc n : npcRegister.values()){
                output.println("-" + n.getType());
            }
        }
    }

    private void displayLocation() {
        if (structure == null) return;
        output.println();
        output.println(travelManger.getRandomSpotting(structure.getName()) + structure.getName());
        output.print(structure.getDescription());
    }

    private void displayItems() {
        if (!drops.isEmpty()) {
            output.println(MessageBundle.get("tile.items.found"));
            printAvailableItems();
        }
    }

    private void displayEnemies() {
        if (enemies.isEmpty()) {
            output.println(MessageBundle.get("tile.enemies.none"));
            return;
        }
        for (Enemy e : enemies) {
            output.println(MessageBundle.get("tile.enemies.spotted", e.getClass().getSimpleName(), e.getDescription()));
        }
    }

    public void generateItems(GameState state) {
        int itemCount = ThreadLocalRandom.current().nextInt(GameConstants.MAX_ITEM_DROPS + 1);
        Chest chest = new Chest(state);
        for (int i = 0; i < itemCount; i++) {
            drops.add(chest.generateRandomItem());
        }
    }

    public void printAvailableItems() {
        if (drops.isEmpty()) {
            output.println(MessageBundle.get("tile.items.none"));
        }
        for (ItemDrop item : drops) {
            output.println("-" + item.quantity() + "x " + item.item().getName());
        }
    }

    public void removeDrop(Item drop, int quantity) {
        if (drop == null || quantity <= 0) {
            throw new IllegalArgumentException(MessageBundle.get("tile.items.invalid"));
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
}
