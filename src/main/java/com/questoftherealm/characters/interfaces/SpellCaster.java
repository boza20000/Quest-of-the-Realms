package com.questoftherealm.characters.interfaces;

import com.questoftherealm.spells.Spell;

import javax.xml.stream.events.Characters;

public interface SpellCaster {
    void castSpell(Spell spell, Characters target);
}
