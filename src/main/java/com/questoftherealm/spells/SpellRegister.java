package com.questoftherealm.spells;

import com.questoftherealm.exceptions.InvalidCommand;

import java.util.HashMap;
import java.util.Map;

public class SpellRegister {
    private final Map<String, Spell> spells = new HashMap<>();

    public SpellRegister() {
        registerSpell("fireball", new Fireball());
        registerSpell("lightning", new LightningBolt());
    }

    private void registerSpell(String name, Spell spell) {
        spells.put(name, spell);
    }

    public void listSpells() {
        for (Spell s : spells.values()) {
            System.out.println(s.getSpellName() + ": " + s.getDescription());
        }
    }
    public Spell getSpell(String name) {
        if (spells.get(name) == null) {
            throw new InvalidCommand("Command not recognised");
        }
        return spells.get(name);
    }
}
