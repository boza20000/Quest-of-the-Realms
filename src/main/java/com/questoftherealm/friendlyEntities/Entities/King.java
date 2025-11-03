package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.interaction.SlowPrinter;

public class King extends Npc {
    public static boolean hasTalkedToTheKing = false;

    public King() {
        super(NpcType.King);
    }

    @Override
    public void talk(Player player,boolean isSimulation) {
        if(isSimulation){
            hasTalkedToTheKing = true;
            return;
        }
        SlowPrinter.slowPrint("""
                🏰 You arrive at the Castle.
                The guards at the gates barely recognise you — dirt-streaked, armor dented, eyes weary from the northern wilds.
                They let you in.
                👑 Without hesitation, you rush through the great halls toward the King's chamber...
                
                The air inside is tense; whispers of war already hang like a storm about to break.
                """);

        SlowPrinter.slowPrint("""
                🧝‍♂️ You kneel, still covered in dust and scars from the journey.
                "My King... the rumors were true.
                 The villages... they weren’t raided by men or beasts — but by *goblins*."
                """);

        SlowPrinter.slowPrint("""
                👑 King: "Goblins? Impossible. They were scattered long ago during the First Wars."
                """);

        SlowPrinter.slowPrint("""
                🧝‍♂️ "I saw them with my own eyes, Your Majesty.
                 Small and green — some larger and armored. But they are many. Too many.
                 They have built a camp deep in the northern forests... and they are marching south."
                """);

        SlowPrinter.slowPrint("""
                👑 King (leaning forward): "Marching south? Toward *us*?"
                """);

        SlowPrinter.slowPrint("""
                🧝‍♂️ "Yes, my King. Their numbers are vast — an army beyond anything we’ve faced in generations.
                 Two villages have already been reduced to ashes. If we do not act now...
                 the realm of men will fall before the first snow."
                """);

        SlowPrinter.slowPrint("""
                👑 King (grimly): "Then war it shall be."
                He rises slowly, voice echoing through the hall.
                "Sound the horns. Summon the generals.
                 The armies of Eldoria will march north by dawn.
                 We will not wait for the shadows to reach our gates!"
                """);

        SlowPrinter.slowPrint("""
                The chamber erupts in shouts and movement — messengers rush off, the war banners are unfurled.
                You stand before your King, weary but resolute.
                
                👑 King: "You have done well, brave one. Rest for now.
                 Soon, all of Eldoria will owe its survival to your courage."
                
                ⚔️ The drums of war begin to thunder across the realm...
                """);
        hasTalkedToTheKing = true;
    }

    @Override
    public boolean isHasTalked() {
        return false;
    }
}
