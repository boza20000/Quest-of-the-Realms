package com.questoftherealm.characters.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.questoftherealm.characters.characterInterfaces.Explorer;
import com.questoftherealm.characters.characterInterfaces.InventoryHandler;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.exceptions.RandomItemNotGenerated;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.missions.Meet_the_Elder;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;
import com.questoftherealm.interaction.SlowPrinter;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.map.LocationTrigger;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TriggerRegister;

import java.util.HashMap;
import java.util.Objects;

import static com.questoftherealm.game.GameConstants.*;
import static com.questoftherealm.items.Chest.generateRandomItem;

public class Player implements InventoryHandler, Explorer {
    private final String name;
    private final PlayerTypes playerType;
    private final Characters playerCharacter;
    private final Inventory inventory;
    private int level;
    private int experience;
    private int gold;
    private Position position;
    private int x, y;
    private String currentZone;
    private HashMap<ItemEffect, Item> armor;
    private Item weapon;
    private Quest curQuest;
    private Mission curMission;

    public Player(String name, PlayerTypes type) {
        this.name = name;
        this.playerType = type;
        this.playerCharacter = (PlayerFactory.createPlayer(type));
        this.inventory = new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY);
        this.level = 1;
        this.gold = 0;
        this.experience = 0;
        this.currentZone = "Spawn";
        this.armor = new HashMap<>();
        armor.put(ItemEffect.HELMET, null);
        armor.put(ItemEffect.CHESTPLATE, null);
        armor.put(ItemEffect.BOOTS, null);
        this.weapon = this.playerCharacter.getDefaultWeapon();
        this.x = PLAYER_START.x();
        this.y = PLAYER_START.y();
        this.position = PLAYER_START;
        this.curQuest = new StartQuest();
        this.curMission = new Meet_the_Elder();
    }

    @JsonCreator
    public Player(@JsonProperty("name") String name,
                  @JsonProperty("playerType") PlayerTypes playerType,
                  @JsonProperty("level") int level,
                  @JsonProperty("experience") int experience,
                  @JsonProperty("gold") int gold,
                  @JsonProperty("x") int x,
                  @JsonProperty("y") int y,
                  @JsonProperty("currentZone") String currentZone,
                  @JsonProperty("armor") HashMap<ItemEffect, Item> armor,
                  @JsonProperty("weapon") Item weapon,
                  @JsonProperty("inventory") Inventory inventory,
                  @JsonProperty("quest") Quest quest,
                  @JsonProperty("mission") Mission mission) {
        this.name = name;
        this.playerType = playerType;
        this.playerCharacter = PlayerFactory.createPlayer(playerType);
        this.level = level;
        this.experience = experience;
        this.gold = gold;
        this.position = new Position(x, y);
        this.x = x;
        this.y = y;
        this.currentZone = currentZone;
        this.weapon = weapon != null ? weapon : playerCharacter.getDefaultWeapon();
        this.armor = armor != null ? armor : new HashMap<>();
        if (!this.armor.containsKey(ItemEffect.HELMET)) this.armor.put(ItemEffect.HELMET, null);
        if (!this.armor.containsKey(ItemEffect.CHESTPLATE)) this.armor.put(ItemEffect.CHESTPLATE, null);
        if (!this.armor.containsKey(ItemEffect.BOOTS)) this.armor.put(ItemEffect.BOOTS, null);
        this.inventory = inventory != null ? inventory : new Inventory(20);
        recalculateStats();
        this.curQuest = quest;
        this.curMission = mission;
    }

    public void addExp(int exp) {
        experience += exp;
        while (experience >= level * MAX_EXP_PER_LEVEL) {
            experience -= level * MAX_EXP_PER_LEVEL;
            level++;
        }
    }

    public void addMoney(int amount) {
        gold += amount;
        if (gold >= GameConstants.MAX_GOLD) {
            System.out.println("You have reached max gold!!!");
            gold = GameConstants.MAX_GOLD;
        }
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
        this.position = new Position(x, y);
        Game.getGameMap().movePlayer(this, this.x, this.y);
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public Characters getPlayerCharacter() {
        return playerCharacter;
    }

    public PlayerTypes getPlayerType() {
        return playerType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getCurrentZone() {
        return currentZone;
    }

    public int getLevel() {
        return level;
    }

    public int getGold() {
        return gold;
    }

    public int getExperience() {
        return experience;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setCurrentZone(String s) {
        currentZone = s;
    }

    public HashMap<ItemEffect, Item> getArmor() {
        return armor;
    }

    public void setArmor(HashMap<ItemEffect, Item> armor) {
        this.armor = armor;
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    public Item getWeapon() {
        return weapon;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Quest getCurQuest() {
        return curQuest;
    }

    public void setCurQuest(Quest curQuest) {
        this.curQuest = curQuest;
    }

    public Mission getCurMission() {
        return curMission;
    }

    public void setCurMission(Mission curMission) {
        this.curMission = curMission;
    }

    @Override
    public void look() {
        Position pos = new Position(getX(), getY());
        for (LocationTrigger locTrigger : TriggerRegister.triggers) {
            if (locTrigger.isAtPosition(pos)) {
                locTrigger.trigger(this);
                return;
            }
        }

        Tile curTile = Game.getGameMap().curZone(getX(), getY());
        if (!curTile.isContentGenerated()) {
            curTile.onEnter(this);
        } else {
            System.out.println("There seems to be nothing else...");
        }

    }

    @Override
    public void exploreStructure(String structure) {
//explore structure
    }

    @Override
    public void openChest() {
        try {
            ItemDrop drop = generateRandomItem();
            System.out.println("Chest opened");
            System.out.println("Random item drop: " + drop.item() + "x" + drop.quantity());
            this.inventory.addItem(drop.item(), drop.quantity());
        } catch (RandomItemNotGenerated e) {
            System.out.println(e.getMessage());
        }
    }


    public void equipArmorPiece(Item armorEquipment) {
        switch (armorEquipment.getEffect()) {
            case HELMET -> swapArmor(ItemEffect.HELMET, armorEquipment);
            case CHESTPLATE -> swapArmor(ItemEffect.CHESTPLATE, armorEquipment);
            case BOOTS -> swapArmor(ItemEffect.BOOTS, armorEquipment);
        }
        recalculateStats();
    }

    private void swapArmor(ItemEffect slot, Item armorPiece) {
        if (armor.get(slot) != null) {
            getInventory().addItem(armor.get(slot), 1);
        }
        armor.put(slot, armorPiece);
    }


    public void equipWeapon(Item weaponEquipment) {
        if (getWeapon() != null) {
            getInventory().addItem(getWeapon(), 1);
        }
        setWeapon(weaponEquipment);
        recalculateStats();
    }

    private void recalculateStats() {
        int weaponAttack = (getWeapon() != null ? getWeapon().getPower() : 0);
        playerCharacter.setAttack(playerCharacter.getBaseAttack() + weaponAttack);
        int armorSum = armor.values()
                .stream()
                .filter(Objects::nonNull)
                .mapToInt(Item::getPower)
                .sum();
        playerCharacter.setArmor(armorSum);
    }

    //use consumables
    public void useItem(Item item) {
        // playerCharacter.useMana(item);
        switch (item.getEffect()) {
            case RESTORE_MANA ->
                    playerCharacter.setMana(Math.min(item.getPower() + playerCharacter.getMana(), MAX_MANA));
            case BUFF_STRENGTH ->
                    playerCharacter.setAttack(Math.min(playerCharacter.getAttack() + item.getPower(), MAX_ATTACK));
//                case SPELL_FIRE -> curCharacter.castSpell("fireball", item.getPower());
//                case SPELL_ICE -> curCharacter.castSpell("iceSpike", item.getPower());
//                case SPELL_HEAL -> curCharacter.castSpell("heal", item.getPower());
//                case SPELL_SHIELD -> curCharacter.castSpell("shield", item.getPower());
//                case SPELL_LIGHTNING -> curCharacter.castSpell("lightning", item.getPower());
            case RESTORE_HP ->
                    playerCharacter.setHealth(Math.min(playerCharacter.getHealth() + item.getPower(), MAX_HEALTH));
//                case BUFF_CHARISMA -> curCharacter.addBuff("charisma", item.getPower());
//              case BUFF_INTELLIGENCE -> curCharacter.addBuff("intelligence", item.getPower());
//                case QUEST_ITEM -> Game.getQuestManager().collectItem(item);
//                case FRAGMENT -> Game.getFragmentManager().collectFragment(item);
        }
    }

    public void openInventory() {
        getInventory().listItems();
    }

    public void equipItem(Item item) {
        switch (item.getType()) {
            case ARMOR -> {
                equipArmorPiece(item);
            }
            case WEAPON -> {
                equipWeapon(item);
            }
        }
    }

    public void updateQuestStatus() {
        Quest currentQuest = QuestFactory.getCurrentQuest();
        if (currentQuest == null) {
            System.out.println("🏁 All quests completed!");
            this.curQuest = null;
            this.curMission = null;
            return;
        }
        currentQuest.updateStatus();
        this.curQuest = QuestFactory.getCurrentQuest();
        this.curMission = QuestFactory.getCurrentMission();
    }
}
