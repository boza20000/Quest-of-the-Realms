package com.questoftherealm.characters.player;

import com.questoftherealm.characters.Characters;

public class Player {
    private final String name;
    private final Characters playerCharacter;
    private final Inventory inventory;

    private int level;
    private int experience;
    private int gold;

    private int x, y;
    private String currentZone;

    public Player(String name, PlayerTypes playerCharacter) {
        this.name = name;
        this.playerCharacter = (PlayerFactory.createPlayer(playerCharacter));
        this.inventory = new Inventory(20);
        this.level = 1;
        this.gold = 0;
        this.experience = 0;
        this.currentZone = "Castle of the Avions";
    }

    public String getName() {
        return name;
    }

    public Characters getPlayerCharacter() {
        return playerCharacter;
    }

    public void addExp(int exp) {
        experience += exp;
        if (experience >= level * 100) {
            level++;
            experience -= 100;
            System.out.println("Congratulations you just leveled up to level " + level + "!");
        }
    }

    public void addMoney(int amount) {
        gold += amount;
        if(gold>=100){
            System.out.println("You have reached max gold!!!");
            gold = 100;
        }
    }

    public void move(int x,int y){
         playerCharacter.move(x,y);
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
}
