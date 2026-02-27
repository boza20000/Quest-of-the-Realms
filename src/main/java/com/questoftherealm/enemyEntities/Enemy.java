package com.questoftherealm.enemyEntities;


import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.EnemiesInterfaces.Fightable;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.items.Item;
import com.questoftherealm.map.TileTypes;
import java.util.ArrayList;
import java.util.List;
import static com.questoftherealm.enemyEntities.EnemyFactory.createEnemy;

public abstract class Enemy implements Fightable {
    private final String description;
    private final EnemyType type;
    private volatile int health;
    private final int baseAttack;
    private final int baseDefense;
    private List<Item> armor;
    private Item weapon;
    private volatile boolean isDead;
    private List<Loot> loot;
    private int xpReward;
    private int goldReward;

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
        this.xpReward = data.xpReward();
        this.goldReward = data.goldReward();
    }


    @Override
    public void attack(Player player, GameState state) {
        int damage = this.getBaseAttack() + (getWeapon() != null ? getWeapon().getPower() : 0);
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("enemy.attack.player",this.getClass().getSimpleName(),damage));
        player.getPlayerCharacter().takeDamage(damage, state,player);
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    @Override
    public void takeDamage(int damage, GameState state) {
        int armorPower = armor.stream().mapToInt(Item::getPower).sum();
        int reducedDamageTaken = Math.max(0, damage - ((getBaseDefense() + armorPower) / 2));
        int newHealth = Math.max(0, getHealth() - reducedDamageTaken);
        setHealth(newHealth);
        isDead = newHealth == 0;
        if (!isDead) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("enemy.armor.block", this.getClass().getSimpleName(), reducedDamageTaken));
        }
    }

    @Override
    public boolean isAlive() {
        return !isDead;
    }

    public static List<Enemy> generateEnemies(TileTypes type, GameState state) {
        List<EnemyType> chosenEnemies = new ArrayList<>();

        RandomService rand = state.getGameServices().getRandom();
        int spawnChance = rand.randomInt(100);
        if (spawnChance > 80) {
            return toEnemyObj(chosenEnemies, state);
        }
        int countRoll = rand.randomInt(100);
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
            EnemyType picked = pool.get(rand.randomInt(pool.size()));
            chosenEnemies.add(picked);
        }
        return toEnemyObj(chosenEnemies, state);
    }

    private static List<Enemy> toEnemyObj(List<EnemyType> enemies, GameState state) {
        List<Enemy> result = new ArrayList<>();
        for (EnemyType type : enemies) {
            result.add(createEnemy(type, state));
        }
        return result;
    }


    private static List<EnemyType> enemyPoolForTile(TileTypes type) {
        return switch (type) {
            case GRASS -> List.of(EnemyType.GOBLIN, EnemyType.WOLF, EnemyType.BANDIT);
            case FOREST -> List.of(EnemyType.GOBLIN, EnemyType.WOLF, EnemyType.GIANT_SPIDER, EnemyType.LOST_SPIRIT);
            case SWAMP -> List.of(EnemyType.GOBLIN, EnemyType.LOST_SPIRIT, EnemyType.GIANT_SPIDER, EnemyType.SKELETON);
            case MOUNTAIN -> List.of(EnemyType.BANDIT, EnemyType.GIANT_SPIDER, EnemyType.WOLF);
            case WATER -> List.of(EnemyType.LOST_SPIRIT, EnemyType.SKELETON, EnemyType.GOBLIN);
            case VILLAGE -> List.of(EnemyType.BANDIT, EnemyType.WOLF);
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
        if (health == 0) {
            isDead = true;
        }
        this.health = health;
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    public boolean isDead() {
        return isDead;
    }

    public List<Loot> getLoot() {
        return loot;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getXpReward() {
        return xpReward;
    }

    public synchronized boolean interact(Player player, GameState state) {
        Battle newBattle = BattleFactory.createBattle(player, this, state);
        return newBattle.simulate();
    }

    public int getGoldReward() {
        return goldReward;
    }
}
