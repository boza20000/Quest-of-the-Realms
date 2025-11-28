package com.questoftherealm.spells;

import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;

import java.util.HashMap;
import java.util.Map;

public class SpellRegister {
    private final Map<String, Spell> spells = new HashMap<>();
    private GameState state;

    public SpellRegister(GameState state) {
        registerSpell(state.getMessages().getBundle().get("spells.register.fireball"), new Fireball(state));
        registerSpell(state.getMessages().getBundle().get("spells.register.lightning"), new LightningBolt(state));
    }

    private void registerSpell(String name, Spell spell) {
        spells.put(name, spell);
    }

    public void listSpells() {
        for (Spell s : spells.values()) {
            state.getGameServices().getOutput().println(s.getSpellName() + ": " + s.getDescription());
        }
    }

    public Spell getSpell(String name) {
        if (spells.get(name) == null) {
            throw new InvalidCommand(state.getMessages().getBundle().get("error.command.InvalidCommand"));
        }
        return spells.get(name);
    }
}
