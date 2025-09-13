package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.characters.EnemiesInterfaces.Fightable;
import com.questoftherealm.characters.EnemiesInterfaces.Lootable;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemType;
import com.questoftherealm.maps.TileTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Enemy implements Fightable, Lootable {
    private  String Description;
    private EnemyType type;
    private int health;
    private List<Item> armor;
    private Item weapon;
    boolean isDead ;

    public Enemy(){}


    public Enemy(String description, EnemyType type, int health, List<Item> armor, Item weapon) {
        this.Description = description;
        this.type = type;
        this.health = health;
        this.armor = armor;
        this.weapon = weapon;
        this.isDead = false;
    }

    @Override
    public void attack(Player player) {
        int damage = player.getPlayerCharacter().getAttack();
        player.getPlayerCharacter().takeDamage(damage);

    }

    @Override
    public void takeDamage(int damage) {
        if (getHealth() - damage > 0) {
            setHealth(health - damage);
        }
        isDead = true;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    public static List<Enemy> generateEnemies(TileTypes type) {
        List<EnemyType> chosenEnemies = new ArrayList<>();
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        // Chance of enemies appearing at all
        int spawnChance = rand.nextInt(100); // 0–99
        if (spawnChance < 40) { // 40% chance no enemies
            return toEnemyObj(chosenEnemies); // empty list
        }
        int countRoll = ThreadLocalRandom.current().nextInt(100);
        int enemyCount = 0;

        if (countRoll < GameConstants.THREE_ENEMY_CHANCE) {
            enemyCount = 3;
        } else if (countRoll < GameConstants.TWO_ENEMY_CHANCE) {
            enemyCount = 2;
        } else if (countRoll < GameConstants.ONE_ENEMY_CHANCE) {
            enemyCount = 1;
        }
        EnemyType[] pool = enemyPoolForTile(type);
        for (int i = 0; i < enemyCount; i++) {
            EnemyType picked = pool[rand.nextInt(pool.length)];
            chosenEnemies.add(picked);
        }
        return toEnemyObj(chosenEnemies);
    }

    private static List<Enemy> toEnemyObj(List<EnemyType> enemies) {
        List<Enemy> result = new ArrayList<>();
        for (EnemyType type : enemies) {
            switch (type) {
                case GOBLIN -> result.add(new Goblin());
                case BANDIT -> result.add(new Bandit());
                case SKELETON -> result.add(new Skeleton());
                case WOLF -> result.add(new Wolf());
                case GOBLIN_HORDE -> result.add(new GoblinHorde());
                case DARK_MAGE -> result.add(new DarkMage());
                case GOBLIN_GENERAL -> result.add(new GoblinGeneral());
                case GIANT_SPIDER -> result.add(new GiantSpider());
                case LOST_SPIRIT -> result.add(new Spirit());
                case TRAVELING_TRADER -> result.add(new TraderNPC());
            }
        }
        return result;
    }


    private static EnemyType[] enemyPoolForTile(TileTypes type) {
        return switch (type) {
            case GRASS -> new EnemyType[]{
                    EnemyType.GOBLIN, EnemyType.WOLF, EnemyType.BANDIT, EnemyType.TRAVELING_TRADER
            };
            case FOREST -> new EnemyType[]{
                    EnemyType.GOBLIN, EnemyType.WOLF, EnemyType.GOBLIN_HORDE, EnemyType.GIANT_SPIDER, EnemyType.LOST_SPIRIT
            };
            case SWAMP -> new EnemyType[]{
                    EnemyType.GOBLIN, EnemyType.LOST_SPIRIT, EnemyType.GIANT_SPIDER, EnemyType.SKELETON
            };
            case MOUNTAIN -> new EnemyType[]{
                    EnemyType.BANDIT, EnemyType.GIANT_SPIDER, EnemyType.GOBLIN_GENERAL, EnemyType.WOLF
            };
            case WATER -> new EnemyType[]{
                    EnemyType.LOST_SPIRIT, EnemyType.SKELETON, EnemyType.GOBLIN
            };
            case VILLAGE -> new EnemyType[]{
                    EnemyType.BANDIT, EnemyType.WOLF, EnemyType.TRAVELING_TRADER
            };
            case CASTLE -> new EnemyType[]{
                    EnemyType.DARK_MAGE, EnemyType.GOBLIN_GENERAL, EnemyType.SKELETON
            };
            case QUEST_LOCATION -> new EnemyType[]{
                    // Empty, quest-specific spawns only
            };
        };
    }

    public EnemyType getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public Item getWeapon() {
        return weapon;
    }

    public List<Item> getArmor() {
        return armor;
    }

    public String getDescription() {

        return " ";
    }

    public void setType(EnemyType type) {
        this.type = type;
    }

    public void setArmor(List<Item> armor) {
        this.armor = armor;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    public boolean isDead() {
        return isDead;
    }
}
