package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.map.Event;
import com.questoftherealm.map.TileTypes;

import java.util.List;
import java.util.Random;

public class TravelManger {
    private GameState state;
    private Output output;
    private SlowPrinter slowPrinter;

    private Random random() {
        return state.getGameServices().getRandom().random();
    }

    public TravelManger(GameState s) {
        state = s;
        output = s.getGameServices().getOutput();
        this.slowPrinter = new SlowPrinter(s);
    }

    private final List<String> MOVE_CONNECTORS = List.of(
            "➡️ travel.move.connector1",
            "🚶 travel.move.connector2",
            "🌍 travel.move.connector3",
            "🧭 travel.move.connector4",
            "🏞️ travel.move.connector5",
            "➡️ travel.move.connector6",
            "🌅 travel.move.connector7",
            "🌾 travel.move.connector8"
    );

    private void randomTravelText(String direction, TileTypes start) {
        String fullEntry = MOVE_CONNECTORS.get(random().nextInt(MOVE_CONNECTORS.size()));
        String emoji = fullEntry.substring(0, fullEntry.indexOf(' '));
        String key = fullEntry.substring(fullEntry.indexOf(' ') + 1);
        String line = emoji + " " + MessageBundle.get(key, start, direction);
        slowPrinter.slowPrint(line);
    }

    private void randomEvent(TileTypes type, Player player, GameState state) {
        Event event = Event.generateEvent(type);
        slowPrinter.slowPrint("⚠️ " + MessageBundle.get("travel.event.encounter", event.getName()));
        slowPrinter.slowPrint(event.getDescription());
        boolean investigate = promptYesNo(MessageBundle.get("travel.event.investigate.question"));
        if (investigate) {
            slowPrinter.slowPrint("👉 " + MessageBundle.get("travel.event.investigate.accept"));
            event.getNpc().interact(player, state);
        } else {
            slowPrinter.slowPrint("➡️ " + MessageBundle.get("travel.event.investigate.decline"));
        }
    }

    private boolean promptYesNo(String question) {
        slowPrinter.slowPrint(question + " " + MessageBundle.get("travel.prompt.yesno"));
        while (true) {
            output.print(">");
            String input = state.getGameServices().getInput().nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) return true;
            if (input.equals("no") || input.equals("n")) return false;
            output.println(MessageBundle.get("travel.prompt.invalid"));
        }
    }

    public void pathInteraction(TileTypes start, String direction, Player player, GameState state) {
        if (random().nextInt(100) < 30) { // 30% chance
            randomEvent(start, player, state);
        } else {
            randomTravelText(direction, start);
        }
    }

    public String getTransition(TileTypes start, TileTypes end) {
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

    private final List<String> SPOTTING_CONNECTORS = List.of(
            "👀 travel.spot.connector1",
            "🏕️ travel.spot.connector2",
            "🌄 travel.spot.connector3",
            "🔎 travel.spot.connector4",
            "🌲 travel.spot.connector5",
            "⚔️ travel.spot.connector6",
            "✨ travel.spot.connector7",
            "🌍 travel.spot.connector8",
            "🔥 travel.spot.connector9",
            "🏚️ travel.spot.connector10"
    );

    public String getRandomSpotting(String locationName) {
        String fullEntry = SPOTTING_CONNECTORS.get(random().nextInt(SPOTTING_CONNECTORS.size()));
        String emoji = fullEntry.substring(0, fullEntry.indexOf(' '));
        String key = fullEntry.substring(fullEntry.indexOf(' ') + 1);
        return emoji + " " + MessageBundle.get(key, locationName);
    }
}
