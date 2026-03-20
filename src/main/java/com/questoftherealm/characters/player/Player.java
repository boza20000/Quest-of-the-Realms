package com.questoftherealm.characters.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.questoftherealm.characters.characterInterfaces.Explorer;
import com.questoftherealm.characters.characterInterfaces.InventoryHandler;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.expeditions.missions.Mission;
import com.questoftherealm.expeditions.quest.Quest;
import com.questoftherealm.expeditions.quest.QuestFactory;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.Position;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.ExploreManager;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.map.LocationTrigger;
import com.questoftherealm.map.Locations;
import com.questoftherealm.map.Tile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.questoftherealm.game.GameConstants.*;

public class Player implements InventoryHandler, Explorer {
    private final String name;
    private final PlayerTypes playerType;
    private final Characters playerCharacter;
    private Inventory inventory;
    private int level;
    private int experience;
    private int gold;
    private Position position;
    private int x, y;
    private String currentZone;
    private Map<ItemEffect, Item> armor;
    private Item weapon;
    @JsonIgnore
    private Quest curQuest;
    @JsonIgnore
    private Mission curMission;
    private PlayTime playTime;
    @JsonIgnore
    private long startTime;
    private QuestFactory questFactory;
    private volatile boolean isDead;
    @JsonIgnore
    private volatile boolean isActive;

