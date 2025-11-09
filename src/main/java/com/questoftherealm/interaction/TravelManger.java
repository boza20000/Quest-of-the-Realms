package com.questoftherealm.interaction;

import com.questoftherealm.game.Game;
import com.questoftherealm.localization.MessageBundle;
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
            "➡️ " + MessageBundle.get("travel.move.connector1"),
            "🚶 " + MessageBundle.get("travel.move.connector2"),
            "🌍 " + MessageBundle.get("travel.move.connector3"),
            "🧭 " + MessageBundle.get("travel.move.connector4"),
            "🏞️ " + MessageBundle.get("travel.move.connector5"),
            "➡️ " + MessageBundle.get("travel.move.connector6"),
            "🌅 " + MessageBundle.get("travel.move.connector7"),
            "🌾 " + MessageBundle.get("travel.move.connector8")
    );

    private static void randomTravelText(String direction, TileTypes start) {
        String line = MOVE_CONNECTORS.get(random().nextInt(MOVE_CONNECTORS.size()));
        SlowPrinter.slowPrint(String.format(line, start, direction));
    }

    private static void randomEvent(TileTypes type) {
        Event event = Event.generateEvent(type);
        SlowPrinter.slowPrint("⚠️ " + MessageBundle.get("travel.event.encounter", event.getName()));
        SlowPrinter.slowPrint(event.getDescription());
        boolean investigate = promptYesNo(MessageBundle.get("travel.event.investigate.question"));
        if (investigate) {
            SlowPrinter.slowPrint("👉 " + MessageBundle.get("travel.event.investigate.accept"));
            event.getNpc().interact(Game.getPlayer());
        } else {
            SlowPrinter.slowPrint("➡️ " + MessageBundle.get("travel.event.investigate.decline"));
        }
    }

    public static boolean promptYesNo(String question) {
        SlowPrinter.slowPrint(question + " " + MessageBundle.get("travel.prompt.yesno"));
        while (true) {
            System.out.print(">");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) return true;
            if (input.equals("no") || input.equals("n")) return false;
            System.out.println(MessageBundle.get("travel.prompt.invalid"));
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
                case MOUNTAIN -> "⛰️ " + MessageBundle.get("travel.transition.same.mountain");
                case FOREST -> "🌲 " + MessageBundle.get("travel.transition.same.forest");
                case SWAMP -> "💧 " + MessageBundle.get("travel.transition.same.swamp");
                case GRASS -> "🌾 " + MessageBundle.get("travel.transition.same.grass");
                case VILLAGE -> "🏘️ " + MessageBundle.get("travel.transition.same.village");
                case CASTLE -> "🏰 " + MessageBundle.get("travel.transition.same.castle");
                case WATER -> "💦 " + MessageBundle.get("travel.transition.same.water");
            };
        }
        return switch (end) {
            case MOUNTAIN -> "⛰️ " + MessageBundle.get("travel.transition.to.mountain");
            case FOREST -> "🌲 " + MessageBundle.get("travel.transition.to.forest");
            case SWAMP -> "💧 " + MessageBundle.get("travel.transition.to.swamp");
            case GRASS -> "🌾 " + MessageBundle.get("travel.transition.to.grass");
            case VILLAGE -> "🏘️ " + MessageBundle.get("travel.transition.to.village");
            case CASTLE -> "🏰 " + MessageBundle.get("travel.transition.to.castle");
            case WATER -> "💦 " + MessageBundle.get("travel.transition.to.water");
        };
    }

    private static final List<String> SPOTTING_CONNECTORS = List.of(
            "👀 " + MessageBundle.get("travel.spot.connector1"),
            "🏕️ " + MessageBundle.get("travel.spot.connector2"),
            "🌄 " + MessageBundle.get("travel.spot.connector3"),
            "🔎 " + MessageBundle.get("travel.spot.connector4"),
            "🌲 " + MessageBundle.get("travel.spot.connector5"),
            "⚔️ " + MessageBundle.get("travel.spot.connector6"),
            "✨ " + MessageBundle.get("travel.spot.connector7"),
            "🌍 " + MessageBundle.get("travel.spot.connector8"),
            "🔥 " + MessageBundle.get("travel.spot.connector9"),
            "🏚️ " + MessageBundle.get("travel.spot.connector10")
    );

    public static String getRandomSpotting(String locationName) {
        String template = SPOTTING_CONNECTORS.get(random().nextInt(SPOTTING_CONNECTORS.size()));
        return String.format(template, locationName);
    }
}
