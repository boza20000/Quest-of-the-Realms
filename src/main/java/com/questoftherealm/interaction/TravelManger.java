package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.Event;
import com.questoftherealm.map.TileTypes;

import java.util.List;
import java.util.Random;

public class TravelManger {
    private GameState state;
    private SlowPrinter slowPrinter;

    private Random random() {
        return state.getGameServices().getRandom().random();
    }
    private Output output() {
        return state.getGameServices().getOutput();
    }

    public TravelManger(GameState s) {
        state = s;
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
        String line = emoji + " " + state.getMessages().getBundle().get(key, start, direction);
        slowPrinter.slowPrint(line);
    }

    private void randomEvent(TileTypes type, Player player, GameState state) {
        Event event = Event.generateEvent(type, state);
        String name = state.getMessages().getBundle().get(event.getName());
        String description = state.getMessages().getBundle().get(event.getDescription());
        slowPrinter.slowPrint("⚠️ " + state.getMessages().getBundle().get("travel.event.encounter", name));
        slowPrinter.slowPrint(description);
        boolean investigate = promptYesNo(state.getMessages().getBundle().get("travel.event.investigate.question"));
        Enemy enemy = event.createEnemy(event, state);
        if (investigate) {
            slowPrinter.slowPrint("👉 " + state.getMessages().getBundle().get("travel.event.investigate.accept"));
            enemy.interact(player, state);
        }
        int roll = random().nextInt(10);
        if (roll < 6) {
            slowPrinter.slowPrint("👻 " + state.getMessages().getBundle().get("travel.event.investigate.forced",enemy.getClass().getSimpleName()));
            enemy.interact(player, state);
        }
        slowPrinter.slowPrint("➡️ " + state.getMessages().getBundle().get("travel.event.investigate.decline"));

    }

    private boolean promptYesNo(String question) {
        slowPrinter.slowPrint(question + " " + state.getMessages().getBundle().get("travel.prompt.yesno"));
        while (true) {
            output().print(">");
            output().flush();
            String input = state.getGameServices().getInput().nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) return true;
            if (input.equals("no") || input.equals("n")) return false;
            output().println(state.getMessages().getBundle().get("travel.prompt.invalid"));
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
                case MOUNTAIN -> "⛰️ " + state.getMessages().getBundle().get("travel.transition.same.mountain");
                case FOREST -> "🌲 " + state.getMessages().getBundle().get("travel.transition.same.forest");
                case SWAMP -> "💧 " + state.getMessages().getBundle().get("travel.transition.same.swamp");
                case GRASS -> "🌾 " + state.getMessages().getBundle().get("travel.transition.same.grass");
                case VILLAGE -> "🏘️ " + state.getMessages().getBundle().get("travel.transition.same.village");
                case CASTLE -> "🏰 " + state.getMessages().getBundle().get("travel.transition.same.castle");
                case WATER -> "💦 " + state.getMessages().getBundle().get("travel.transition.same.water");
            };
        }
        return switch (end) {
            case MOUNTAIN -> "⛰️ " + state.getMessages().getBundle().get("travel.transition.to.mountain");
            case FOREST -> "🌲 " + state.getMessages().getBundle().get("travel.transition.to.forest");
            case SWAMP -> "💧 " + state.getMessages().getBundle().get("travel.transition.to.swamp");
            case GRASS -> "🌾 " + state.getMessages().getBundle().get("travel.transition.to.grass");
            case VILLAGE -> "🏘️ " + state.getMessages().getBundle().get("travel.transition.to.village");
            case CASTLE -> "🏰 " + state.getMessages().getBundle().get("travel.transition.to.castle");
            case WATER -> "💦 " + state.getMessages().getBundle().get("travel.transition.to.water");
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
        return emoji + " " + state.getMessages().getBundle().get(key, locationName);
    }
}
