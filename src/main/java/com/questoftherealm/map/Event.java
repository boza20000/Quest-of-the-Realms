package com.questoftherealm.map;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyFactory;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.game.GameState;

public enum Event {

    GOBLIN_CAMP(new EventData(
            "event.goblinCamp.name",
            "event.goblinCamp.desc",
            EnemyType.GOBLIN
    )),

    BANDIT_AMBUSH(new EventData(
            "event.banditAmbush.name",
            "event.banditAmbush.desc",
            EnemyType.BANDIT
    )),

    CURSED_GRAVEYARD(new EventData(
            "event.cursedGraveyard.name",
            "event.cursedGraveyard.desc",
            EnemyType.SKELETON
    )),

    DARK_RITUAL(new EventData(
            "event.darkRitual.name",
            "event.darkRitual.desc",
            EnemyType.DARK_MAGE
    )),

    WOLF_PACK(new EventData(
            "event.wolfPack.name",
            "event.wolfPack.desc",
            EnemyType.WOLF
    )),

    LOST_SPIRIT(new EventData(
            "event.lostSpirit.name",
            "event.lostSpirit.desc",
            EnemyType.LOST_SPIRIT
    )),

    GIANT_SPIDER_NEST(new EventData(
            "event.giantSpiderNest.name",
            "event.giantSpiderNest.desc",
            EnemyType.GIANT_SPIDER
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

    public Enemy createEnemy(Event type,GameState state) {
        return EnemyFactory.createEnemy(type.data.enemyType(), state);
    }

    public static Event generateEvent(TileTypes type, GameState state) {
        return switch (type) {
            case GRASS -> randomOf(state, BANDIT_AMBUSH, WOLF_PACK, LOST_SPIRIT);
            case FOREST -> randomOf(state, WOLF_PACK, CURSED_GRAVEYARD, GIANT_SPIDER_NEST);
            case SWAMP -> randomOf(state, DARK_RITUAL, LOST_SPIRIT, GIANT_SPIDER_NEST);
            case VILLAGE -> randomOf(state, LOST_SPIRIT);
            case CASTLE -> randomOf(state, BANDIT_AMBUSH);
            case MOUNTAIN -> randomOf(state, WOLF_PACK, LOST_SPIRIT);
            case WATER -> randomOf(state, GOBLIN_CAMP, LOST_SPIRIT);
        };
    }

    private static Event randomOf(GameState state, Event... options) {
        return options[state.getGameServices().getRandom().random().nextInt(options.length)];
    }
}
