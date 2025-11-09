package com.questoftherealm.map;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.entities.*;
import com.questoftherealm.localization.MessageBundle;

import java.util.concurrent.ThreadLocalRandom;

public enum Event {

    GOBLIN_CAMP(new EventData(
            MessageBundle.get("event.goblinCamp.name"),
            MessageBundle.get("event.goblinCamp.desc"),
            new Goblin()
    )),

    BANDIT_AMBUSH(new EventData(
            MessageBundle.get("event.banditAmbush.name"),
            MessageBundle.get("event.banditAmbush.desc"),
            new Bandit()
    )),

    CURSED_GRAVEYARD(new EventData(
            MessageBundle.get("event.cursedGraveyard.name"),
            MessageBundle.get("event.cursedGraveyard.desc"),
            new Skeleton()
    )),

    DARK_RITUAL(new EventData(
            MessageBundle.get("event.darkRitual.name"),
            MessageBundle.get("event.darkRitual.desc"),
            new DarkMage()
    )),

    GOBLIN_HORDE(new EventData(
            MessageBundle.get("event.goblinHorde.name"),
            MessageBundle.get("event.goblinHorde.desc"),
            new Goblin()
    )),

    WOLF_PACK(new EventData(
            MessageBundle.get("event.wolfPack.name"),
            MessageBundle.get("event.wolfPack.desc"),
            new Wolf()
    )),

    TRAVELING_TRADER(new EventData(
            MessageBundle.get("event.travelingTrader.name"),
            MessageBundle.get("event.travelingTrader.desc"),
            new SuspiciousTrader()
    )),

    LOST_SPIRIT(new EventData(
            MessageBundle.get("event.lostSpirit.name"),
            MessageBundle.get("event.lostSpirit.desc"),
            new Spirit()
    )),

    GIANT_SPIDER_NEST(new EventData(
            MessageBundle.get("event.giantSpiderNest.name"),
            MessageBundle.get("event.giantSpiderNest.desc"),
            new GiantSpider()
    ));

    private final EventData data;

    Event(final EventData eventData) {
        this.data = eventData;
    }

    public String getName() {
        return data.name();
    }

    public String getDescription() {
        return data.description();
    }

    public Enemy getNpc() {
        return data.npc();
    }

    public static Event generateEvent(TileTypes type) {
        return switch (type) {
            case GRASS -> randomOf(GOBLIN_CAMP, BANDIT_AMBUSH, WOLF_PACK, TRAVELING_TRADER, LOST_SPIRIT);
            case FOREST -> randomOf(GOBLIN_CAMP, GOBLIN_HORDE, WOLF_PACK, CURSED_GRAVEYARD, GIANT_SPIDER_NEST);
            case SWAMP -> randomOf(CURSED_GRAVEYARD, DARK_RITUAL, LOST_SPIRIT, GIANT_SPIDER_NEST);
            case VILLAGE -> randomOf(BANDIT_AMBUSH, TRAVELING_TRADER, LOST_SPIRIT);
            case CASTLE -> randomOf(DARK_RITUAL, BANDIT_AMBUSH);
            case MOUNTAIN -> randomOf(GIANT_SPIDER_NEST, WOLF_PACK, GOBLIN_HORDE, LOST_SPIRIT);
            case WATER -> randomOf(LOST_SPIRIT, TRAVELING_TRADER);
        };
    }

    private static Event randomOf(Event... options) {
        return options[ThreadLocalRandom.current().nextInt(options.length)];
    }
}
