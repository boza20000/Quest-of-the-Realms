package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.spells.Spell;

public interface SpellCaster {
    void castSpell(Spell spell, Characters target);
}
