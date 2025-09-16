package com.questoftherealm.maps;


import com.questoftherealm.characters.EnemyEntities.*;
import com.questoftherealm.characters.EnemyEntities.entities.*;

import java.util.concurrent.ThreadLocalRandom;


public enum Event {
    GOBLIN_CAMP(
            "Goblin Camp",
            "A secret camp of ancient creatures called Goblins. " +
                    "They tend to steal gold and valuable items, and remember they are always in groups. Keep away if you can.",
            new Goblin()
    ),

    BANDIT_AMBUSH(
            "Bandit Ambush",
            "A group of ruthless bandits jumps out from the shadows. " +
                    "They block your path, demanding coin or blood.You have a choice to make",
            new Bandit()
    ),

    CURSED_GRAVEYARD(
            "Cursed Graveyard",
            "The ground trembles as skeletons crawl out of their graves. " +
                    "Dark magic lingers here, making the dead restless.You should run",
            new Skeleton()
    ),

    DARK_RITUAL(
            "Dark Ritual",
            "A sinister mage is performing a forbidden ritual. " +
                    "The air reeks of burning incense and blood. Interfere at your own risk.",
            new DarkMage()
    ),
    GOBLIN_HORDE(
            "Goblin Horde",
            "Dozens of goblins swarm the area, led by a brutish commander. " +
                    "Their war drums echo in the distance as they prepare to attack.",
            new Goblin() // you could later make a 'GoblinGeneral' class
    ),

    WOLF_PACK(
            "Wolf Pack",
            "A pack of hungry wolves stalks you from the shadows. " +
                    "Their glowing eyes pierce through the darkness before they leap. Be ready to fight",
            new Wolf()
    ),

    GOBLIN_GENERAL(
            "Goblin General",
            "A towering goblin clad in crude armor commands his minions. " +
                    "Defeating him might scatter the smaller goblins nearby.",
            new GoblinGeneral()
    ),

    TRAVELING_TRADER(
            "Traveling Trader",
            "A mysterious trader sets up camp on the roadside. " +
                    "He offers exotic items, but his prices seem suspiciously high.",
            new TraderNPC() // could be non-hostile NPC
    ),

    LOST_SPIRIT(
            "Lost Spirit",
            "A wandering spirit drifts in silence. " +
                    "It may curse you if angered, or bless you if appeased.",
            new Spirit()
    ),

    GIANT_SPIDER_NEST(
            "Giant Spider Nest",
            "Thick webs hang between the trees, and the air is eerily still. " +
                    "All of a sudden everything goes silent. " +
                    "You see a monstrous spider lurks within, waiting for prey.",
            new GiantSpider()
    );


    private final String name;
    private final String description;
    private final Enemy npc;


    Event(String name, String description, Enemy npc) {
        this.name = name;
        this.description = description;
        this.npc = npc;
    }

    public static Event generateEvent(TileTypes type) {
        return switch (type) {
            case GRASS -> randomOf(
                    GOBLIN_CAMP,
                    BANDIT_AMBUSH,
                    WOLF_PACK,
                    TRAVELING_TRADER,
                    LOST_SPIRIT
            );

            case FOREST -> randomOf(
                    GOBLIN_CAMP,
                    GOBLIN_HORDE,
                    WOLF_PACK,
                    CURSED_GRAVEYARD,
                    GIANT_SPIDER_NEST
            );

            case SWAMP -> randomOf(
                    CURSED_GRAVEYARD,
                    DARK_RITUAL,
                    LOST_SPIRIT,
                    GIANT_SPIDER_NEST
            );

            case VILLAGE -> randomOf(
                    BANDIT_AMBUSH,
                    TRAVELING_TRADER,
                    LOST_SPIRIT
            );

            case CASTLE -> randomOf(
                    DARK_RITUAL,
                    BANDIT_AMBUSH,
                    GOBLIN_GENERAL
            );

            case MOUNTAIN -> randomOf(
                    GIANT_SPIDER_NEST,
                    WOLF_PACK,
                    GOBLIN_HORDE,
                    LOST_SPIRIT
            );

            case WATER -> randomOf(
                    LOST_SPIRIT,
                    TRAVELING_TRADER
            );

            case QUEST_LOCATION -> null; // leave empty or special scripted quest logic
        };
    }


    private static Event randomOf(Event... options) {
        return options[ThreadLocalRandom.current().nextInt(options.length)];
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Enemy getNpc() {
        return npc;
    }
}
