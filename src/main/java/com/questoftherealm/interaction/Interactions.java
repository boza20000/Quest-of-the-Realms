package com.questoftherealm.interaction;

import com.questoftherealm.game.Game;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.map.Event;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Interactions {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public static void worldStart() {
        String start = """
                You enter the kings castle and you have to talk to the castle's
                Elder so you will be better prepared for the journey that awaits you.
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

    private static final List<String> MOVE_CONNECTORS = List.of(
            "➡️ You depart from the %s and begin walking %s.",
            "🚶 Leaving the %s behind, you head %s.",
            "🌍 From the %s, your journey continues %s.",
            "🧭 You leave the %s, setting off %s.",
            "🏞️ With the %s behind you, the road ahead leads %s.",
            "🪶 Slowly leaving the %s, you turn %s."
    );

    private static void randomTravelText(String direction, TileTypes start) {
        String line = MOVE_CONNECTORS.get(random.nextInt(MOVE_CONNECTORS.size()));
        SlowPrinter.slowPrint(line.formatted(start, direction));
    }

    private static void randomEvent(TileTypes type) {
        Event event = Event.generateEvent(type);
        SlowPrinter.slowPrint("⚠️ While traveling, you stumble upon: " + event.getName());
        SlowPrinter.slowPrint(event.getDescription());
        boolean investigate = promptYesNo("Do you want to investigate?");
        if (investigate) {
            SlowPrinter.slowPrint("👉 You decide to face it head-on!");
            event.getNpc().interact(Game.getPlayer());
            // TODO: Trigger combat, trade, or special logic based on event
        } else {
            SlowPrinter.slowPrint("➡️ You ignore it and continue your journey...");
        }
    }

    public static boolean promptYesNo(String question) {
        SlowPrinter.slowPrint(question + " (yes/no)");
        while (true) {
            System.out.print(">");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) return true;
            if (input.equals("no") || input.equals("n")) return false;
            System.out.println("Please type yes or no.");
        }
    }

    public static void pathInteraction(TileTypes start, String direction) {
        int roll = random.nextInt(100);
        if (roll < 30) { // 30% chance
            randomEvent(start);
        } else {
            randomTravelText(direction, start);
        }
    }

    public static String getTransition(TileTypes start, TileTypes end) {
        String from = start.toString().toLowerCase();
        return switch (end) {
            case MOUNTAIN ->
                    "⛰️ Leaving the %s behind, the air grows thinner and colder. The path ahead winds upward, becoming steep and treacherous."
                            .formatted(from);
            case FOREST ->
                    "🌲 Moving away from the %s, the trees close in around you. The canopy above blocks out most of the light, and the forest grows quiet."
                            .formatted(from);
            case SWAMP ->
                    "💧 As you depart the %s, the ground grows damp and soft. The air becomes heavy with mist, and the smell of stagnant water fills your nose."
                            .formatted(from);
            case GRASS ->
                    "🌾 Leaving the %s, the land opens into wide grasslands. The breeze carries the scent of wildflowers and sun-warmed earth."
                            .formatted(from);
            case VILLAGE ->
                    "🏘️ Stepping away from %s, the chatter of villagers fades. You find yourself on quieter paths leading to the unknown."
                            .formatted(from);
            case CASTLE ->
                    "🏰 The walls of the %s shrink behind you as you walk further. The world ahead feels less guarded, more uncertain."
                            .formatted(from);
            case WATER ->
                    "💦 After leaving the %s, you hear the sound of waves and trickling streams. The ground softens, and the air carries a cool, refreshing scent."
                            .formatted(from);
        };
    }

    private static final List<String> SPOTTING_CONNECTORS = List.of(
            "👀 In the distance, you spot %s.",
            "🏕️ As you travel, you come across %s.",
            "🌄 On the horizon, you see %s.",
            "🔎 Your eyes catch sight of %s nearby.",
            "🌲 Between the trees, %s comes into view.",
            "⚔️ You notice %s standing ahead of you.",
            "✨ Unexpectedly, you stumble upon %s.",
            "🌍 As the path bends, you discover %s.",
            "🔥 Smoke or movement draws your attention to %s.",
            "🏚️ Hidden among the landscape, you find %s."
    );

    public static String getRandomSpotting(String locationName) {
        String template = SPOTTING_CONNECTORS.get(random.nextInt(SPOTTING_CONNECTORS.size()));
        return template.formatted(locationName);
    }

}
