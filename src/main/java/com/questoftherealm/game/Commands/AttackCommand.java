package com.questoftherealm.game.Commands;

import com.questoftherealm.characters.EnemyEntities.Enemy;

import static com.questoftherealm.characters.EnemyEntities.Enemy.getEnemy;

public class AttackCommand extends Command {
    public AttackCommand(){
        super("attack");
    }

    @Override
    public void execute(String[] args) {
        String target = args[1];
        Enemy enemy  = getEnemy(target);
        //if random input sout unknown enemy
        enemy.battle();
    }

    @Override
    public String getDescription() {
        return "- attack [enemy name]";
    }
}
