package com.questoftherealm.characters;

abstract class Characters {
    private int health;//0-50
    private int mana;//0-30
    private int attack;//0-10
    private int defence;//0-10
    private int armor;//0-30
    private int charisma;//0-10
    private int spells;//0-10
    private int intelligence;//0-10

    public Characters(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        setHealth(health);
        setMana(mana);
        setAttack(attack);
        setDefence(defence);
        setArmor(armor);
        setCharisma(charisma);
        setIntelligence(intelligence);
        setSpells(spells);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health > 0 && health <= 50) {
            this.health = health;
        }
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        if (health > 0 && health <= 10) {
            this.intelligence = intelligence;
        }
    }

    public int getSpells() {
        return spells;
    }

    public void setSpells(int spells) {
        if (health > 0 && health <= 10) {
            this.spells = spells;
        }
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        if (health > 0 && health <= 10) {
            this.charisma = charisma;
        }
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        if (health > 0 && health <= 30) {
            this.armor = armor;
        }
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        if (health > 0 && health <= 10) {
            this.defence = defence;
        }
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        if (health > 0 && health <= 30) {
            this.mana = mana;
        }
        this.mana = mana;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        if (health > 0 && health <= 10) {
            this.attack = attack;
        }
    }
}
