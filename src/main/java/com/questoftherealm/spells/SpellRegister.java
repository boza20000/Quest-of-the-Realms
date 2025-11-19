package com.questoftherealm.spells;

import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.MessageBundle;

import java.util.HashMap;
import java.util.Map;

public class SpellRegister {
    private final Map<String, Spell> spells = new HashMap<>();
    private Output output;

    public SpellRegister(Output output) {
        this.output = output;
        registerSpell(MessageBundle.get("spells.register.fireball"), new Fireball());
        registerSpell(MessageBundle.get("spells.register.lightning"), new LightningBolt());
    }

    private void registerSpell(String name, Spell spell) {
        spells.put(name, spell);
    }

    public void listSpells() {
        for (Spell s : spells.values()) {
            output.println(s.getSpellName() + ": " + s.getDescription());
        }
    }

    public Spell getSpell(String name) {
        if (spells.get(name) == null) {
            throw new InvalidCommand(MessageBundle.get("error.command.InvalidCommand"));
        }
        return spells.get(name);
    }
}
