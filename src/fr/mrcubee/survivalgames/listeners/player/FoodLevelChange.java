package fr.mrcubee.survivalgames.listeners.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;

public class FoodLevelChange implements Listener {
	
	private SurvivalGames survivalGames;
	
	public FoodLevelChange(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void foodLevelChangeEvent(FoodLevelChangeEvent event) {
		GameStats gameStats = survivalGames.getGame().getGameStats();
		if (gameStats != GameStats.DURING) {
			event.setFoodLevel(20);
		}
	}
}
