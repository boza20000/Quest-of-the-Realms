package com.questoftherealm.interaction;

import com.questoftherealm.game.Game;
import com.questoftherealm.map.Event;
import com.questoftherealm.map.TileTypes;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class TravelManger {
    private static final Scanner scanner = new Scanner(System.in);

    private static Random random() {
        return ThreadLocalRandom.current();
    }

    private static final List<String> MOVE_CONNECTORS = List.of(
            "➡️ You leave the %s behind and make your way %s, the road stretching before you.",
            "🚶 From the %s, your journey takes you %s, with the wind brushing past as you walk.",
            "🌍 Departing the %s, you follow the worn path leading %s into the unknown.",
            "🧭 The %s fades behind you as you continue %s, footsteps steady and determined.",
            "🏞️ Moving away from the %s, the world ahead opens up as you travel %s.",
            "➡️ Quietly leaving the %s, you tread %s, your thoughts wandering with each step.",
            "🌅 Turning your back on the %s, you set off %s beneath the changing sky.",
            "🌾 The %s grows distant as you venture %s, each step taking you further from what you know."
    );

    private static void randomTravelText(String direction, TileTypes start) {
        String line = MOVE_CONNECTORS.get(random().nextInt(MOVE_CONNECTORS.size()));
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
        if (random().nextInt(100) < 30) { // 30% chance
            randomEvent(start);
        } else {
            randomTravelText(direction, start);
        }
    }

    public static String getTransition(TileTypes start, TileTypes end) {
        if (start == end) {
            return switch (end) {
                case MOUNTAIN ->
                        "⛰️ You continue winding along the rocky mountain paths, surrounded by towering cliffs and echoing winds.";
                case FOREST ->
                        "🌲 You press deeper into the forest, where the canopy thickens and shadows play across the mossy ground.";
                case SWAMP ->
                        "💧 The swamp stretches endlessly, each step sending ripples through murky water and whispering reeds.";
                case GRASS -> "🌾 The grasslands roll endlessly ahead, waves of green swaying under the open sky.";
                case VILLAGE ->
                        "🏘️ You wander through the village, passing familiar faces and hearing the soft hum of daily life.";
                case CASTLE ->
                        "🏰 You roam within the castle’s domain, its walls echoing the weight of stories and power.";
                case WATER ->
                        "💦 You remain near the water’s edge, where waves lap gently and the air smells of salt and cool mist.";
            };
        }
        return switch (end) {
            case MOUNTAIN ->
                    "⛰️ The air grows thinner and colder. The path ahead winds upward, becoming steep and treacherous.";
            case FOREST ->
                    "🌲 The trees close in around you. The canopy above blocks out most of the light, and the forest grows quiet.";
            case SWAMP ->
                    "💧 The ground grows damp and soft. The air becomes heavy with mist, and the smell of stagnant water fills your nose.";
            case GRASS ->
                    "🌾 The land opens into wide grasslands. The breeze carries the scent of wildflowers and sun-warmed earth.";
            case VILLAGE ->
                    "🏘️ You arrive at a village — laughter and chatter fill the air, and warm light spills from nearby windows.";
            case CASTLE ->
                    "🏰 The walls shrink behind you as you walk further. The world ahead feels less guarded, more uncertain.";
            case WATER ->
                    "💦 You hear the sound of waves and trickling streams. The ground softens, and the air carries a cool, refreshing scent.";
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
        String template = SPOTTING_CONNECTORS.get(random().nextInt(SPOTTING_CONNECTORS.size()));
        return template.formatted(locationName);
    }
}
