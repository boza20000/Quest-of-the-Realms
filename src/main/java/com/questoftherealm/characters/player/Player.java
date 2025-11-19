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
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.Position;
import com.questoftherealm.interaction.ExploreManager;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.map.LocationTrigger;
import com.questoftherealm.map.Locations;
import com.questoftherealm.map.Tile;

import java.util.HashMap;
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
    private HashMap<ItemEffect, Item> armor;
    private Item weapon;
    @JsonIgnore
    private Quest curQuest;
    @JsonIgnore
    private Mission curMission;
    private PlayTime playTime;
    private long startTime;
    private QuestFactory questFactory;
    private boolean isDead;

    public Player(String name, PlayerTypes type) {
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
        this.weapon = this.playerCharacter.getDefaultWeapon();
        this.x = PLAYER_START.x();
        this.y = PLAYER_START.y();
        this.position = PLAYER_START;
        this.playTime = new PlayTime(0, 0);
        this.startTime = 0;
        this.questFactory = new QuestFactory(this);
        this.curQuest = questFactory.getCurrentQuest();
        this.curMission = questFactory.getCurrentMission();
        this.isDead = false;
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
                  @JsonProperty("dead") boolean isDead) {

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
        createArmor();
        initializeInventory(inventory);
        recalculateStats();

        this.questFactory = questFactory;
        if (questFactory != null) {
            this.questFactory.setPlayer(this);
            this.curQuest = questFactory.getCurrentQuest();
            this.curMission = questFactory.getCurrentMission();
        }

        this.playTime = playTime != null ? playTime : new PlayTime(0, 0);
        this.isDead = isDead;
    }

    private void createArmor() {
        if (!this.armor.containsKey(ItemEffect.HELMET)) this.armor.put(ItemEffect.HELMET, null);
        if (!this.armor.containsKey(ItemEffect.CHESTPLATE)) this.armor.put(ItemEffect.CHESTPLATE, null);
        if (!this.armor.containsKey(ItemEffect.BOOTS)) this.armor.put(ItemEffect.BOOTS, null);

    }

    private void initializeInventory(Inventory inventory) {
        this.inventory = inventory != null ? inventory : new Inventory(MAX_ITEMS_IN_INVENTORY);
    }

    public void addExp(int exp) {
        experience += exp;
        while (experience >= level * MAX_EXP_PER_LEVEL) {
            experience -= level * MAX_EXP_PER_LEVEL;
            level++;
        }
    }

    public void addMoney(int amount, GameState state) {
        gold += amount;
        if (gold >= GameConstants.MAX_GOLD) {
            state.getGameServices().getOutput().println(MessageBundle.get("player.reached.maxGold"));
            gold = GameConstants.MAX_GOLD;
        }
    }

    public boolean payMoney(int amount, GameState state) {
        if (gold - amount >= 0) {
            gold -= amount;
            state.getGameServices().getOutput().println(MessageBundle.get("player.use.gold"));
            return true;
        }
        return false;
    }

    public void loseMana(int mana) {
        playerCharacter.useMana(mana);
    }

    public void move(int x, int y) {
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void look(GameState state) {
        Position pos = new Position(getX(), getY());
        for (LocationTrigger locTrigger : state.getTriggerRegister().getTriggers()) {
            if (locTrigger.isAtPosition(pos)) {
                locTrigger.trigger(this);
                return;
            }
        }
        Tile curTile = state.getMap().curZone(getX(), getY());
        if (curTile == null) {
            state.getGameServices().getOutput().println("you current location is empty");
            return;
        }
        if (!curTile.isContentGenerated() || curTile.isEmpty()) {
            curTile.onEnter(this, state);
        } else {
            state.getGameServices().getOutput().println(MessageBundle.get("player.tile.empty"));
        }
    }

    @Override
    public void exploreStructure(String structure, GameState state) {
        Locations location = Locations.getStructure(structure);
        if (location == null) {
            state.getGameServices().getOutput().println(MessageBundle.get("structure.no.exist"));
            return;
        }
        state.getGameServices().getOutput().println(MessageBundle.get("player.approach.structure", location.getName()));
        state.getGameServices().getOutput().println(location.getDescription());
        state.getGameServices().getOutput().println(MessageBundle.get("player.decision.structure"));
        state.getGameServices().getOutput().print(MessageBundle.get("console.enter.command.symbol"));
        String line = state.getGameServices().getInput().nextLine();
        switch (line.toUpperCase()) {
            case "ENTER" -> {
                state.getGameServices().getOutput().println(MessageBundle.get("player.enter.structure"));
                ExploreManager interaction = new ExploreManager();
                interaction.exploreStructure(location, this, state);
            }
            case "LEAVE" -> {
                state.getGameServices().getOutput().println(MessageBundle.get("player.default.structure"));
            }
            default -> {
                state.getGameServices().getOutput().println(MessageBundle.get("player.leave.structure"));
            }
        }

    }

    @Override
    public void openChest(GameState state) {
        try {
            Chest chest = new Chest(state);
            ItemDrop drop = chest.generateRandomItem();
            state.getGameServices().getOutput().println(MessageBundle.get("player.open.chest"));
            state.getGameServices().getOutput().println(MessageBundle.get("player.random.item", drop.item(), drop.quantity()));
            this.inventory.addItem(drop.item(), drop.quantity(), state);
        } catch (RandomItemNotGenerated e) {
            state.getGameServices().getOutput().println(e.getMessage());
        }
    }


    public void equipArmorPiece(Item armorEquipment, GameState state) {
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


    public void equipWeapon(Item weaponEquipment, GameState state) {
        if (getWeapon() != null) {
            getInventory().addItem(getWeapon(), 1, state);
        }
        setWeapon(weaponEquipment);
        recalculateStats();
    }

    public void recalculateStats() {
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

    public void openInventory(GameState state) {
        getInventory().listItems(state);
    }

    public void equipItem(Item item, GameState state) {
        switch (item.getType()) {
            case ARMOR -> {
                equipArmorPiece(item, state);
            }
            case WEAPON -> {
                equipWeapon(item, state);
            }
        }
    }

    public void updateQuestStatus(GameState state) {
        if (questFactory == null) return;
        Quest currentQuest = questFactory.getCurrentQuest();
        if (currentQuest == null) {
            state.getGameServices().getOutput().println(MessageBundle.get("player.quests.completed"));
            this.curQuest = null;
            this.curMission = null;
            return;
        }
        currentQuest.updateStatus(state);
        this.curQuest = questFactory.getCurrentQuest();
        this.curMission = questFactory.getCurrentMission();
    }

    public void trackPlayTime() {
        long endTime = System.currentTimeMillis();
        long duration = endTime - getStartTime();
        setPlayTime(duration);
    }

    public boolean isDead() {
        return playerCharacter.isDead();
    }

    public void setDead() {
        isDead = isDead();
    }
}
