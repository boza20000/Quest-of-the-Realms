package com.questoftherealm.characters.player;

import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;

public class Player {
    private final String name;
    private final Characters playerCharacter;
    private final Inventory inventory;
    private int level;
    private int experience;
    private int gold;
    private int x, y;
    private String currentZone;
    private Item[] armor;
    private Item weapon;

    public Player(String name, PlayerTypes playerCharacter) {
        this.name = name;
        this.playerCharacter = (PlayerFactory.createPlayer(playerCharacter));
        this.inventory = new Inventory(20);
        this.level = 1;
        this.gold = 0;
        this.experience = 0;
        this.currentZone = "Spawn";
        this.armor = new Item[3];
        this.weapon = this.playerCharacter.getDefaultWeapon();
        this.x = 2;
        this.y = 3;
    }

    public void addExp(int exp) {
        experience += exp;
        if (experience >= level * 100) {
            experience -= 100;
            level++;
        }
    }

    public void addMoney(int amount) {
        gold += amount;
        if (gold >= 100) {
            System.out.println("You have reached max gold!!!");
            gold = 100;
        }
    }

    public void equipWeapon(Item weapon) {
        inventory.addItem(this.weapon, 1);
        this.weapon = weapon;//equipping weapon
    }


    public void addArmorPiece(Item piece) {

        switch (piece.getEffect()) {
            case HELMET -> {
                if (armor[0] != null) {
                    inventory.addItem(armor[0], 1);
                }
                armor[0] = piece;
            }

            case CHESTPLATE -> {
                if (armor[1] != null) {
                    inventory.addItem(armor[1], 1);
                }
                armor[1] = piece;
            }

            case BOOTS -> {
                if (armor[2] != null) {
                    inventory.addItem(armor[2], 1);
                }
                armor[2] = piece;
            }
        }
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
        Game.getGameMap().movePlayer(this, this.x, this.y);
    }

    public String getName() {
        return name;
    }

    public Characters getPlayerCharacter() {
        return playerCharacter;
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
}
