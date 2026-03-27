package com.questoftherealm.items;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Item(
    @JsonProperty("name") String name,
    @JsonProperty("type") ItemType type,
    @JsonProperty("stackable") boolean stackable,
    @JsonProperty("power") int power,
    @JsonProperty("cost") int price,
    @JsonProperty("manaCost") int mana,
    @JsonProperty("rarity") Rarity rarity,
    @JsonProperty("effect") ItemEffect effect
) {

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
