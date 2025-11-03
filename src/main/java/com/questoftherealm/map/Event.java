package com.questoftherealm.map;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.entities.*;
import java.util.concurrent.ThreadLocalRandom;

public enum Event {

    GOBLIN_CAMP(new EventData(
            "Goblin Camp",
            "A secret camp of ancient creatures called Goblins. " +
                    "They tend to steal gold and valuable items — and they never travel alone.",
            new Goblin()
    )),

    BANDIT_AMBUSH(new EventData(
            "Bandit Ambush",
            "Ruthless bandits block your path, demanding coin or blood.",
            new Bandit()
    )),

    CURSED_GRAVEYARD(new EventData(
            "Cursed Graveyard",
            "The ground trembles as skeletons crawl out of their graves. " +
                    "Dark magic lingers here — best to run.",
            new Skeleton()
    )),

    DARK_RITUAL(new EventData(
            "Dark Ritual",
            "A sinister mage performs a forbidden ritual. The air reeks of death.",
            new DarkMage()
    )),

    GOBLIN_HORDE(new EventData(
            "Goblin Horde",
            "Dozens of goblins swarm the area, led by a brutish commander.",
            new Goblin()
    )),

    WOLF_PACK(new EventData(
            "Wolf Pack",
            "A pack of hungry wolves stalks you from the shadows. Be ready to fight.",
            new Wolf()
    )),

    TRAVELING_TRADER(new EventData(
            "Traveling Trader",
            "A mysterious trader greets you with a grin. His prices seem... questionable.",
            new TraderNPC()
    )),

    LOST_SPIRIT(new EventData(
            "Lost Spirit",
            "A wandering spirit drifts nearby. It might bless you — or curse you.",
            new Spirit()
    )),

    GIANT_SPIDER_NEST(new EventData(
            "Giant Spider Nest",
            "Thick webs cover the trees. Something massive lurks within.",
            new GiantSpider()
    ));


    private final EventData data;


    Event(final EventData eventData) {
        data = eventData;
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
