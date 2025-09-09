package com.questoftherealm.items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private final String name;
    private final ItemType type;
    private final boolean stackable;
    private final int power; //damage for weapons or healing for potions
    private final int price;
    private final int mana;
    private final Rarity rarity;
    private final ItemEffect effect;

    public Item( @JsonProperty("name") String name,
                 @JsonProperty("type") ItemType type,
                 @JsonProperty("stackable") boolean stackable,
                 @JsonProperty("power") int power,
                 @JsonProperty("cost") int price,
                 @JsonProperty("manaCost") int mana,
                 @JsonProperty("rarity") Rarity rarity,
                 @JsonProperty("effect") ItemEffect effect) {
        this.name = name;
        this.type = type;
        this.stackable = stackable;
        this.power = power;
        this.price = price;
        this.mana = mana;
        this.rarity = rarity;
        this.effect = effect;
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

    public ItemEffect getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        return name;
    }

}
