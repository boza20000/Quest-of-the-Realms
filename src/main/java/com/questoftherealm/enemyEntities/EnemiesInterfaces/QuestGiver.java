package com.questoftherealm.enemyEntities.EnemiesInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quest.Quest;

public interface QuestGiver {
    Quest giveQuest(Player player);//some mobs can give you side quests

}