    public Player(String name, PlayerTypes type, GameState state) {
        WeaponFactory weaponFactory = new WeaponFactory(state.getItemRegistry());
        this.name = name;
        this.playerType = type;
        this.playerCharacter = (PlayerFactory.createPlayer(type));
        this.inventory = new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY);
        this.level = GameConstants.START_LEVEL;
        this.gold = 0;
        this.experience = 0;
        this.currentZone = GameConstants.PLAYER_SPAWN;
        this.armor = new HashMap<>();
        armor.put(ItemEffect.HELMET, null);
        armor.put(ItemEffect.CHESTPLATE, null);
        armor.put(ItemEffect.BOOTS, null);
        this.weapon = weaponFactory.create(playerCharacter.getDefaultWeapon(state));
        this.x = PLAYER_START.x();
        this.y = PLAYER_START.y();
        this.position = PLAYER_START;
        this.playTime = new PlayTime(0, 0);
        this.startTime = 0;
        this.questFactory = new QuestFactory(this, state);
        this.curQuest = questFactory.getCurrentQuest();
        this.curMission = questFactory.getCurrentMission();
        this.isDead = false;
        this.isActive = true;
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
                  @JsonProperty("playTime") PlayTime playTime,
                  @JsonProperty("questFactory") QuestFactory questFactory,
                  @JsonProperty("dead") boolean isDead,
                  @JsonProperty("characterHealth") Integer characterHealth,
                  @JsonProperty("characterMana") Integer characterMana) {

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
        this.weapon = weapon;
        this.armor = armor != null ? armor : new HashMap<>();
        createArmor();
        initializeInventory(inventory);
        recalculateStats();

        if (characterHealth != null) {
            this.playerCharacter.setHealth(characterHealth);
        }
        if (characterMana != null) {
            this.playerCharacter.setMana(characterMana);
        }

        this.questFactory = questFactory;
        if (questFactory != null) {
            this.questFactory.setPlayer(this);
            this.curQuest = questFactory.getCurrentQuest();
            this.curMission = questFactory.getCurrentMission();
        }

        this.playTime = playTime != null ? playTime : new PlayTime(0, 0);
        this.isDead = isDead;
        this.isActive = true;
    }

    private void createArmor() {
        if (!this.armor.containsKey(ItemEffect.HELMET)) this.armor.put(ItemEffect.HELMET, null);
        if (!this.armor.containsKey(ItemEffect.CHESTPLATE)) this.armor.put(ItemEffect.CHESTPLATE, null);
        if (!this.armor.containsKey(ItemEffect.BOOTS)) this.armor.put(ItemEffect.BOOTS, null);
    }

    private void initializeInventory(Inventory inventory) {
        this.inventory = inventory != null ? inventory : new Inventory(MAX_ITEMS_IN_INVENTORY);
    }

    public synchronized void addExp(int exp) {
        experience += exp;
        while (experience >= level * MAX_EXP_PER_LEVEL) {
            experience -= level * MAX_EXP_PER_LEVEL;
            level++;
        }
    }

    public synchronized void addMoney(int amount, GameState state) {
        gold += amount;
        if (gold >= GameConstants.MAX_GOLD) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("player.reached.maxGold"));
            gold = GameConstants.MAX_GOLD;
        }
    }

    public synchronized boolean payMoney(int amount, GameState state) {
        if (gold - amount >= 0) {
            gold -= amount;
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("player.use.gold"));
            return true;
        }
        return false;
    }

    public void loseMana(int mana) {
        playerCharacter.useMana(mana);
    }

    public synchronized void move(int x, int y) {
        this.x = x;
        this.y = y;
        this.position = new Position(x, y);
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public Characters getPlayerCharacter() {
        return playerCharacter;
    }

    @JsonProperty("characterHealth")
    public int getCharacterHealth() {
        return playerCharacter.getHealth();
    }

    @JsonProperty("characterMana")
    public int getCharacterMana() {
        return playerCharacter.getMana();
    }

    public PlayerTypes getPlayerType() {
        return playerType;
    }

    public synchronized int getX() {
        return x;
    }

    public synchronized int getY() {
        return y;
    }

    public synchronized String getCurrentZone() {
        return currentZone;
    }

    public synchronized int getLevel() {
        return level;
    }

    public synchronized int getGold() {
        return gold;
    }

    public synchronized int getExperience() {
        return experience;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public synchronized void setCurrentZone(String s) {
        currentZone = s;
    }

    public synchronized Map<ItemEffect, Item> getArmor() {
        return armor;
    }

    public synchronized void setArmor(Map<ItemEffect, Item> armor) {
        this.armor = armor;
    }

    public synchronized void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    public synchronized Item getWeapon() {
        return weapon;
    }

    public synchronized Position getPosition() {
        return position;
    }

    public synchronized void setPosition(Position position) {
        this.position = position;
        this.x = position.x();
        this.y = position.y();
    }


    public void setQuestFactory(QuestFactory questFactory) {
        this.questFactory = questFactory;
    }

    public QuestFactory getQuestFactory() {
        return questFactory;
    }

    @JsonIgnore
    public Quest getCurQuest() {
        return curQuest;
    }

    @JsonIgnore
    public void setCurQuest(Quest curQuest) {
        this.curQuest = curQuest;
    }

    @JsonIgnore
    public Mission getCurMission() {
        return curMission;
    }

    @JsonIgnore
    public void setCurMission(Mission curMission) {
        this.curMission = curMission;
    }

    public PlayTime getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        PlayTime timePlayed = this.getPlayTime();
        int totalMinutes = (int) (((playTime / 1000) / 60) + timePlayed.minutes() + timePlayed.hours() * 60);
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        this.playTime = new PlayTime(hours, minutes);
    }
    @JsonIgnore
    public long getStartTime() {
        return startTime;
    }
    @JsonIgnore
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void look(GameState state) {
        Position pos = new Position(getX(), getY());
        for (LocationTrigger locTrigger : state.getTriggerRegister().getTriggers()) {
            if (locTrigger.isAtPosition(pos)) {
                locTrigger.trigger(this);
                if (locTrigger.isExecuted()) {
                    return;
                }
            }
        }
        Tile curTile = state.getMap().curZone(getX(), getY());
        if (curTile == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("player.tile.empty"));
            return;
        }
        if (!curTile.isContentGenerated()) {
            curTile.onEnter(this, state);
        } else {
            curTile.listContent(state);
        }
    }

    @Override
    public void exploreStructure(String structure, GameState state) {
        Locations location = Locations.getStructure(structure);
        MessageBundle bundle = state.getMessages().getBundle();
        Output output = state.getGameServices().getOutput();
        if (location == null) {
            output.println(bundle.get("structure.no.exist"));
            return;
        }
        output.println(bundle.get("player.approach.structure", bundle.get(location.getName())));
        output.println(bundle.get("player.decision.structure"));
        output.print(bundle.get("console.menu.prompt"));
        output.flush();
        String line = state.getGameServices().getInput().nextLine().toUpperCase();
        switch (line.toUpperCase()) {
            case "ENTER" -> {
                output.println(bundle.get("player.enter.structure"));
                ExploreManager interaction = new ExploreManager();
                interaction.exploreStructure(location, this, state);
            }
            case "LEAVE" -> output.println(bundle.get("player.default.structure"));
            default -> output.println(bundle.get("player.leave.structure"));
        }

    }

    public synchronized void equipArmorPiece(Item armorEquipment, GameState state) {
        switch (armorEquipment.getEffect()) {
            case HELMET -> swapArmor(ItemEffect.HELMET, armorEquipment, state);
            case CHESTPLATE -> swapArmor(ItemEffect.CHESTPLATE, armorEquipment, state);
            case BOOTS -> swapArmor(ItemEffect.BOOTS, armorEquipment, state);
        }
        recalculateStats();
    }

    private void swapArmor(ItemEffect slot, Item armorPiece, GameState state) {
        if (armor.get(slot) != null) {
            getInventory().addItem(armor.get(slot), 1, state);
        }
        armor.put(slot, armorPiece);
    }

    public synchronized void equipWeapon(Item weaponEquipment, GameState state) {
        if (getWeapon() != null) {
            getInventory().addItem(getWeapon(), 1, state);
        }
        setWeapon(weaponEquipment);
        recalculateStats();
    }

    public synchronized void recalculateStats() {
        int weaponAttack = (getWeapon() != null ? getWeapon().getPower() : 0);
        playerCharacter.setAttack(playerCharacter.getBaseAttack() + weaponAttack);
        if (armor == null) {
            playerCharacter.setArmor(0);
        } else {
            int armorSum = armor.values()
                    .stream()
                    .filter(Objects::nonNull)
                    .mapToInt(Item::getPower)
                    .sum();
            playerCharacter.setArmor(armorSum);
        }
    }

    public synchronized void useItem(Item item) {
        playerCharacter.useMana(item.getMana());
        switch (item.getEffect()) {
            case RESTORE_MANA ->
                    playerCharacter.setMana(Math.min(item.getPower() + playerCharacter.getMana(), MAX_MANA));
            case BUFF_STRENGTH ->
                    playerCharacter.setAttack(Math.min(playerCharacter.getAttack() + item.getPower(), MAX_ATTACK / 2));
            case RESTORE_HP ->
                    playerCharacter.setHealth(Math.min(playerCharacter.getHealth() + item.getPower(), MAX_HEALTH));
        }
    }

    public void openInventory(GameState state) {
        getInventory().listItems(state.getGameServices().getOutput(), state.getMessages().getBundle());
    }

    public void equipItem(Item item, GameState state) {
        switch (item.getType()) {
            case ARMOR -> equipArmorPiece(item, state);
            case WEAPON -> equipWeapon(item, state);
        }
    }

    public void updateQuestStatus(GameState state) {
        if (questFactory == null) return;
        Quest currentQuest = questFactory.getCurrentQuest();
        if (currentQuest == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("player.quests.completed"));
            this.curQuest = null;
            this.curMission = null;
            return;
        }
        currentQuest.updateStatus(state);
        this.curQuest = questFactory.getCurrentQuest();
        this.curMission = questFactory.getCurrentMission();
    }

    public void trackPlayTime(GameState state) {
        long endTime = state.getClock().now();
        long duration = endTime - getStartTime();
        setPlayTime(duration);
    }

    public synchronized boolean isDead() {
        return playerCharacter.isDead() || isDead;
    }

    public synchronized void setDead() {
        isDead = true;
    }

    public Tile curTile(GameState state) {
        return state.getMap().curZone(getX(), getY());
    }

    public synchronized boolean isActive() {
        return isActive;
    }

    @JsonIgnore
    public synchronized void setActive(boolean active) {
        isActive = active;
    }

    public void respawn(GameState state) {
        int roll = state.getGameServices().getRandom().randomInt(1, 4);
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("player.respawn." + roll));
        setPosition(GameConstants.PLAYER_START);
        playerCharacter.setHealth(playerCharacter.getMaxHealth());
        playerCharacter.setMana(playerCharacter.getMana() + 10);
        setCurrentZone(GameConstants.PLAYER_SPAWN);
        this.isDead = false;
    }

//    public boolean finishedGame() {
//        if(curQuest == null) return false;
//    }
}
