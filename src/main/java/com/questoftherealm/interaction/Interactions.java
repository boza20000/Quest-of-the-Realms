package com.questoftherealm.interaction;

import com.questoftherealm.items.ItemDrop;

public class Interactions {


    public static void worldStart() {
        String start = """
                You find yourself in the kings castle
                and you have been told to talk to the castle's
                Elder so you will be better prepared for
                the journey that awaits you.
                """;
        SlowPrinter.slowPrint(start);
    }

    public static void elderDialogue(String name, ItemDrop weapon, ItemDrop helmet, ItemDrop chestplate, ItemDrop boots) {
        String dialogue = """
                The eldar %s is one of the council's wisest people.
                He is urging you to prepare well for the expedition.
                He will give you some items to help you on your journey.
                """.formatted(name);
        SlowPrinter.slowPrint(dialogue);
        SlowPrinter.slowPrint("""
                A %s to protect yourself from what you meet in the north and on the way there.
                This weapon should provide the needed extra protection to reach the objective.
                You have received: %s
                """.formatted(weapon.item().getEffect(), weapon.item().getName()));
        SlowPrinter.slowPrint("""
                The king also gathered his best blacksmiths to make strong armor for you.
                You have received: %s, %s, %s
                """.formatted(helmet.item().getName(), chestplate.item().getName(), boots.item().getName()));
    }

    public static void traderDialogue() {

    }

}
