package com.questoftherealm.enemyEntities;

import com.questoftherealm.enemyEntities.EnemiesInterfaces.Fightable;
import com.questoftherealm.enemyEntities.EnemiesInterfaces.Lootable;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.Item;
import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.map.TileTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.questoftherealm.enemyEntities.EnemyFactory.createEnemy;

public abstract class Enemy implements Fightable, Lootable {
    private final String description;
    private final EnemyType type;
    private int health;
    private final int baseAttack;
    private final int baseDefense;
    private List<Item> armor;
    private Item weapon;
    private boolean isDead;
    private List<Loot> loot;

    public Enemy(EnemyData data) {
        this.description = data.description();
        this.type = data.type();
        this.health = data.health();
        this.baseAttack = data.baseAttack();
        this.baseDefense = data.baseDefense();
        this.armor = new ArrayList<>(data.armor());
        this.weapon = data.weapon();
        this.loot = new ArrayList<>(data.loot());
        this.isDead = data.isDead();
    }


    @Override
    public void attack(Player player) {
        int damage = this.getBaseAttack() + (getWeapon() != null ? getWeapon().getPower() : 0);
        System.out.println(MessageBundle.get("enemy.attack.player"));
        player.getPlayerCharacter().takeDamage(damage);
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    @Override
    public void takeDamage(int damage) {
        int reducedDamageTaken = Math.max(0, damage - (getBaseDefense() / 2));
        int newHealth = Math.max(0, getHealth() - reducedDamageTaken);
        setHealth(newHealth);
        isDead = newHealth == 0;
        if (!isDead) {
            System.out.println(MessageBundle.get("enemy.armor.block",this.getClass().getSimpleName(),reducedDamageTaken));
        }
    }

    @Override
    public boolean isAlive() {
        return !isDead;
    }

    public static List<Enemy> generateEnemies(TileTypes type) {
        List<EnemyType> chosenEnemies = new ArrayList<>();
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        // Chance of enemies appearing at all
        int spawnChance = rand.nextInt(100); // 0–99
        if (spawnChance < 60) { // 60% chance no enemies
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
        List<EnemyType> pool = enemyPoolForTile(type);
        for (int i = 0; i < enemyCount; i++) {
            EnemyType picked = pool.get(rand.nextInt(pool.size()));
            chosenEnemies.add(picked);
        }
        return toEnemyObj(chosenEnemies);
    }

    private static List<Enemy> toEnemyObj(List<EnemyType> enemies) {
        List<Enemy> result = new ArrayList<>();
        for (EnemyType type : enemies) {
            result.add(createEnemy(type));
        }
        return result;
    }


    private static List<EnemyType> enemyPoolForTile(TileTypes type) {
        return switch (type) {
            case GRASS -> List.of(EnemyType.GOBLIN, EnemyType.WOLF, EnemyType.BANDIT, EnemyType.SUSPICIOUS_TRADER);
            case FOREST -> List.of(EnemyType.GOBLIN, EnemyType.WOLF, EnemyType.GOBLIN_HORDE, EnemyType.GIANT_SPIDER, EnemyType.LOST_SPIRIT);
            case SWAMP -> List.of(EnemyType.GOBLIN, EnemyType.LOST_SPIRIT, EnemyType.GIANT_SPIDER, EnemyType.SKELETON);
            case MOUNTAIN -> List.of(EnemyType.BANDIT, EnemyType.GIANT_SPIDER, EnemyType.WOLF);
            case WATER -> List.of(EnemyType.LOST_SPIRIT, EnemyType.SKELETON, EnemyType.GOBLIN);
            case VILLAGE -> List.of(EnemyType.BANDIT, EnemyType.WOLF, EnemyType.SUSPICIOUS_TRADER);
            case CASTLE -> List.of(EnemyType.DARK_MAGE, EnemyType.SKELETON);
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

        return description;
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

    public static Item getDefaultWeapon() {
        return null;
    }

    public List<Loot> getLoot() {
        return loot;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public boolean interact(Player player) {
        Battle newBattle = BattleFactory.createBattle(player, this);
        return newBattle.simulate();
    }
}
