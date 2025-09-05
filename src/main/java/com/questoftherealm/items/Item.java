package com.questoftherealm.items;

public class Item {
    private final String name;
    private final ItemType type;
    private final boolean stackable;
    private final int power; //damage for weapons or healing for potions
    private final int price;
    private final int mana;
    private final Rarity rarity;

    public Item(String name, ItemType type, boolean stackable, int power, int price, int mana, Rarity rarity) {
        this.name = name;
        this.type = type;
        this.stackable = stackable;
        this.power = power;
        this.price = price;
        this.mana = mana;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public boolean isStackable() {
        return stackable;
    }

    public int getPower() {
        return power;
    }

    public int getPrice() {
        return price;
    }

    public int getMana() {
        return mana;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public String toString() {
        return name;
    }
}
